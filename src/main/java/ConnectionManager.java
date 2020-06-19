package main.java;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class ConnectionManager {

    private String databaseUrl;
    private String userName;
    private String password;
    private int maxPoolSize = 10;
    private int connNum = 0;

    private static final String SQL_VERIFYCONN = "select 1";

    Stack<Connection> freePool = new Stack<>();
    Set<Connection> occupiedPool = new HashSet<>();

    public ConnectionManager(String databaseUrl, String userName, String password, int maxSize) {
        this.databaseUrl = databaseUrl;
        this.userName = userName;
        this.password = password;
        this.maxPoolSize = maxSize;
    }

    // Get an available connection
    public synchronized Connection getConnection() throws SQLException {
        Connection conn = null;

        if (isFull()) {
            throw new SQLException("The connection pool is full.");
        }

        conn = getConnectionFromPool();

        // If there is no free connection, create a new one.
        if (conn == null) {
            conn = createNewConnectionForPool();
        }

        /*
        If there is no action on one connection for some
        time, the connection is lost. By this, make sure the connection is
        active. Otherwise reconnect it.
        */
        conn = makeAvailable(conn);
        return conn;
    }

    // Return a connection to the pool
    public synchronized void returnConnection (Connection conn) throws SQLException {
        if (conn == null) {
            throw new NullPointerException();
        }
        if (!occupiedPool.remove(conn)) {
            throw new SQLException("The connection is returned already or it isn't for this pool");
        }
        freePool.push(conn);
    }

    // Verify if the connection is full
    private synchronized boolean isFull() {
        return ((freePool.size() == 0) && (connNum >= maxPoolSize));
    }

    // Create a connection for the pool
    private Connection createNewConnectionForPool() throws SQLException {
        Connection conn = createNewConnection();
        connNum++;
        occupiedPool.add(conn);
        return conn;
    }

    // Crate a new connection
    private Connection createNewConnection() throws SQLException {
        Connection conn = null;
        conn = DriverManager.getConnection(databaseUrl, userName, password);
        return conn;
    }

    // Get a connection from the pool. If there is no free connection, return null
    private Connection getConnectionFromPool() {
        Connection conn = null;
        if (freePool.size() > 0) {
            conn = freePool.pop();
            occupiedPool.add(conn);
        }
        return conn;
    }

    // Check, if the connection is available now. Otherwise, reconnect it
    private Connection makeAvailable(Connection conn) throws SQLException {
        if (isConnectionAvailable(conn)) {
            return conn;
        }

        // If the connection is't available, reconnect it.
        occupiedPool.remove(conn);
        connNum--;
        conn.close();

        conn = createNewConnection();
        occupiedPool.add(conn);
        connNum++;
        return conn;
    }

    // By running a sql to verify if the connection is available
    private boolean isConnectionAvailable(Connection conn) {
        try (Statement st = conn.createStatement()) {
            st.executeQuery(SQL_VERIFYCONN);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    // Just an Example
    public static void main(String[] args) throws SQLException {

        Connection conn = null;

        ConnectionManager pool = new ConnectionManager (
                "jdbc:mysql://localhost:3306,localhost:3307/mysql?" +
                        "serverTimezone=CET" +
                        "&autoReconnect=true" +
                        "&connectTimeout=3000" +
                        "&autoReconnectForPools=true",
                "root", "Passw0rd", 2);

        try {
            conn = pool.getConnection();
            try (Statement statement = conn.createStatement())
            {
                ResultSet res = statement.executeQuery("show tables");
                System.out.println("There are below tables:");
                while (res.next()) {
                    String tblName = res.getString(1);
                    System.out.println(tblName);
                }
            }
        }
        finally {
            if (conn != null) {
                pool.returnConnection(conn);
            }
        }

    }


}

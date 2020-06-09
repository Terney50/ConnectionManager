//STEP 1. Import required packages

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionManager {

        // JDBC driver name and database URL
        static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        static final String DB_URL = "jdbc:mysql://localhost/?serverTimezone=CET";

        //  Database credentials
        static final String USR = "User1";
        static final String PSW = "Passw0rd";

    public static void main(String[] args) {
        //System.out.println("Empty...");

        Connection conn = null;
        Statement stmt = null;
        try {
            // STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            // STEP 3: Open a connection
            System.out.println("Connecting to database No.1...");
            conn = DriverManager.getConnection(DB_URL, USR, PSW);

            /*// STEP 4: Execute a query
            System.out.println("Creating database...");
            stmt = conn.createStatement();

            String sql = "CREATE DATABASE BLAH"; // BLAH = DB
            stmt.executeUpdate(sql);
            System.out.println("Database created successfully...");*/
        } catch (SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // Finally block used to close resources
            try {
                if(stmt != null)
                    stmt.close();
                    System.out.println("stmt close...");
            } catch (SQLException se2) {
            }// Nothing we can do
            try {
                if(conn != null)
                    conn.close();
                    System.out.println("conn close...");
            } catch (SQLException se) {
                se.printStackTrace();
            }// End finally try
        }// End try
        System.out.println("Connection to database No.1 tested");
    }// End main


}// End ConnectionManager

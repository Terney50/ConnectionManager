//STEP 1. Import required packages

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionManager {

        // JDBC driver name and database URL
        static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        static final String DB_URL = "jdbc:mysql://localhost:3306/?serverTimezone=CET"; // S1
        static final String DB_URL_S2 = "jdbc:mysql://localhost:3307/?serverTimezone=CET"; // S2

        //  Database credentials
        static final String USR = "root"; // S1
        static final String PSW = "Passw0rd"; // S1
        static final String USR_S2 = "root"; // S2
        static final String PSW_S2 = "Passw0rd"; // S2


    public static void main(String[] args) {
        //System.out.println("Empty...");

        // S1
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

        // S2
        Connection conn2 = null;
        Statement stmt2 = null;
        try {
            // Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            // STEP 3: Open a connection
            System.out.println("Connecting to database No.2...");
            conn = DriverManager.getConnection(DB_URL_S2, USR_S2, PSW_S2);

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
                if(stmt2 != null)
                    stmt2.close();
                System.out.println("stmt2 close...");
            } catch (SQLException se2) {
            }// Nothing we can do
            try {
                if(conn2 != null)
                    conn2.close();
                System.out.println("conn2 close...");
            } catch (SQLException se) {
                se.printStackTrace();
            }// End finally try
        }// End try
        System.out.println("Connection to database No.2 tested");
    }// End main


}// End ConnectionManager

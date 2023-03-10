package Utilities;

import java.sql.*;

/**
 * @Author Ryan Wilkinson
 * SoftWare II
 */

/**
 * Creating class to connect MySQL database to project
 */
public class DataBaseConnection {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    public static Connection connection;  // Connection Interface

    public static Connection openConnection() {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Connection successful!");
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }
    public static java.sql.Connection getConnection(){
        return connection;
    }

    private static PreparedStatement preparedStatement;

    public static void setPreparedStatement(Connection connection, String query) throws SQLException {
        preparedStatement = connection.prepareStatement(query);
    }
    public static PreparedStatement getPreparedStatement(){
        return preparedStatement;
    }


}



package com.jobtracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://core-pipeline-db-fullstack-services.g.aivencloud.com:24710/defaultdb?sslMode=REQUIRED";
    private static final String USER = "avnadmin";
    
    // This looks for the DB_PASSWORD environment variable set in Render
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        if (PASSWORD == null) {
            throw new SQLException("DB_PASSWORD environment variable is not set!");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
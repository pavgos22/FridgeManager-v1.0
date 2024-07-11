package com.food.manager.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public enum DbManager {
    INSTANCE;

    private Connection connection;

    DbManager() {
        Properties connectionProps = new Properties();
        connectionProps.put("user", "user1");
        connectionProps.put("password", "user1");
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/app_db" +
                            "?serverTimezone=Europe/Warsaw" +
                            "&useSSL=False" +
                            "&allowPublicKeyRetrieval=true",
                    connectionProps);
        } catch (SQLException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static DbManager getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() {
        return connection;
    }
}

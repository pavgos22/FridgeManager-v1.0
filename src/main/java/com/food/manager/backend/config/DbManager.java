package com.food.manager.backend.config;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Getter
public enum DbManager {
    INSTANCE;

    private final Connection connection;

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

}

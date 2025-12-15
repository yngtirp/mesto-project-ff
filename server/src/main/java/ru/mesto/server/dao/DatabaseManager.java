package ru.mesto.server.dao;

import ru.mesto.server.config.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;
    private Config config;

    private DatabaseManager() {
        config = Config.getInstance();
        initializeDatabase();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void initializeDatabase() {
        try {
            // Load H2 driver
            Class.forName("org.h2.Driver");
            
            // Create data directory if it doesn't exist
            String dbUrl = config.getProperty("db.url");
            java.io.File dataDir = new java.io.File("./data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            
            connection = DriverManager.getConnection(dbUrl, 
                config.getProperty("db.user"), 
                config.getProperty("db.password"));
            
            createTables();
        } catch (ClassNotFoundException e) {
            System.err.println("H2 Driver not found: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Users table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                "_id VARCHAR(255) PRIMARY KEY, " +
                "name VARCHAR(255), " +
                "about TEXT, " +
                "avatar TEXT, " +
                "\"authorization\" VARCHAR(255) UNIQUE" +
                ")");

            // Cards table
            stmt.execute("CREATE TABLE IF NOT EXISTS cards (" +
                "_id VARCHAR(255) PRIMARY KEY, " +
                "name VARCHAR(255), " +
                "link TEXT, " +
                "owner_id VARCHAR(255), " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (owner_id) REFERENCES users(_id)" +
                ")");

            // Likes table
            stmt.execute("CREATE TABLE IF NOT EXISTS likes (" +
                "_id VARCHAR(255) PRIMARY KEY, " +
                "user_id VARCHAR(255), " +
                "card_id VARCHAR(255), " +
                "FOREIGN KEY (user_id) REFERENCES users(_id), " +
                "FOREIGN KEY (card_id) REFERENCES cards(_id), " +
                "UNIQUE(user_id, card_id)" +
                ")");
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                String dbUrl = config.getProperty("db.url");
                connection = DriverManager.getConnection(dbUrl, 
                    config.getProperty("db.user"), 
                    config.getProperty("db.password"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting connection: " + e.getMessage());
        }
        return connection;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}


package ru.mesto.server.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static Config instance;
    private Properties properties;

    private Config() {
        properties = new Properties();
        try {
            var inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
            if (inputStream != null) {
                properties.load(inputStream);
                inputStream.close();
            } else {
                String configPath = System.getProperty("config.path", 
                    "src/main/resources/config.properties");
                properties.load(new FileInputStream(configPath));
            }
        } catch (IOException e) {
            System.err.println("Error loading config: " + e.getMessage() + ". Using defaults.");
            properties.setProperty("server.port", "3000");
            properties.setProperty("server.host", "localhost");
            properties.setProperty("db.url", "jdbc:h2:file:./data/mesto;AUTO_SERVER=TRUE");
            properties.setProperty("log.level", "INFO");
            properties.setProperty("log.file.path", "./logs/");
            properties.setProperty("log.file.name", "server.log");
            properties.setProperty("db.user", "sa");
            properties.setProperty("db.password", "");
        }
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
}


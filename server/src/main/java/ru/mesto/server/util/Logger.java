package ru.mesto.server.util;

import ru.mesto.server.config.Config;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static Logger instance;
    private PrintWriter fileWriter;
    private String logLevel;
    private Config config;

    private Logger() {
        config = Config.getInstance();
        logLevel = config.getProperty("log.level");
        try {
            String logPath = config.getProperty("log.file.path");
            String logFileName = config.getProperty("log.file.name");
            java.io.File logDir = new java.io.File(logPath);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            fileWriter = new PrintWriter(new FileWriter(logPath + logFileName, true));
        } catch (IOException e) {
            System.err.println("Error initializing logger: " + e.getMessage());
        }
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void info(String message) {
        if (shouldLog("INFO")) {
            log("INFO", message);
        }
    }

    public void error(String message) {
        if (shouldLog("ERROR")) {
            log("ERROR", message);
        }
    }

    public void debug(String message) {
        if (shouldLog("DEBUG")) {
            log("DEBUG", message);
        }
    }

    private boolean shouldLog(String level) {
        if (logLevel == null) return true;
        switch (logLevel) {
            case "DEBUG":
                return true;
            case "INFO":
                return !level.equals("DEBUG");
            case "ERROR":
                return level.equals("ERROR");
            default:
                return true;
        }
    }

    private void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String logMessage = String.format("[%s] [%s] %s", timestamp, level, message);
        System.out.println(logMessage);
        if (fileWriter != null) {
            fileWriter.println(logMessage);
            fileWriter.flush();
        }
    }

    public void close() {
        if (fileWriter != null) {
            fileWriter.close();
        }
    }
}


package ru.mesto.server;

import ru.mesto.server.dao.DatabaseManager;
import ru.mesto.server.util.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class AdminConsole {
    private static final Logger logger = Logger.getInstance();
    private boolean running = true;

    public void start() {
        System.out.println("\n=== Admin Console ===");
        System.out.println("Available commands:");
        System.out.println("  stats - Show server statistics");
        System.out.println("  users - List all users");
        System.out.println("  cards - List all cards");
        System.out.println("  help - Show this help");
        System.out.println("  exit - Stop the server");
        System.out.println("===================\n");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (running) {
                System.out.print("admin> ");
                String command = reader.readLine();
                if (command == null) break;
                
                command = command.trim().toLowerCase();
                
                switch (command) {
                    case "stats":
                        showStats();
                        break;
                    case "users":
                        showUsers();
                        break;
                    case "cards":
                        showCards();
                        break;
                    case "help":
                        showHelp();
                        break;
                    case "exit":
                        running = false;
                        System.exit(0);
                        break;
                    default:
                        if (!command.isEmpty()) {
                            System.out.println("Unknown command. Type 'help' for available commands.");
                        }
                }
            }
        } catch (Exception e) {
            logger.error("Error in admin console: " + e.getMessage());
        }
    }

    private void showStats() {
        try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
            Connection conn = dbManager.getConnection();
            
            int userCount = 0;
            int cardCount = 0;
            int likeCount = 0;
            
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM users");
                if (rs.next()) userCount = rs.getInt("count");
                
                rs = stmt.executeQuery("SELECT COUNT(*) as count FROM cards");
                if (rs.next()) cardCount = rs.getInt("count");
                
                rs = stmt.executeQuery("SELECT COUNT(*) as count FROM likes");
                if (rs.next()) likeCount = rs.getInt("count");
            }
            
            System.out.println("\n=== Server Statistics ===");
            System.out.println("Users: " + userCount);
            System.out.println("Cards: " + cardCount);
            System.out.println("Likes: " + likeCount);
            System.out.println("=======================\n");
            
            logger.info("Admin: stats command executed");
        } catch (Exception e) {
            System.out.println("Error getting statistics: " + e.getMessage());
            logger.error("Error in stats command: " + e.getMessage());
        }
    }

    private void showUsers() {
        try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
            Connection conn = dbManager.getConnection();
            
            System.out.println("\n=== Users ===");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT _id, name, about FROM users")) {
                int count = 0;
                while (rs.next()) {
                    count++;
                    System.out.println(count + ". ID: " + rs.getString("_id"));
                    System.out.println("   Name: " + rs.getString("name"));
                    System.out.println("   About: " + rs.getString("about"));
                    System.out.println();
                }
                if (count == 0) {
                    System.out.println("No users found.");
                }
            }
            System.out.println("============\n");
            
            logger.info("Admin: users command executed");
        } catch (Exception e) {
            System.out.println("Error getting users: " + e.getMessage());
            logger.error("Error in users command: " + e.getMessage());
        }
    }

    private void showCards() {
        try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
            Connection conn = dbManager.getConnection();
            
            System.out.println("\n=== Cards ===");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT _id, name, link, owner_id FROM cards ORDER BY created_at DESC")) {
                int count = 0;
                while (rs.next()) {
                    count++;
                    System.out.println(count + ". ID: " + rs.getString("_id"));
                    System.out.println("   Name: " + rs.getString("name"));
                    System.out.println("   Link: " + rs.getString("link"));
                    System.out.println("   Owner: " + rs.getString("owner_id"));
                    System.out.println();
                }
                if (count == 0) {
                    System.out.println("No cards found.");
                }
            }
            System.out.println("============\n");
            
            logger.info("Admin: cards command executed");
        } catch (Exception e) {
            System.out.println("Error getting cards: " + e.getMessage());
            logger.error("Error in cards command: " + e.getMessage());
        }
    }

    private void showHelp() {
        System.out.println("\n=== Available Commands ===");
        System.out.println("  stats - Show server statistics");
        System.out.println("  users - List all users");
        System.out.println("  cards - List all cards");
        System.out.println("  help - Show this help");
        System.out.println("  exit - Stop the server");
        System.out.println("==========================\n");
    }
}


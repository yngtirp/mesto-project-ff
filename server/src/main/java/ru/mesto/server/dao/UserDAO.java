package ru.mesto.server.dao;

import ru.mesto.server.model.User;
import ru.mesto.server.util.PasswordUtil;

import java.sql.*;
import java.util.UUID;

public class UserDAO {
    private DatabaseManager dbManager;

    public UserDAO() {
        dbManager = DatabaseManager.getInstance();
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by email: " + e.getMessage());
        }
        return null;
    }

    public User registerUser(String email, String password, String name, String about, String avatar) {
        if (getUserByEmail(email) != null) {
            return null;
        }

        String userId = UUID.randomUUID().toString();
        String token = UUID.randomUUID().toString();
        String hashedPassword = PasswordUtil.hashPassword(password);
        
        String sql = "INSERT INTO users (_id, email, password, name, about, avatar, \"authorization\") VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.setString(2, email);
            stmt.setString(3, hashedPassword);
            stmt.setString(4, name);
            stmt.setString(5, about != null ? about : "");
            stmt.setString(6, avatar != null ? avatar : "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRnXqIkcn-r6Mzz93BpSx5qTEcYB-SlZPcPSg&s");
            stmt.setString(7, token);
            stmt.executeUpdate();
            return getUserByAuthorization(token);
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
        }
        return null;
    }

    public User loginUser(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password");
                if (storedHash != null && PasswordUtil.verifyPassword(password, storedHash)) {
                    String token = UUID.randomUUID().toString();
                    String userId = rs.getString("_id");
                    updateToken(userId, token);
                    return getUserByAuthorization(token);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error logging in user: " + e.getMessage());
        }
        return null;
    }

    private void updateToken(String userId, String token) {
        String sql = "UPDATE users SET \"authorization\" = ? WHERE _id = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, token);
            stmt.setString(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating token: " + e.getMessage());
        }
    }

    public User getUserByAuthorization(String authorization) {
        String sql = "SELECT * FROM users WHERE \"authorization\" = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, authorization);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting user: " + e.getMessage());
        }
        return null;
    }

    public User createUser(String authorization) {
        String userId = UUID.randomUUID().toString();
        String sql = "INSERT INTO users (_id, name, about, avatar, \"authorization\") VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.setString(2, "Жак-Ив Кусто");
            stmt.setString(3, "Исследователь");
            stmt.setString(4, "https://pictures.s3.yandex.net/resources/jacques-cousteau_1604399756.png");
            stmt.setString(5, authorization);
            stmt.executeUpdate();
            return getUserByAuthorization(authorization);
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
        }
        return null;
    }

    public User updateUser(String authorization, String name, String about) {
        String sql = "UPDATE users SET name = ?, about = ? WHERE \"authorization\" = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, about);
            stmt.setString(3, authorization);
            stmt.executeUpdate();
            return getUserByAuthorization(authorization);
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
        }
        return null;
    }

    public User updateAvatar(String authorization, String avatar) {
        String sql = "UPDATE users SET avatar = ? WHERE \"authorization\" = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, avatar);
            stmt.setString(2, authorization);
            stmt.executeUpdate();
            return getUserByAuthorization(authorization);
        } catch (SQLException e) {
            System.err.println("Error updating avatar: " + e.getMessage());
        }
        return null;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.set_id(rs.getString("_id"));
        user.setName(rs.getString("name"));
        user.setAbout(rs.getString("about"));
        user.setAvatar(rs.getString("avatar"));
        user.setAuthorization(rs.getString("authorization"));
        return user;
    }
}


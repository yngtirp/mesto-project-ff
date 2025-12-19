package ru.mesto.server.dao;

import ru.mesto.server.model.Card;
import ru.mesto.server.model.Like;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardDAO {
    private DatabaseManager dbManager;

    public CardDAO() {
        dbManager = DatabaseManager.getInstance();
    }

    public List<Card> getAllCards() {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT * FROM cards ORDER BY created_at DESC";
        try (Statement stmt = dbManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Card card = mapResultSetToCard(rs);
                card.setLikes(getLikesForCard(card.get_id()));
                cards.add(card);
            }
        } catch (SQLException e) {
            System.err.println("Error getting cards: " + e.getMessage());
        }
        return cards;
    }

    public Card createCard(String name, String link, String ownerId) {
        String cardId = UUID.randomUUID().toString();
        String sql = "INSERT INTO cards (_id, name, link, owner_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, cardId);
            stmt.setString(2, name);
            stmt.setString(3, link);
            stmt.setString(4, ownerId);
            stmt.executeUpdate();
            return getCardById(cardId);
        } catch (SQLException e) {
            System.err.println("Error creating card: " + e.getMessage());
        }
        return null;
    }

    public Card getCardById(String cardId) {
        String sql = "SELECT * FROM cards WHERE _id = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, cardId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Card card = mapResultSetToCard(rs);
                card.setLikes(getLikesForCard(cardId));
                return card;
            }
        } catch (SQLException e) {
            System.err.println("Error getting card: " + e.getMessage());
        }
        return null;
    }

    public boolean deleteCard(String cardId, String userId) {
        String checkSql = "SELECT owner_id FROM cards WHERE _id = ?";
        try (PreparedStatement checkStmt = dbManager.getConnection().prepareStatement(checkSql)) {
            checkStmt.setString(1, cardId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getString("owner_id").equals(userId)) {
                String deleteLikesSql = "DELETE FROM likes WHERE card_id = ?";
                try (PreparedStatement deleteLikesStmt = dbManager.getConnection().prepareStatement(deleteLikesSql)) {
                    deleteLikesStmt.setString(1, cardId);
                    deleteLikesStmt.executeUpdate();
                }
                String deleteSql = "DELETE FROM cards WHERE _id = ?";
                try (PreparedStatement deleteStmt = dbManager.getConnection().prepareStatement(deleteSql)) {
                    deleteStmt.setString(1, cardId);
                    return deleteStmt.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error deleting card: " + e.getMessage());
        }
        return false;
    }

    public Card likeCard(String cardId, String userId) {
        String likeId = UUID.randomUUID().toString();
        String sql = "INSERT INTO likes (_id, user_id, card_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, likeId);
            stmt.setString(2, userId);
            stmt.setString(3, cardId);
            stmt.executeUpdate();
            return getCardById(cardId);
        } catch (SQLException e) {
            return getCardById(cardId);
        }
    }

    public Card unlikeCard(String cardId, String userId) {
        String sql = "DELETE FROM likes WHERE card_id = ? AND user_id = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, cardId);
            stmt.setString(2, userId);
            stmt.executeUpdate();
            return getCardById(cardId);
        } catch (SQLException e) {
            System.err.println("Error unliking card: " + e.getMessage());
        }
        return getCardById(cardId);
    }

    private List<Like> getLikesForCard(String cardId) {
        List<Like> likes = new ArrayList<>();
        String sql = "SELECT * FROM likes WHERE card_id = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, cardId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Like like = new Like();
                like.set_id(rs.getString("_id"));
                like.setUser_id(rs.getString("user_id"));
                like.setCard_id(rs.getString("card_id"));
                likes.add(like);
            }
        } catch (SQLException e) {
            System.err.println("Error getting likes: " + e.getMessage());
        }
        return likes;
    }

    private Card mapResultSetToCard(ResultSet rs) throws SQLException {
        Card card = new Card();
        card.set_id(rs.getString("_id"));
        card.setName(rs.getString("name"));
        card.setLink(rs.getString("link"));
        card.setOwner_id(rs.getString("owner_id"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            card.setCreatedAt(createdAt.toString());
        }
        return card;
    }
}


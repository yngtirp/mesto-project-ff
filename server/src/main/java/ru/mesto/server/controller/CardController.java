package ru.mesto.server.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ru.mesto.server.dao.CardDAO;
import ru.mesto.server.dao.UserDAO;
import ru.mesto.server.model.Card;
import ru.mesto.server.model.CardResponse;
import ru.mesto.server.model.Like;
import ru.mesto.server.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CardController {
    private CardDAO cardDAO;
    private UserDAO userDAO;
    private Gson gson;

    public CardController() {
        cardDAO = new CardDAO();
        userDAO = new UserDAO();
        gson = new Gson();
    }

    public String handleGetCards() {
        List<Card> cards = cardDAO.getAllCards();
        List<CardResponse> responses = cards.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
        return gson.toJson(responses);
    }

    public String handleCreateCard(String authorization, BufferedReader reader) throws IOException {
        User user = userDAO.getUserByAuthorization(authorization);
        if (user == null) {
            return "{\"error\":\"User not found\"}";
        }

        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }

        JsonObject json = gson.fromJson(body.toString(), JsonObject.class);
        String name = json.get("name").getAsString();
        String link = json.get("link").getAsString();

        Card card = cardDAO.createCard(name, link, user.get_id());
        CardResponse response = convertToResponse(card);
        return gson.toJson(response);
    }

    public String handleDeleteCard(String cardId, String authorization) {
        User user = userDAO.getUserByAuthorization(authorization);
        if (user == null) {
            return "{\"error\":\"User not found\"}";
        }

        boolean deleted = cardDAO.deleteCard(cardId, user.get_id());
        if (deleted) {
            return "{\"message\":\"Card deleted\"}";
        } else {
            return "{\"error\":\"Card not found or access denied\"}";
        }
    }

    public String handleLikeCard(String cardId, String authorization) {
        User user = userDAO.getUserByAuthorization(authorization);
        if (user == null) {
            return "{\"error\":\"User not found\"}";
        }

        Card card = cardDAO.likeCard(cardId, user.get_id());
        CardResponse response = convertToResponse(card);
        return gson.toJson(response);
    }

    public String handleUnlikeCard(String cardId, String authorization) {
        User user = userDAO.getUserByAuthorization(authorization);
        if (user == null) {
            return "{\"error\":\"User not found\"}";
        }

        Card card = cardDAO.unlikeCard(cardId, user.get_id());
        CardResponse response = convertToResponse(card);
        return gson.toJson(response);
    }

    private CardResponse convertToResponse(Card card) {
        if (card == null) {
            return null;
        }
        CardResponse response = new CardResponse();
        response.set_id(card.get_id());
        response.setName(card.getName());
        response.setLink(card.getLink());
        response.setCreatedAt(card.getCreatedAt());
        
        CardResponse.Owner owner = new CardResponse.Owner(card.getOwner_id());
        response.setOwner(owner);
        
        List<CardResponse.Owner> likes = new ArrayList<>();
        if (card.getLikes() != null) {
            for (Like like : card.getLikes()) {
                likes.add(new CardResponse.Owner(like.getUser_id()));
            }
        }
        response.setLikes(likes);
        
        return response;
    }
}


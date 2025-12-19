package ru.mesto.server.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ru.mesto.server.dao.UserDAO;
import ru.mesto.server.model.User;

import java.io.BufferedReader;
import java.io.IOException;

public class UserController {
    private UserDAO userDAO;
    private Gson gson;

    public UserController() {
        userDAO = new UserDAO();
        gson = new Gson();
    }

    public String handleGetUser(String authorization) {
        if (authorization == null || authorization.isEmpty()) {
            return "{\"error\":\"Unauthorized\"}";
        }
        User user = userDAO.getUserByAuthorization(authorization);
        if (user == null) {
            user = userDAO.createUser(authorization);
        }
        return gson.toJson(user);
    }

    public String handleUpdateUser(String authorization, BufferedReader reader) throws IOException {
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }

        JsonObject json = gson.fromJson(body.toString(), JsonObject.class);
        String name = json.get("name").getAsString();
        String about = json.get("about").getAsString();

        User user = userDAO.updateUser(authorization, name, about);
        return gson.toJson(user);
    }

    public String handleUpdateAvatar(String authorization, BufferedReader reader) throws IOException {
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }

        JsonObject json = gson.fromJson(body.toString(), JsonObject.class);
        String avatar = json.get("avatar").getAsString();

        User user = userDAO.updateAvatar(authorization, avatar);
        return gson.toJson(user);
    }
}


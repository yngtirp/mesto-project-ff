package ru.mesto.server.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ru.mesto.server.dao.UserDAO;
import ru.mesto.server.model.User;

import java.io.BufferedReader;
import java.io.IOException;

public class AuthController {
    private UserDAO userDAO;
    private Gson gson;

    public AuthController() {
        userDAO = new UserDAO();
        gson = new Gson();
    }

    public String handleSignup(BufferedReader reader) throws IOException {
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }

        JsonObject json = gson.fromJson(body.toString(), JsonObject.class);
        String email = json.get("email").getAsString();
        String password = json.get("password").getAsString();
        String name = json.has("name") ? json.get("name").getAsString() : email.split("@")[0];
        String about = json.has("about") ? json.get("about").getAsString() : "";
        String avatar = json.has("avatar") ? json.get("avatar").getAsString() : 
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRnXqIkcn-r6Mzz93BpSx5qTEcYB-SlZPcPSg&s";

        User user = userDAO.registerUser(email, password, name, about, avatar);
        if (user != null) {
            return gson.toJson(user);
        } else {
            return "{\"error\":\"User already exists\"}";
        }
    }

    public String handleSignin(BufferedReader reader) throws IOException {
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }

        JsonObject json = gson.fromJson(body.toString(), JsonObject.class);
        String email = json.get("email").getAsString();
        String password = json.get("password").getAsString();

        User user = userDAO.loginUser(email, password);
        if (user != null) {
            return gson.toJson(user);
        } else {
            return "{\"error\":\"Invalid email or password\"}";
        }
    }
}


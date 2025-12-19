package ru.mesto.server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import ru.mesto.server.config.Config;
import ru.mesto.server.controller.AuthController;
import ru.mesto.server.controller.CardController;
import ru.mesto.server.controller.UserController;
import ru.mesto.server.util.Logger;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Server {
    private static final Logger logger = Logger.getInstance();
    private static Config config = Config.getInstance();
    private HttpServer server;
    private UserController userController;
    private CardController cardController;
    private AuthController authController;
    private AdminConsole adminConsole;

    public Server() {
        userController = new UserController();
        cardController = new CardController();
        authController = new AuthController();
        adminConsole = new AdminConsole();
    }

    public void start() {
        try {
            int port = config.getIntProperty("server.port", 3000);
            server = HttpServer.create(new InetSocketAddress(port), 0);
            
            server.createContext("/", new CORSHandler());
            
            server.createContext("/signup", new SignupHandler());
            server.createContext("/signin", new SigninHandler());
            
            server.createContext("/users/me/avatar", new AvatarHandler());
            server.createContext("/users/me", new UserHandler());
            
            server.createContext("/cards/likes", new LikeHandler());
            server.createContext("/cards", new CardHandler());
            
            server.setExecutor(Executors.newCachedThreadPool());
            server.start();
            
            logger.info("Server started on port " + port);
            System.out.println("Server started on http://localhost:" + port);
            
            new Thread(() -> adminConsole.start()).start();
            
        } catch (IOException e) {
            logger.error("Error starting server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            logger.info("Server stopped");
        }
    }

    class CORSHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCORSHeaders(exchange);
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                exchange.close();
                return;
            }
            exchange.sendResponseHeaders(404, 0);
            exchange.close();
        }
    }

    class UserHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCORSHeaders(exchange);
            String method = exchange.getRequestMethod();
            
            if ("OPTIONS".equals(method)) {
                exchange.sendResponseHeaders(200, -1);
                exchange.close();
                return;
            }
            
            String authorization = getAuthorization(exchange);
            
            logger.info(method + " /users/me - Authorization: " + authorization);
            
            try {
                if ("GET".equals(method)) {
                    String response = userController.handleGetUser(authorization);
                    if (response.contains("\"error\"")) {
                        sendResponse(exchange, 401, response);
                    } else {
                        sendResponse(exchange, 200, response);
                    }
                } else if ("PATCH".equals(method)) {
                    String response = userController.handleUpdateUser(authorization, 
                        new BufferedReader(new InputStreamReader(exchange.getRequestBody())));
                    sendResponse(exchange, 200, response);
                } else {
                    sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
                }
            } catch (Exception e) {
                logger.error("Error handling user request: " + e.getMessage());
                sendResponse(exchange, 500, "{\"error\":\"Internal server error\"}");
            }
        }
    }

    class AvatarHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCORSHeaders(exchange);
            String method = exchange.getRequestMethod();
            
            if ("OPTIONS".equals(method)) {
                exchange.sendResponseHeaders(200, -1);
                exchange.close();
                return;
            }
            
            String authorization = getAuthorization(exchange);
            
            logger.info(method + " /users/me/avatar - Authorization: " + authorization);
            
            try {
                if ("PATCH".equals(method)) {
                    String response = userController.handleUpdateAvatar(authorization, 
                        new BufferedReader(new InputStreamReader(exchange.getRequestBody())));
                    sendResponse(exchange, 200, response);
                } else {
                    sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
                }
            } catch (Exception e) {
                logger.error("Error handling avatar request: " + e.getMessage());
                sendResponse(exchange, 500, "{\"error\":\"Internal server error\"}");
            }
        }
    }

    class CardHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCORSHeaders(exchange);
            String method = exchange.getRequestMethod();
            
            if ("OPTIONS".equals(method)) {
                exchange.sendResponseHeaders(200, -1);
                exchange.close();
                return;
            }
            
            String authorization = getAuthorization(exchange);
            String path = exchange.getRequestURI().getPath();
            
            logger.info(method + " " + path + " - Authorization: " + authorization);
            
            try {
                if ("GET".equals(method) && path.equals("/cards")) {
                    String response = cardController.handleGetCards();
                    sendResponse(exchange, 200, response);
                } else if ("POST".equals(method) && path.equals("/cards")) {
                    String response = cardController.handleCreateCard(authorization, 
                        new BufferedReader(new InputStreamReader(exchange.getRequestBody())));
                    sendResponse(exchange, 200, response);
                } else if ("DELETE".equals(method) && path.startsWith("/cards/") && !path.contains("/likes/")) {
                    String cardId = path.substring("/cards/".length());
                    String response = cardController.handleDeleteCard(cardId, authorization);
                    sendResponse(exchange, 200, response);
                } else {
                    sendResponse(exchange, 404, "{\"error\":\"Not found\"}");
                }
            } catch (Exception e) {
                logger.error("Error handling card request: " + e.getMessage());
                sendResponse(exchange, 500, "{\"error\":\"Internal server error\"}");
            }
        }
    }

    class LikeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCORSHeaders(exchange);
            String method = exchange.getRequestMethod();
            
            if ("OPTIONS".equals(method)) {
                exchange.sendResponseHeaders(200, -1);
                exchange.close();
                return;
            }
            
            String authorization = getAuthorization(exchange);
            String path = exchange.getRequestURI().getPath();
            
            String cardId = null;
            if (path.startsWith("/cards/likes/")) {
                cardId = path.substring("/cards/likes/".length());
            } else if (path.startsWith("/cards//likes/")) {
                cardId = path.substring("/cards//likes/".length());
            }
            
            if (cardId == null || cardId.isEmpty()) {
                sendResponse(exchange, 400, "{\"error\":\"Invalid card ID\"}");
                return;
            }
            
            logger.info(method + " " + path + " - Authorization: " + authorization);
            
            try {
                if ("PUT".equals(method)) {
                    String response = cardController.handleLikeCard(cardId, authorization);
                    sendResponse(exchange, 200, response);
                } else if ("DELETE".equals(method)) {
                    String response = cardController.handleUnlikeCard(cardId, authorization);
                    sendResponse(exchange, 200, response);
                } else {
                    sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
                }
            } catch (Exception e) {
                logger.error("Error handling like request: " + e.getMessage());
                sendResponse(exchange, 500, "{\"error\":\"Internal server error\"}");
            }
        }
    }

    class SignupHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCORSHeaders(exchange);
            String method = exchange.getRequestMethod();
            
            if ("OPTIONS".equals(method)) {
                exchange.sendResponseHeaders(200, -1);
                exchange.close();
                return;
            }
            
            logger.info(method + " /signup");
            
            try {
                if ("POST".equals(method)) {
                    String response = authController.handleSignup(
                        new BufferedReader(new InputStreamReader(exchange.getRequestBody())));
                    if (response.contains("\"error\"")) {
                        sendResponse(exchange, 400, response);
                    } else {
                        sendResponse(exchange, 200, response);
                    }
                } else {
                    sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
                }
            } catch (Exception e) {
                logger.error("Error handling signup request: " + e.getMessage());
                sendResponse(exchange, 500, "{\"error\":\"Internal server error\"}");
            }
        }
    }

    class SigninHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCORSHeaders(exchange);
            String method = exchange.getRequestMethod();
            
            if ("OPTIONS".equals(method)) {
                exchange.sendResponseHeaders(200, -1);
                exchange.close();
                return;
            }
            
            logger.info(method + " /signin");
            
            try {
                if ("POST".equals(method)) {
                    String response = authController.handleSignin(
                        new BufferedReader(new InputStreamReader(exchange.getRequestBody())));
                    if (response.contains("\"error\"")) {
                        sendResponse(exchange, 401, response);
                    } else {
                        sendResponse(exchange, 200, response);
                    }
                } else {
                    sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
                }
            } catch (Exception e) {
                logger.error("Error handling signin request: " + e.getMessage());
                sendResponse(exchange, 500, "{\"error\":\"Internal server error\"}");
            }
        }
    }

    private void setCORSHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        exchange.getResponseHeaders().add("Content-Type", "application/json");
    }

    private String getAuthorization(HttpExchange exchange) {
        return exchange.getRequestHeaders().getFirst("Authorization");
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop();
            logger.close();
        }));
    }
}


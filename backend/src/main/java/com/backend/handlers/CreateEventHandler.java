package com.backend.handlers;

import com.sun.net.httpserver.HttpExchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.net.Socket;
import java.util.Random;

public class CreateEventHandler extends BaseHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public CreateEventHandler(String HOST, int PORT) {
        super(HOST, PORT);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            handleOptionsRequest(exchange);
        } else if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            handlePostRequest(exchange);
        } else {
            System.out.println("Received non-POST request: " + exchange.getRequestMethod());
            exchange.sendResponseHeaders(405, -1);
        }
    }

    private void handleOptionsRequest(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:3009");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        int statusCode = 204;
        exchange.sendResponseHeaders(statusCode, -1);
        super.logRequest(exchange, statusCode);
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }
        System.out.println(requestBody.toString());
    
        RequestObject requestObject = objectMapper.readValue(requestBody.toString(), RequestObject.class);
        String accessToken = requestObject.getAccessToken();
        System.out.println(accessToken);
        if (!isAuthorized(accessToken)) {
            exchange.sendResponseHeaders(401, -1);
            return;
        }
        String email = super.getAddress(accessToken).get();
        System.out.println("email: " + email);
        
        Random random = new Random();
        int eventID = random.nextInt();
        String query = "upsert event (eventID,title,startedAt,endedAt,description,owner) (" + 
            eventID + "," +
            requestObject.getTitle() + "," + 
            requestObject.getStartedAt() + "," + 
            requestObject.getEndedAt() + "," + 
            requestObject.getDescription() + "," + 
            email + ")";
        String response = communicateWithDB(query);

        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:3009");
        int statusCode = 200;
        exchange.sendResponseHeaders(statusCode, -1);

        super.logRequest(exchange, statusCode);
    }

    public static class RequestObject {
        @JsonProperty("accessToken")
        private String accessToken;
    
        @JsonProperty("description")
        private String description;
    
        @JsonProperty("endedAt")
        private String endedAt;
    
        @JsonProperty("startedAt")
        private String startedAt;
    
        @JsonProperty("title")
        private String title;
    
        public RequestObject() {}
    
        public RequestObject(String accessToken, String description, String endedAt, String startedAt, String title) {
            this.accessToken = accessToken;
            this.description = description;
            this.endedAt = endedAt;
            this.startedAt = startedAt;
            this.title = title;
        }
    
        public String getAccessToken() {
            return accessToken;
        }
    
        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
    
        public String getDescription() {
            return description;
        }
    
        public void setDescription(String description) {
            this.description = description;
        }
    
        public String getEndedAt() {
            return endedAt;
        }
    
        public void setEndedAt(String endedAt) {
            this.endedAt = endedAt;
        }
    
        public String getStartedAt() {
            return startedAt;
        }
    
        public void setStartedAt(String startedAt) {
            this.startedAt = startedAt;
        }
    
        public String getTitle() {
            return title;
        }
    
        public void setTitle(String title) {
            this.title = title;
        }
    
        @Override
        public String toString() {
            return "RequestObject{" +
                    "accessToken='" + accessToken + '\'' +
                    ", description='" + description + '\'' +
                    ", endedAt='" + endedAt + '\'' +
                    ", startedAt='" + startedAt + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }
}

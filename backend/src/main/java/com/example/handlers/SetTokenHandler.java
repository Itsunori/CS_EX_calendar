package com.example.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class SetTokenHandler extends BaseHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();

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
    
        RequestObject requestObject = objectMapper.readValue(requestBody.toString(), RequestObject.class);

        // NOTE: DB logic would go here

        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:3009");
        int statusCode = 200;
        exchange.sendResponseHeaders(statusCode, -1);

        super.logRequest(exchange, statusCode);
    }

    public static class RequestObject {
        private String accessToken;

        public RequestObject() {}

        public RequestObject(String accessToken) {
            this.accessToken = accessToken;
        }
        
        public String getAccessToken() {
            return accessToken;
        }
    
        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        @Override
        public String toString() {
            return "RequestObject{accessToken='" + accessToken + "'}";
        }
    }
}

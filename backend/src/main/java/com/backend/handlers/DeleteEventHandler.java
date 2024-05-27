package com.backend.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.backend.utils.JsonParser;

public class DeleteEventHandler extends BaseHandler {

    public DeleteEventHandler(String HOST, int PORT) {
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
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:3009");
        exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");

        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }
        try {
            RequestObject requestObject = parseRequestObject(requestBody.toString());
            String accessToken = requestObject.getAccessToken();
            if (!isAuthorized(accessToken)) {
                exchange.sendResponseHeaders(401, -1);
                return;
            }

            super.getAddress(accessToken).get();

            String query = "delete event (eventID) (" + requestObject.getEventID() + ")";
            communicateWithDB(query);
            
            exchange.sendResponseHeaders(200, -1);
            super.logRequest(exchange, 200);
        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(400, -1);
            super.logRequest(exchange, 400);
        }
    }

    private RequestObject parseRequestObject(String requestBody) {
        Map<String, Object>  jsonObject = JsonParser.parseJson(requestBody);
        return new RequestObject(
                (String) jsonObject.get("accessToken"),
                (String) jsonObject.get("eventID")
        );
    }

    public static class RequestObject {
        private String accessToken;
        private String eventID;

        public RequestObject() {}

        public RequestObject(String accessToken, String eventID) {
            this.accessToken = accessToken;
            this.eventID = eventID;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getEventID() {
            return eventID;
        }

        public void setEventID(String eventID) {
            this.eventID = eventID;
        }

        @Override
        public String toString() {
            return "RequestObject{" +
                    "accessToken='" + accessToken + '\'' +
                    ", eventID='" + eventID + '\'' +
                    '}';
        }
    }
}

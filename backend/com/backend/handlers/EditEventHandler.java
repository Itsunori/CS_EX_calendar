package com.backend.handlers;

import com.backend.utils.JsonParser;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;


public class EditEventHandler extends BaseHandler {

    public EditEventHandler(String HOST, int PORT) {
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
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        int statusCode = 204;
        exchange.sendResponseHeaders(statusCode, -1);
        super.logRequest(exchange, statusCode);
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
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

            String email = super.getAddress(accessToken).get();

            String query = "delete event (eventID) (" + requestObject.getEventID() + ")";
            communicateWithDB(query);
            
            query = "upsert event (eventID,title,startedAt,endedAt,description,owner) (" + 
                requestObject.getEventID() + "," +
                requestObject.getTitle() + "," + 
                requestObject.getStartedAt() + "," + 
                requestObject.getEndedAt() + "," + 
                requestObject.getDescription() + "," + 
                email + ")";
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
                (String)jsonObject.get("accessToken"),
                (String)jsonObject.get("description"),
                (String)jsonObject.get("eventID"),
                (String)jsonObject.get("endedAt"),
                (String)jsonObject.get("startedAt"),
                (String)jsonObject.get("title")
        );
    }

    public static class RequestObject {
        private String accessToken;
        private String description;
        private String eventID;
        private String endedAt;
        private String startedAt;
        private String title;

        public RequestObject() {}

        public RequestObject(String accessToken, String description, String eventID, String endedAt, String startedAt, String title) {
            this.accessToken = accessToken;
            this.description = description;
            this.eventID = eventID;
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

        public String getEventID() {
            return eventID;
        }

        public void setEventID(String eventID) {
            this.eventID = eventID;
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
                    ", eventID='" + eventID + '\'' +
                    ", endedAt='" + endedAt + '\'' +
                    ", startedAt='" + startedAt + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }
}

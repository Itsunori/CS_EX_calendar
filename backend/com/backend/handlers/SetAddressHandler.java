package com.backend.handlers;

import com.backend.utils.JsonParser;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public class SetAddressHandler extends BaseHandler {

    public SetAddressHandler(String HOST, int PORT) {
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

        // check if the access token is valid and possible to get the email
        RequestObject requestObject = parseRequestObject(requestBody.toString());
        String accessToken = requestObject.getAccessToken();
        Optional<String> email = super.getAddress(accessToken);
        if (!email.isPresent()) {
            exchange.sendResponseHeaders(401, -1);
            return;
        }

        Boolean existUser = isUser(email.get());
        if (!existUser) {
            String query = "upsert user (mailAddress) (" + email.get() + ")";
            communicateWithDB(query);
        }

        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:3009");
        int statusCode = 200;
        exchange.sendResponseHeaders(statusCode, -1);

        super.logRequest(exchange, statusCode);
    }

    private RequestObject parseRequestObject(String requestBody) {
        Map<String, Object>  jsonObject = JsonParser.parseJson(requestBody);
        return new RequestObject((String)jsonObject.get("accessToken"));
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

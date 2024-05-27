package com.backend.handlers;

import com.backend.utils.Event;
import com.backend.utils.StringToJsonConverter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

public class EventListHandler extends BaseHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public EventListHandler(String HOST, int PORT) {
        super(HOST, PORT);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            handleOptionsRequest(exchange);
        } else if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            handleGetRequest(exchange);
        } else {
            System.out.println("Received non-Get request: " + exchange.getRequestMethod());
            exchange.sendResponseHeaders(405, -1);
        }
    }

    private void handleOptionsRequest(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:3009");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", " GET, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        int statusCode = 204;
        exchange.sendResponseHeaders(statusCode, -1);
        super.logRequest(exchange, statusCode);
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String query = requestURI.getQuery();
    
        Map<String, String> queryParams = parseQuery(query);
        String accessToken = queryParams.get("accessToken");
    
        if (!isAuthorized(accessToken)) {
            exchange.sendResponseHeaders(401, -1);
            return;
        }
    
        query = "from event select (eventID,title,startedAt,endedAt,description,owner)";
        String response = communicateWithDB(query);

        List<Event> events = StringToJsonConverter.parseStringToEventList(response);
        String responseBody = StringToJsonConverter.convertToJson(events);

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:3009");
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(200, responseBody.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(responseBody.getBytes());
        os.close();

        super.logRequest(exchange, 200);
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

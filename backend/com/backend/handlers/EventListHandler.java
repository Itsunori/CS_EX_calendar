package com.backend.handlers;

import com.backend.utils.Event;
import com.backend.utils.StringToJsonConverter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EventListHandler extends BaseHandler {
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
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
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

        int year, month, day;
        if(queryParams.get("year")!=null && queryParams.get("month") != null && queryParams.get("day") != null){
            year = Integer.parseInt(queryParams.get("year"));
            month = Integer.parseInt(queryParams.get("month"));
            day = Integer.parseInt(queryParams.get("day"));
        } else {
            LocalDate today = LocalDate.now();
            year = today.getYear();
            month = today.getMonthValue();
            day = today.getDayOfMonth();
        }
        LocalDate targetDay = LocalDate.of(year, month, day);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        Optional<String> mail = super.getAddress(accessToken);
        query = String.format("from event where (owner, =, %s) where (startedAt, >=, %s) where (endedAt, <=, %s) select (eventID,title,startedAt,endedAt,description,owner)",mail.get(),targetDay.format(formatter)+" 00:00:00", targetDay.format(formatter) + " 23:59:59");

        String response = communicateWithDB(query);
        System.out.println(response);
        String responseBody = "[]";
        if(response.length()>10){
            List<Event> events = StringToJsonConverter.parseStringToEventList(response);
            responseBody = StringToJsonConverter.convertToJson(events);
        }

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
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

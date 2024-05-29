package handler;

import com.sun.net.httpserver.HttpHandler;

import src.Jakoten;
import utils.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

public class ScheduleHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String token = getCookie(exchange);
        String template = Files.readString( Paths.get("pages/schedule.html.jkt").toAbsolutePath().normalize(), StandardCharsets.UTF_8);
        System.out.println(token);
        URI requestUri = exchange.getRequestURI();
        Map<String, String> queryParams = parseQueryParams(requestUri.getRawQuery());        
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
        LocalDate previousLocalDate = targetDay.minusDays(1);
        LocalDate nextLocalDate = targetDay.plusDays(1);

        List<Map<String, Object>> events = new ArrayList<>();
        Map<String, Object> context = new HashMap<>();
        
        String baseUrl = "http://localhost:8000/event-list/";

        String query = String.format("accessToken=%s&year=%d&month=%d&day=%d",
                URLEncoder.encode(token, StandardCharsets.UTF_8.toString()),
                year, month, day);
        @SuppressWarnings("deprecation")
        URL url = new URL(baseUrl + "?" + query);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int responseCode = connection.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { 
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            if (response.toString().length()> 10){
                events = JsonParser.parseJsonArray(response.toString());
            }
        } 
        connection.disconnect();

        context.put("events", events);
        context.put("year", year );
        context.put("month", month );
        context.put("day", day );

        context.put("previousYear", previousLocalDate.getYear());
        context.put("previousMonth", previousLocalDate.getMonthValue());
        context.put("previousDay", previousLocalDate.getDayOfMonth());

        context.put("nextYear", nextLocalDate.getYear());
        context.put("nextMonth", nextLocalDate.getMonthValue());
        context.put("nextDay", nextLocalDate.getDayOfMonth());


        Jakoten engine = new Jakoten();
        String result = engine.render(template, context);
        handleResponse(exchange, result);
    }

    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> queryParams = new HashMap<>();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=", 2);
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                } else if (keyValue.length == 1) {
                    queryParams.put(keyValue[0], "");
                }
            }
        }
        return queryParams;
    }

    private String getCookie(HttpExchange exchange ){
        Map<String, List<String>> headers = exchange.getRequestHeaders();
        List<String> cookies = headers.get("Cookie");
        String token = "";
        if (cookies != null) {
            Map<String, String> cookieMap = new HashMap<>();
            for (String cookie : cookies) {
                String[] cookiePairs = cookie.split(";\\s*");
                for (String cookiePair : cookiePairs) {
                    String[] keyValue = cookiePair.split("=", 2);
                    if (keyValue.length == 2) {
                        cookieMap.put(keyValue[0], keyValue[1]);
                    }
                }
            }
            token = cookieMap.get("access_token");
        }
        return token;
    }

    private static void handleResponse(HttpExchange exchange, String response) throws IOException {
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}

package handler;

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
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import src.Jakoten;
import utils.JsonParser;

public class DetailHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String token = getCookie(exchange);

        String template = Files.readString( Paths.get("pages/detail.html.jkt").toAbsolutePath().normalize(), StandardCharsets.UTF_8);
        
        URI requestUri = exchange.getRequestURI();
        Map<String, String> queryParams = parseQueryParams(requestUri.getRawQuery());        
        Map<String, Object> context = new HashMap<>();

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

        if (queryParams.get("id") != null){
            String baseUrl = "http://localhost:8000/event-detail/";
            String eventID = queryParams.get("id");
            String query = String.format("accessToken=%s&eventID=%s",
                    URLEncoder.encode(token, StandardCharsets.UTF_8.toString()),
                    eventID);
            System.out.println(query);
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
                    context.putAll(JsonParser.parseJsonArray(response.toString()).get(0));
                }
            }
            connection.disconnect();
            context.put("isEdit", true);
        } else {
            context.put("title", "入力してください");
            context.put("description", "入力してください");

            LocalDate targetDay = LocalDate.of(year, month, day);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            context.put("startedAt", targetDay.format(formatter)+"T00:00");
            context.put("endedAt", targetDay.format(formatter)+"T01:00");
            context.put("isCreate", true);
        }

        context.put("year", year );
        context.put("month", month );
        context.put("day", day );

        Jakoten engine = new Jakoten();
        String result = engine.render(template, context);
        handleResponse(exchange, result);
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

    private static void handleResponse(HttpExchange exchange, String response) throws IOException {
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}

package handler;

import com.sun.net.httpserver.HttpHandler;

import src.Jakoten;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

public class ScheduleHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String template = Files.readString( Paths.get("pages/schedule.html.jkt").toAbsolutePath().normalize(), StandardCharsets.UTF_8);
        URI requestUri = exchange.getRequestURI();
        Map<String, String> queryParams = parseQueryParams(requestUri.getRawQuery());        
        int year = 2024;
        int month = 5;
        int day = 15; 

        String scheduleTitle = year + "年" + month + "月" + day + "日のスケジュール";
        String event = "会議";

        Map<String, Object> context = new HashMap<>();
        context.put("year", year);
        context.put("month", month);
        context.put("day", day);
        context.put("scheduleTitle", scheduleTitle);
        context.put("event", event);

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

    private static void handleResponse(HttpExchange exchange, String response) throws IOException {
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}

package handler;

import com.sun.net.httpserver.HttpHandler;

import src.Jakoten;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
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

        String template = Files.readString( Paths.get("pages/schedule.html.jkt").toAbsolutePath().normalize(), StandardCharsets.UTF_8);
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

        Map<String, Object> event1 = new HashMap<>();
        event1.put("id", "1");
        event1.put("title", "Meeting with Team");
        event1.put("description", "Discuss project updates");
        event1.put("date", "2024-06-01");
        event1.put("time", "10:00 AM");
        events.add(event1);

        Map<String, Object> event2 = new HashMap<>();
        event2.put("id", "2");
        event2.put("title", "Doctor's Appointment");
        event2.put("description", "Routine check-up");
        event2.put("date", "2024-06-02");
        event2.put("time", "02:00 PM");
        events.add(event2);

        Map<String, Object> event3 = new HashMap<>();
        event3.put("id", "3");
        event3.put("title", "Lunch with Client");
        event3.put("description", "Discuss contract details");
        event3.put("date", "2024-06-03");
        event3.put("time", "12:00 PM");
        events.add(event3);

        // Mapにデータを詰める
        Map<String, Object> context = new HashMap<>();
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

    private static void handleResponse(HttpExchange exchange, String response) throws IOException {
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}

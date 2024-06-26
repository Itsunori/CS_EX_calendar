package handler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import src.Jakoten;

public class CalendarHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String template = Files.readString( Paths.get("pages/calendar.html.jkt").toAbsolutePath().normalize(), StandardCharsets.UTF_8);
        URI requestUri = exchange.getRequestURI();
        Map<String, String> queryParams = parseQueryParams(requestUri.getRawQuery());        
        int year, month;
        if(queryParams.get("year")!=null && queryParams.get("month") != null){
            year = Integer.parseInt(queryParams.get("year"));
            month = Integer.parseInt(queryParams.get("month"));
        } else {
            LocalDate today = LocalDate.now();
            year = today.getYear();
            month = today.getMonthValue();
        }
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstOfMonth = yearMonth.atDay(1);
        DayOfWeek firstDayOfWeek = firstOfMonth.getDayOfWeek();
        int daysInMonth = yearMonth.lengthOfMonth();

        int firstDayOfMonth = firstDayOfWeek.getValue() % 7;

        List<Integer> daysInMonthList = IntStream.rangeClosed(1, daysInMonth).boxed().collect(Collectors.toList());

        YearMonth previousMonthYearMonth = yearMonth.minusMonths(1);
        YearMonth nextMonthYearMonth = yearMonth.plusMonths(1);

        int previousYear = previousMonthYearMonth.getYear();
        int previousMonth = previousMonthYearMonth.getMonthValue();

        int nextYear = nextMonthYearMonth.getYear();
        int nextMonth = nextMonthYearMonth.getMonthValue();

        Map<String, Object> context = new HashMap<>();
        context.put("year", year);
        context.put("month", month);
        context.put("first_day_of_month", firstDayOfMonth);
        context.put("days_in_month", daysInMonthList);
        context.put("previousYear", previousYear);
        context.put("previousMonth", previousMonth);
        context.put("nextYear", nextYear);
        context.put("nextMonth", nextMonth);


        Jakoten engine = new Jakoten();
        String result = engine.render(template, context);
        handleResponse(exchange, result);
    }

    private static void handleResponse(HttpExchange exchange, String response) throws IOException {
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
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
}

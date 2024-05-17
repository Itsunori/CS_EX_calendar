import com.sun.net.httpserver.HttpServer;

import handler.LoginHandler;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Index {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(3009), 0);
        
        server.createContext("/login/", new LoginHandler());
        server.createContext("/signup/", new SignupHandler());
        server.createContext("/calendar/", new CalendarHandler());
        server.createContext("/schedule/", new ScheduleHandler());
        
        server.setExecutor(null);
        server.start();
        System.out.println("Server is listening on port 3009");
    }

    static class SignupHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "This is the signup page";
            handleResponse(exchange, response);
        }
    }

    static class CalendarHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "This is the calendar page";
            handleResponse(exchange, response);
        }
    }

    static class ScheduleHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "This is the schedule page";
            handleResponse(exchange, response);
        }
    }

    private static void handleResponse(HttpExchange exchange, String response) throws IOException {
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
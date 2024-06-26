import com.sun.net.httpserver.HttpServer;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import handler.*;

public class Index {
    public static void main(String[] args) throws IOException {
        int port = 3009; 
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid Port");
            }
        }
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", new LoginHandler());
        server.createContext("/login", new LoginHandler());
        server.createContext("/signup", new SignupHandler());
        server.createContext("/calendar", new CalendarHandler());
        server.createContext("/callback", new CallbackHandler());
        server.createContext("/schedule", new ScheduleHandler());
        server.createContext("/detail", new DetailHandler());
        server.createContext("/js/", new StaticFileHandler("js/"));

        server.setExecutor(null);
        server.start();
        System.out.println("Server is listening on port "+port);
    }

    static class SignupHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "This is the signup page";
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

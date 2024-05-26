package handler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import src.Jakoten;

public class DetailHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String template = Files.readString( Paths.get("pages/detail.html.jkt").toAbsolutePath().normalize(), StandardCharsets.UTF_8);
        
        Map<String, Object> context = new HashMap<>();
        context.put("test", "hello world");

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
}

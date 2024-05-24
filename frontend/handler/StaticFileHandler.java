package handler;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StaticFileHandler implements HttpHandler {
    private final String basePath;

    public StaticFileHandler(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String filePath = basePath + exchange.getRequestURI().getPath().substring(basePath.length());
        byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
        exchange.sendResponseHeaders(200, fileBytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(fileBytes);
        os.close();
    }
}

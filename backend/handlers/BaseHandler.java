package handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.util.Date;
import java.io.*;

public class BaseHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException{
    }

    protected void logRequest(HttpExchange exchange, int statusCode) {
        String clientAddress = exchange.getRemoteAddress().getAddress().getHostAddress();
        String requestMethod = exchange.getRequestMethod();
        String requestURI = exchange.getRequestURI().toString();
        Date requestTime = new Date();

        System.out.println("[" + requestTime + "] " +
                            "Request from " + clientAddress + " " +
                            requestMethod + " " +
                            requestURI + " " +
                            "Status: " + statusCode);
    }
}

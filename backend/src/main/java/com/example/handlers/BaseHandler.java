package com.example.handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.util.Date;
import java.io.*;
import java.net.HttpURLConnection;
import java.util.Optional;
import java.net.URL;

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

    protected Optional<String> getAddress(String accessToken){
        try {
            String url = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = con.getResponseCode();
            System.out.println("Response Code : " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println("Response: " + response.toString());

                String jsonResponse = response.toString();
                String email = jsonResponse.split("\"email\":\"")[1].split("\"")[0];
                System.out.println("User email: " + email);
                return Optional.of(email);
            } else {
                System.out.println("GET request not worked");
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}

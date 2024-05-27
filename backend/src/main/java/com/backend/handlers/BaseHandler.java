package com.backend.handlers;

import com.sun.net.httpserver.HttpHandler;

import com.sun.net.httpserver.HttpExchange;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.util.Optional;
import java.util.stream.Collectors;
import java.net.URL;

public class BaseHandler implements HttpHandler {
    private final String HOST;
    private final int PORT;

    public BaseHandler(String HOST, int PORT) {
        this.HOST = HOST;
        this.PORT = PORT;
    }

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

    protected Optional<String> getAddress(String accessToken) {
        try {
            String url = "https://www.googleapis.com/oauth2/v3/userinfo";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String jsonResponse = response.toString();
                String email = jsonResponse.split("\"email\": \"")[1].split("\"")[0];
                return Optional.of(email);
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.empty();
    }

    protected Boolean isUser(String email) throws IOException {
        String query = "from user select (mailAddress)";
        String response = communicateWithDB(query);
        logDBRequest(query, response);
        List<String> emailList = splitComma(response);
        for (String element : emailList) {
            if (element.equals(email)) {
                return true;
            }
        }
        return false;
    }

    private List<String> splitComma(String strings){
        String[] emailList = strings.split(",");
        List<String> resultList = new ArrayList<>();

        for (String element : emailList) {
            element = element.trim();
            if (!element.isEmpty()) {
                resultList.add(element);
            }
        }

        return resultList;
    }

    protected void logDBRequest(String command, String response) {
        Date requestTime = new Date();
        System.out.println("[" + requestTime + "] Command: " + command + " Response: " + response);
    }

    protected String communicateWithDB(String query) throws IOException {
        try (Socket dbSocket = new Socket(HOST, PORT);
            PrintWriter out = new PrintWriter(dbSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(dbSocket.getInputStream()))) {

            out.println(query);

            StringBuilder response = new StringBuilder();
            String dbLine;
            while ((dbLine = in.readLine()) != null) {
                response.append(dbLine);
            }
            logDBRequest(query, response.toString());

            return response.toString();
        }
    }

    protected boolean isAuthorized(String accessToken) {
        Optional<String> email = getAddress(accessToken);
        if (email.isEmpty()) {
            return false;
        }
        try {
            return isUser(email.get());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected Map<String, String> parseQuery(String query) {
        return List.of(query.split("&")).stream()
            .map(param -> param.split("="))
            .collect(Collectors.toMap(
                pair -> decode(pair[0]), 
                pair -> pair.length > 1 ? decode(pair[1]) : ""
            ));
    }

    private String decode(String value) {
        try {
            return java.net.URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }
}

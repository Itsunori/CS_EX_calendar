package com.backend;

import com.sun.net.httpserver.HttpServer;
import com.backend.handlers.EventListHandler;
import com.backend.handlers.SetAddressHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;

public class Backend {
    private static final String HOST = "localhost";
    private static final int PORT = 3366;
    public static void main(String[] args) throws IOException {
        String filePath = "./src/main/java/com/db/init.jjj";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            while ((line = reader.readLine()) != null) {
                String response;
                do {
                    response = communicateWithDB(line);
                } while (response.isEmpty());

                logDBRequest(line, response);
                // NOTE: error handling if needed
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Initialization complete");
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/set-address/", new SetAddressHandler(HOST, PORT));
        server.createContext("/event-list/", new EventListHandler(HOST, PORT));
        server.setExecutor(null); 
        server.start();
        System.out.println("Server started on port 8000");
    }

    private static String communicateWithDB(String query) throws IOException {
        try (Socket dbSocket = new Socket(HOST, PORT);
            PrintWriter out = new PrintWriter(dbSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(dbSocket.getInputStream()))) {

            out.println(query);

            StringBuilder response = new StringBuilder();
            String dbLine;
            while ((dbLine = in.readLine()) != null) {
                response.append(dbLine);
            }

            return response.toString();
        }
    }

    private static void logDBRequest(String command, String response) {
        Date requestTime = new Date();
        System.out.println("[" + requestTime + "] Command: " + command + " Response: " + response);
    }
}

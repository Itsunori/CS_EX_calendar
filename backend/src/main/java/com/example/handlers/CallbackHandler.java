// package handlers;

// import com.sun.net.httpserver.HttpHandler;
// import com.sun.net.httpserver.HttpExchange;
// import backend.Cache;

// import java.io.*;
// import java.net.HttpURLConnection;
// import java.net.URL;
// import java.nio.charset.StandardCharsets;
// import java.util.concurrent.TimeUnit;

// public class CallbackHandler implements HttpHandler {
//     private static final Cache<String, String> cache = new Cache<>(60, TimeUnit.SECONDS);

//     @Override
//     public void handle(HttpExchange exchange) throws IOException {
//         String query = exchange.getRequestURI().getQuery();
//         String code = query.split("=")[1];

//         String clientId = "YOUR_GOOGLE_CLIENT_ID";
//         String clientSecret = "YOUR_GOOGLE_CLIENT_SECRET";
//         String redirectUri = "http://localhost:8000/callback";
//         String tokenUrl = "https://oauth2.googleapis.com/token";

//         URL url = new URL(tokenUrl);
//         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//         conn.setRequestMethod("POST");
//         conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//         conn.setDoOutput(true);

//         String params = "code=" + code +
//                 "&client_id=" + clientId +
//                 "&client_secret=" + clientSecret +
//                 "&redirect_uri=" + redirectUri +
//                 "&grant_type=authorization_code";

//         try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8)) {
//             writer.write(params);
//             writer.flush();
//         }

//         StringBuilder response = new StringBuilder();
//         try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
//             String line;
//             while ((line = reader.readLine()) != null) {
//                 response.append(line);
//             }
//         }

//         JSONObject json = new JSONObject(response.toString());
//         String accessToken = json.getString("access_token");
//         String refreshToken = json.has("refresh_token") ? json.getString("refresh_token") : null;

//         cache.put("accessToken", accessToken, 10, TimeUnit.MINUTES);
//         if (refreshToken != null) {
//             cache.put("refreshToken", refreshToken, 60, TimeUnit.MINUTES);
//         }

//         String userInfoUrl = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken;
//         URL userInfoEndpoint = new URL(userInfoUrl);
//         HttpURLConnection userInfoConn = (HttpURLConnection) userInfoEndpoint.openConnection();
//         userInfoConn.setRequestMethod("GET");

//         StringBuilder userInfoResponse = new StringBuilder();
//         try (BufferedReader reader = new BufferedReader(new InputStreamReader(userInfoConn.getInputStream(), StandardCharsets.UTF_8))) {
//             String line;
//             while ((line = reader.readLine()) != null) {
//                 userInfoResponse.append(line);
//             }
//         }

//         String userInfo = userInfoResponse.toString();
//         System.out.println("User Info: " + userInfo);

//         // Cache the user info
//         cache.put("userInfo", userInfo, 10, TimeUnit.MINUTES);

//         // Render the user info on the webpage
//         exchange.sendResponseHeaders(200, userInfo.getBytes().length);
//         OutputStream os = exchange.getResponseBody();
//         os.write(userInfo.getBytes());
//         os.close();
//     }
// }
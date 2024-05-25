import com.sun.net.httpserver.HttpServer;
import handlers.SetTokenHandler;

import java.io.IOException;
import java.net.InetSocketAddress;


public class Backend {
        public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/set-token/", new SetTokenHandler());
        server.setExecutor(null); 
        server.start();
        System.out.println("Server started on port 8000");
    }
}

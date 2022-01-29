package handler;

import com.sun.net.httpserver.*;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;

public class FileHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        try (OutputStream respBody = exchange.getResponseBody()) {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                String  urlPath = exchange.getRequestURI().toString();
                if (urlPath == null || urlPath.equals("/")) {
                    urlPath = "/index.html";
                }
                String filePath = "web" + urlPath;
                File file = new File(filePath);
                int httpCode = HttpURLConnection.HTTP_OK;
                if (!file.exists()) {
                    httpCode = HttpURLConnection.HTTP_NOT_FOUND;
                    filePath = "web/HTML/404.html";
                    file = new File(filePath);
                }
                exchange.sendResponseHeaders(httpCode, file.length());
                Files.copy(file.toPath(),respBody);
            }
            else {
                throw new IOException("the HttpExchange request method was not a get");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
}

package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import result.ClearResult;
import service.ClearService;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;

public class ClearHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                Gson gson = new Gson();

                ClearService clearService = new ClearService();
                ClearResult clearResult = clearService.clear(true);
                String  loginResponseJson = gson.toJson(clearResult);

                Writer writer = new OutputStreamWriter(exchange.getResponseBody());
                if (clearResult.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, loginResponseJson.length());
                }
                else { //failure to clear
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
                }
                writer.write(loginResponseJson);
                writer.close();
            }
            else {
                throw new IOException("the HttpExchange request method was not a post");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
            exchange.getResponseBody().close();
        }
    }
}

package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.LoadRequest;
import result.LoadResult;
import service.LoadService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.util.Scanner;

public class LoadHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                InputStream reqBody = exchange.getRequestBody();
                Gson gson = new Gson();
                String reqData = readString(reqBody);
                System.out.println(reqData);
                LoadRequest loadRequest = gson.fromJson(reqData, LoadRequest.class);

                LoadService loadService = new LoadService(loadRequest);
                LoadResult loadResult = loadService.load(true);
                String loadResponseJson = gson.toJson(loadResult);

                Writer writer = new OutputStreamWriter(exchange.getResponseBody());
                if (loadResult.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, loadResponseJson.length());
                } else { //failure to log in properly
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
                writer.write(loadResponseJson);
                writer.close();
            } else {
                throw new IOException("the HttpExchange request method was not a post");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
            exchange.getResponseBody().close();
        }
    }

    private String readString(InputStream reqBody) {
        Scanner scanner = new Scanner(reqBody);
        StringBuilder reqBodyBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            reqBodyBuilder.append(scanner.nextLine());
        }
        scanner.close();

        return reqBodyBuilder.toString();
    }
}

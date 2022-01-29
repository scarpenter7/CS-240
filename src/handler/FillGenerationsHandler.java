package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.FillGenerationsRequest;
import result.FillGenerationsResult;
import service.FillGenerationsService;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;

public class FillGenerationsHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                Gson gson = new Gson();
                URI fillURI = exchange.getRequestURI();
                String path = fillURI.getPath();
                String[] pathParts = path.split("/");
                String userName = pathParts[2];
                int numGenerations = 4;
                if (pathParts.length > 3) {
                    numGenerations = checkValidGenerationsParameter(pathParts, exchange, gson);
                    //checkValidGenerationsParameter returns -1 upon invalid generations parameter
                    if (numGenerations == -1) {
                        return;
                    }
                }
                FillGenerationsRequest fillGenerationsRequest = new FillGenerationsRequest(userName, numGenerations);
                FillGenerationsService fillGenerationsService = new FillGenerationsService(fillGenerationsRequest);
                FillGenerationsResult fillGenerationsResult =
                        fillGenerationsService.fillGenerations(true);

                String fillGenerationsResponseJson = gson.toJson(fillGenerationsResult);

                Writer writer = new OutputStreamWriter(exchange.getResponseBody());
                if (fillGenerationsResult.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, fillGenerationsResponseJson.length());
                } else { //failure
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
                writer.write(fillGenerationsResponseJson);
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

    private int checkValidGenerationsParameter
            ( String[] pathParts, HttpExchange exchange, Gson gson) {
        int numGenerations;
        try {
            numGenerations = Integer.parseInt(pathParts[3]);
            if (numGenerations < 0) {
                return -1;
            }
        }
        catch (NumberFormatException e) {
            handleInvalidParameters(exchange, gson);
            return -1;
        }
        return numGenerations;
    }

    private void handleInvalidParameters(HttpExchange exchange, Gson gson) {
        FillGenerationsResult fillGenerationsResult = new FillGenerationsResult
                (false, "error: invalid generations parameter");
        String fillGenerationsResponseJson = gson.toJson(fillGenerationsResult);
        Writer writer = new OutputStreamWriter(exchange.getResponseBody());
        try {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            writer.write(fillGenerationsResponseJson);
            writer.close();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}

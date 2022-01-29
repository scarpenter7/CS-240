package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.EventRequest;
import result.EventsResult;
import service.EventService;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;

public class EventHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        final String AUTHORIZATION = "Authorization";
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                Gson gson = new Gson();
                URI uri = exchange.getRequestURI();
                String path = uri.getPath();
                String[] pathParts = path.split("/");
                Headers headers = exchange.getRequestHeaders();
                List<String> authorization = headers.get(AUTHORIZATION);
                String authTokenName = authorization.get(0);

                EventRequest eventRequest;
                if (pathParts.length > 2) {
                    String eventID = pathParts[2];
                    eventRequest = new EventRequest(eventID, authTokenName);
                }
                else {
                    eventRequest = new EventRequest(authTokenName);
                }
                EventService eventService = new EventService(eventRequest);
                EventsResult eventsResult = eventService.event();

                String eventResponseJson = gson.toJson(eventsResult);

                Writer writer = new OutputStreamWriter(exchange.getResponseBody());
                if (eventsResult.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, eventResponseJson.getBytes().length);
                } else { //failure
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, eventResponseJson.getBytes().length);
                }
                writer.write(eventResponseJson);
                writer.close();
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

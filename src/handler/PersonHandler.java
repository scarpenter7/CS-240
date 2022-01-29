package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.PersonRequest;
import result.PersonsResult;
import service.PersonService;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;

public class PersonHandler implements HttpHandler {
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

                PersonRequest personRequest;
                if (pathParts.length > 2) {
                    String personID = pathParts[2];
                    personRequest = new PersonRequest(personID, authTokenName);
                }
                else {
                    personRequest = new PersonRequest(authTokenName);
                }
                PersonService personService = new PersonService(personRequest);
                PersonsResult personsResult = personService.person();

                String personResponseJson = gson.toJson(personsResult);

                Writer writer = new OutputStreamWriter(exchange.getResponseBody());
                if (personsResult.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, personResponseJson.length());
                } else { //failure
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
                writer.write(personResponseJson);
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

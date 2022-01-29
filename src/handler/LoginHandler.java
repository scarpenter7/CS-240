package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.LoginRequest;
import result.LoginResult;
import service.LoginService;
import com.google.gson.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.Scanner;

public class LoginHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                InputStream reqBody = exchange.getRequestBody();
                Gson gson = new Gson();
                String reqData = readString(reqBody);
                System.out.println(reqData);
                LoginRequest loginRequest = gson.fromJson(reqData, LoginRequest.class);

                LoginService loginService = new LoginService(loginRequest);
                LoginResult loginResult = loginService.login(true);
                String  loginResponseJson = gson.toJson(loginResult);

                Writer writer = new OutputStreamWriter(exchange.getResponseBody());
                if (loginResult.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, loginResponseJson.length());
                }
                else { //failure to log in properly
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
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

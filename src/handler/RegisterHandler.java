package handler;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;
import request.RegisterRequest;
import result.RegisterResult;
import service.RegisterService;


public class RegisterHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                InputStream reqBody = exchange.getRequestBody();
                Gson gson = new Gson();
                String reqData = readString(reqBody);
                System.out.println(reqData);
                RegisterRequest registerRequest = gson.fromJson(reqData, RegisterRequest.class);

                RegisterService registerService = new RegisterService(registerRequest);
                RegisterResult registerResult = registerService.register(true);
                String  registerResponseJson = gson.toJson(registerResult);

                Writer writer = new OutputStreamWriter(exchange.getResponseBody());
                if (registerResult.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, registerResponseJson.length());
                }
                else { //failure to log in properly
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
                writer.write(registerResponseJson);
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

package service;

import com.google.gson.Gson;
import dao.DataAccessException;
import dao.Database;
import model.AuthToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.FillGenerationsRequest;
import request.LoadRequest;
import request.RegisterRequest;
import result.FillGenerationsResult;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

class FillGenerationsServiceTest {

    @BeforeEach
    public void loadInitialData() {
        try {
            Reader reader = new FileReader("json/tests.json");
            Gson gson = new Gson();
            LoadRequest request = gson.fromJson(reader, LoadRequest.class);
            reader.close();
            LoadService service = new LoadService(request);
            service.load(true);

            Database db = new Database();
            AuthToken authToken1 = new AuthToken("DKbBMjXm","sam4");
            AuthToken authToken2 = new AuthToken("hello1","sam1");
            db.getAuthTokenDAO().clear();
            db.getAuthTokenDAO().insert(authToken1);
            db.getAuthTokenDAO().insert(authToken2);
            db.closeConnection(true);
        }
        catch(IOException | DataAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void fillGenerationsPositive() {

        FillGenerationsRequest request0 = new FillGenerationsRequest("sam3", 0);
        FillGenerationsRequest request1 = new FillGenerationsRequest("sam3", 1);
        FillGenerationsRequest request2 = new FillGenerationsRequest("sam3", 2);
        FillGenerationsRequest request3 = new FillGenerationsRequest("sam3", 3);
        FillGenerationsRequest request4 = new FillGenerationsRequest("sam3", 4);

        FillGenerationsService service0 = new FillGenerationsService(request0);
        FillGenerationsService service1 = new FillGenerationsService(request1);
        FillGenerationsService service2 = new FillGenerationsService(request2);
        FillGenerationsService service3 = new FillGenerationsService(request3);
        FillGenerationsService service4 = new FillGenerationsService(request4);

        FillGenerationsResult response0 = service0.fillGenerations(false);
        FillGenerationsResult response1 = service1.fillGenerations(false);
        FillGenerationsResult response2 = service2.fillGenerations(false);
        FillGenerationsResult response3 = service3.fillGenerations(false);
        FillGenerationsResult response4 = service4.fillGenerations(false);

        Assertions.assertEquals
                ("Successfully added 1 persons and 1 events to the database.", response0.getMessage());
        Assertions.assertEquals
                ("Successfully added 3 persons and 7 events to the database.", response1.getMessage());
        Assertions.assertEquals
                ("Successfully added 7 persons and 19 events to the database.", response2.getMessage());
        Assertions.assertEquals
                ("Successfully added 15 persons and 43 events to the database.", response3.getMessage());
        Assertions.assertEquals
                ("Successfully added 31 persons and 91 events to the database.", response4.getMessage());
    }

    @Test
    void fillGenerationsNegativeBadUserName() {
        FillGenerationsRequest request = new FillGenerationsRequest("sam5", 0);
        FillGenerationsService service = new FillGenerationsService(request);
        FillGenerationsResult response = service.fillGenerations(false);

        Assertions.assertEquals
                ("error: Invalid username", response.getMessage());
    }
}
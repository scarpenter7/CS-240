package service;

import com.google.gson.Gson;
import dao.*;
import model.Event;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoadRequest;
import result.LoadResult;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

class LoadServiceTest {

    private LoadRequest request;

    @BeforeEach
    void loadJsonString() {
        try {
            Reader reader = new FileReader("json/example.json");
            Gson gson = new Gson();
            request = gson.fromJson(reader, LoadRequest.class);
            reader.close();
            ClearService service = new ClearService();
            service.clear(true);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void loadPositive() {
        LoadService service = new LoadService(request);
        LoadResult response = service.load(true);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals("Successfully added 1 users, 3 persons, and 2 events to the database.",
                                response.getMessage());

        try {
            Database db = new Database();
            UserDAO userDAO = db.getUserDAO();
            PersonDAO personDAO = db.getPersonDAO();
            EventDAO eventDAO = db.getEventDAO();

            Assertions.assertNotNull(userDAO.find("sheila"));

            Assertions.assertNotNull(personDAO.find("Sheila_Parker"));
            Assertions.assertNotNull(personDAO.find("Patrick_Spencer"));
            Assertions.assertNotNull(personDAO.find("Im_really_good_at_names"));

            Assertions.assertNotNull(eventDAO.find("Sheila_Family_Map"));
            Assertions.assertNotNull(eventDAO.find("I_hate_formatting"));
            db.closeConnection(false);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    void loadNegative() {
        request.getPersons()[0].setGender("oops"); //invalid gender
        LoadService service = new LoadService(request);
        LoadResult response = service.load(false);

        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals("error: Invalid request data", response.getMessage());
    }
}
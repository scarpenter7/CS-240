package service;

import com.google.gson.Gson;
import dao.*;
import model.AuthToken;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoadRequest;
import result.ClearResult;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ClearServiceTest {

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
    void clearPositive() {
        try {
            Database db = new Database();
            UserDAO userDAO = db.getUserDAO();
            PersonDAO personDAO = db.getPersonDAO();
            AuthTokenDAO authTokenDAO = db.getAuthTokenDAO();
            EventDAO eventDAO = db.getEventDAO();

            assertNotNull(userDAO.find("sam1"));
            assertNotNull(userDAO.find("sam2"));
            assertNotNull(userDAO.find("sam3"));

            assertNotNull(personDAO.find("sam1"));
            assertNotNull(personDAO.find("sam2"));
            assertNotNull(personDAO.find("sam3"));

            assertNotNull(eventDAO.find("hello1"));
            assertNotNull(eventDAO.find("hello2"));
            assertNotNull(eventDAO.find("hello3"));

            assertNotNull(authTokenDAO.find("DKbBMjXm"));
            assertNotNull(authTokenDAO.find("hello1"));
            db.closeConnection(false);

            ClearService service = new ClearService();
            ClearResult response = service.clear(true);
            Assertions.assertEquals("Clear succeeded.", response.getMessage());

            Database db2 = new Database();
            UserDAO userDAO2 = db2.getUserDAO();
            PersonDAO personDAO2 = db2.getPersonDAO();
            AuthTokenDAO authTokenDAO2 = db2.getAuthTokenDAO();
            EventDAO eventDAO2 = db2.getEventDAO();

            assertNull(userDAO2.find("sam1"));
            assertNull(userDAO2.find("sam2"));
            assertNull(userDAO2.find("sam3"));

            assertNull(personDAO2.find("sam1"));
            assertNull(personDAO2.find("sam2"));
            assertNull(personDAO2.find("sam3"));

            assertNull(eventDAO2.find("hello1"));
            assertNull(eventDAO2.find("hello2"));
            assertNull(eventDAO2.find("hello3"));

            assertNull(authTokenDAO2.find("DKbBMjXm"));
            assertNull(authTokenDAO2.find("hello1"));

            db2.closeConnection(true);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    //check to see if the service can be called multiple times consecutively without locking up the Database
    @Test
    void clearTwice() {
        try {
            Database db = new Database();
            UserDAO userDAO = db.getUserDAO();
            PersonDAO personDAO = db.getPersonDAO();
            AuthTokenDAO authTokenDAO = db.getAuthTokenDAO();
            EventDAO eventDAO = db.getEventDAO();

            assertNotNull(userDAO.find("sam1"));
            assertNotNull(userDAO.find("sam2"));
            assertNotNull(userDAO.find("sam3"));

            assertNotNull(personDAO.find("sam1"));
            assertNotNull(personDAO.find("sam2"));
            assertNotNull(personDAO.find("sam3"));

            assertNotNull(eventDAO.find("hello1"));
            assertNotNull(eventDAO.find("hello2"));
            assertNotNull(eventDAO.find("hello3"));

            assertNotNull(authTokenDAO.find("DKbBMjXm"));
            assertNotNull(authTokenDAO.find("hello1"));
            db.closeConnection(false);

            ClearService service = new ClearService();
            ClearResult response = service.clear(true);
            Assertions.assertEquals("Clear succeeded.", response.getMessage());
            ClearResult response2 = service.clear(true);
            Assertions.assertEquals("Clear succeeded.", response2.getMessage());

            Database db2 = new Database();
            UserDAO userDAO2 = db2.getUserDAO();
            PersonDAO personDAO2 = db2.getPersonDAO();
            AuthTokenDAO authTokenDAO2 = db2.getAuthTokenDAO();
            EventDAO eventDAO2 = db2.getEventDAO();

            assertNull(userDAO2.find("sam1"));
            assertNull(userDAO2.find("sam2"));
            assertNull(userDAO2.find("sam3"));

            assertNull(personDAO2.find("sam1"));
            assertNull(personDAO2.find("sam2"));
            assertNull(personDAO2.find("sam3"));

            assertNull(eventDAO2.find("hello1"));
            assertNull(eventDAO2.find("hello2"));
            assertNull(eventDAO2.find("hello3"));

            assertNull(authTokenDAO2.find("DKbBMjXm"));
            assertNull(authTokenDAO2.find("hello1"));

            db2.closeConnection(true);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
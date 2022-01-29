package service;

import com.google.gson.Gson;
import dao.DataAccessException;
import dao.Database;
import model.AuthToken;
import model.Event;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.EventRequest;
import request.LoadRequest;
import result.EventsResult;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

class EventServiceTest {

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
    void eventPositive() {
        final String AUTH_TOKEN_NAME = "DKbBMjXm"; //authToken for sam4
        final String EVENT_ID = "8CCrU6qY"; //death of Gaylord
        EventRequest request1 = new EventRequest(AUTH_TOKEN_NAME);
        EventRequest request2 = new EventRequest(EVENT_ID, AUTH_TOKEN_NAME);
        EventService service1 = new EventService(request1);
        EventService service2 = new EventService(request2);
        EventsResult response1 = service1.event();
        EventsResult response2 = service2.event();

        Assertions.assertTrue(response1.isSuccess());
        Assertions.assertTrue(response2.isSuccess());
        Assertions.assertEquals(1, response1.getData().size());

        Event death1 = new Event(EVENT_ID, "sam4", "lUPX1Hif", (float)50.0666999816895,
                (float) 19.9333000183105, "Poland", "Kraków", "death", 2032);

        Event death2 = new Event(response2.getEventID(), response2.getAssociatedUsername(), response2.getPersonID(),
                (float)response2.getLatitude()  , (float)response2.getLongitude() , response2.getCountry(),
                response2.getCity(), response2.getEventType(), Integer.parseInt(response2.getYear()));
        Assertions.assertEquals(death1, death2);
    }

    @Test
    void eventNegativeBadAuthtoken() {
        final String AUTH_TOKEN_NAME = "DKbBMjX"; //missing an 'm' at the end
        EventRequest request = new EventRequest(AUTH_TOKEN_NAME);
        EventService service = new EventService(request);
        EventsResult response = service.event();

        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals("error: invalid authToken", response.getMessage());
    }

    @Test
    void eventNegativeBadEventID() {
        final String AUTH_TOKEN_NAME = "DKbBMjXm";
        final String EVENT_ID = "8CCrU6q"; //missing a 'Y' at the end

        EventRequest request = new EventRequest(EVENT_ID, AUTH_TOKEN_NAME);
        EventService service = new EventService(request);
        EventsResult response = service.event();

        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals("error: invalid eventID parameter", response.getMessage());
    }

    @Test
    void eventNegativeWrongUser() {
        final String AUTH_TOKEN_NAME = "hello1"; //valid authToken for user sam1, not sam4
        final String EVENT_ID = "8CCrU6qY";

        EventRequest request = new EventRequest(EVENT_ID, AUTH_TOKEN_NAME);
        EventService service = new EventService(request);
        EventsResult response = service.event();

        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals("error: requested event does not belong to this user",
                response.getMessage());
    }
}
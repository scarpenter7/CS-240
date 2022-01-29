package service;

import com.google.gson.Gson;
import dao.DataAccessException;
import dao.Database;
import model.AuthToken;
import model.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoadRequest;
import request.PersonRequest;
import result.PersonsResult;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

class PersonServiceTest {

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
    void personPositive() {
        final String AUTH_TOKEN_NAME = "DKbBMjXm"; //authToken for sam4
        final String PERSON_ID = "lUPX1Hif"; //personID for Gaylord
        PersonRequest request1 = new PersonRequest(AUTH_TOKEN_NAME);
        PersonRequest request2 = new PersonRequest(PERSON_ID, AUTH_TOKEN_NAME);
        PersonService service1 = new PersonService(request1);
        PersonService service2 = new PersonService(request2);
        PersonsResult response1 = service1.person();
        PersonsResult response2 = service2.person();

        Assertions.assertTrue(response1.isSuccess());
        Assertions.assertTrue(response2.isSuccess());
        Assertions.assertEquals(1, response1.getData().size());

        Person gaylord1 = new Person(PERSON_ID, "sam4", "m", "82yBk6gf",
                "n70B0h0p", "oBZZOWXi", "Gaylord", "Carp4");

        Person gaylord2 = new Person(response2.getPersonID(), response2.getUsername(), response2.getGender(),
                                    response2.getFatherID(), response2.getMotherID(), response2.getSpouseID(),
                                    response2.getFirstName(), response2.getLastName());
        Assertions.assertEquals(gaylord1, gaylord2);
    }

    @Test
    void personNegativeBadAuthtoken() {
        final String AUTH_TOKEN_NAME = "DKbBMjX"; //missing an 'm' at the end
        PersonRequest request = new PersonRequest(AUTH_TOKEN_NAME);
        PersonService service = new PersonService(request);
        PersonsResult response = service.person();

        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals("error: invalid authToken", response.getMessage());
    }

    @Test
    void personNegativeBadPersonID() {
        final String AUTH_TOKEN_NAME = "DKbBMjXm";
        final String PERSON_ID = "lUPX1Hi"; //missing an 'f' at the end

        PersonRequest request = new PersonRequest(PERSON_ID, AUTH_TOKEN_NAME);
        PersonService service = new PersonService(request);
        PersonsResult response = service.person();

        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals("error: invalid personID parameter", response.getMessage());
    }

    @Test
    void personNegativeWrongUser() {
        final String AUTH_TOKEN_NAME = "hello1"; //valid authToken for user sam1, not sam4
        final String PERSON_ID = "lUPX1Hif";
        PersonRequest request = new PersonRequest(PERSON_ID, AUTH_TOKEN_NAME);
        PersonService service = new PersonService(request);
        PersonsResult response = service.person();

        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals("error: requested person does not belong to this user",
                                response.getMessage());
    }
}
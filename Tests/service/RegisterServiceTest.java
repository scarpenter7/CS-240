package service;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoadRequest;
import request.RegisterRequest;
import result.RegisterResult;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {

    @BeforeEach
    public void loadInitialData() {
        try {
            Reader reader = new FileReader("json/tests.json");
            Gson gson = new Gson();
            LoadRequest request = gson.fromJson(reader, LoadRequest.class);
            reader.close();
            LoadService service = new LoadService(request);
            service.load(true);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void registerPositive() {
        RegisterRequest rr = new RegisterRequest("sam5", "hello4","sam5@test.com",
                                                "Samuel4", "Carp4", "f");
        RegisterService rs = new RegisterService(rr);
        try {
            RegisterResult registerResult = rs.register(false);
            Assertions.assertTrue(registerResult.isSuccess());
            Assertions.assertEquals("sam5", registerResult.getUserName());
            assertNotNull(registerResult.getAuthToken());
            assertNotNull(registerResult.getPersonID());
            assertNull(registerResult.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    void registerNegativeNonUniqueUserName() {
        //Username already in the database, should reject
        RegisterRequest rrBAD = new RegisterRequest("sam3", "hello4","sam4@test.com",
                "Samuel4", "Carp4", "f");

        RegisterService rsBAD = new RegisterService(rrBAD);
        try {
            RegisterResult registerResultBAD = rsBAD.register(false);
            Assertions.assertFalse(registerResultBAD.isSuccess());
            assertNull(registerResultBAD.getUserName());
            assertNull(registerResultBAD.getAuthToken());
            assertNull(registerResultBAD.getPersonID());
            Assertions.assertEquals("userName already taken error!", registerResultBAD.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    void registerNegativeEmptyUserName() {
        RegisterRequest rrBAD = new RegisterRequest("", "hello4","sam4@test.com",
                "Samuel4", "Carp4", "");

        RegisterService rsBAD = new RegisterService(rrBAD);
        try {
            RegisterResult registerResultBAD = rsBAD.register(false);
            Assertions.assertFalse(registerResultBAD.isSuccess());
            assertNull(registerResultBAD.getUserName());
            assertNull(registerResultBAD.getAuthToken());
            assertNull(registerResultBAD.getPersonID());
            Assertions.assertEquals("Missing userName error!", registerResultBAD.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    void registerNegativeInvalidGender() {
        RegisterRequest rrBAD = new RegisterRequest("sam5", "hello4","sam5@test.com",
                "Samuel4", "Carp4", "");

        RegisterService rsBAD = new RegisterService(rrBAD);
        try {
            RegisterResult registerResultBAD = rsBAD.register(false);
            Assertions.assertFalse(registerResultBAD.isSuccess());
            assertNull(registerResultBAD.getUserName());
            assertNull(registerResultBAD.getAuthToken());
            assertNull(registerResultBAD.getPersonID());
            Assertions.assertEquals("Invalid or missing gender error!", registerResultBAD.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            Assertions.fail();
        }

        RegisterRequest rrBAD2 = new RegisterRequest("sam5", "hello4","sam5@test.com",
                "Samuel4", "Carp4", "gangsta");

        RegisterService rsBAD2 = new RegisterService(rrBAD2);
        try {
            RegisterResult registerResultBAD2 = rsBAD2.register(false);
            Assertions.assertFalse(registerResultBAD2.isSuccess());
            assertNull(registerResultBAD2.getUserName());
            assertNull(registerResultBAD2.getAuthToken());
            assertNull(registerResultBAD2.getPersonID());
            Assertions.assertEquals("Invalid or missing gender error!", registerResultBAD2.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    void registerNegativeBadEmail() {
        RegisterRequest rrBAD = new RegisterRequest("sam5", "hello4","sam5test.com",
                "Samuel4", "Carp4", "");

        RegisterService rsBAD = new RegisterService(rrBAD);
        try {
            RegisterResult registerResultBAD = rsBAD.register(false);
            Assertions.assertFalse(registerResultBAD.isSuccess());
            assertNull(registerResultBAD.getUserName());
            assertNull(registerResultBAD.getAuthToken());
            assertNull(registerResultBAD.getPersonID());
            Assertions.assertEquals("Invalid or missing email error!", registerResultBAD.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            Assertions.fail();
        }

        RegisterRequest rrBAD2 = new RegisterRequest("sam5", "hello5","",
                "Samuel4", "Carp4", "");

        RegisterService rsBAD2 = new RegisterService(rrBAD2);
        try {
            RegisterResult registerResultBAD2 = rsBAD2.register(false);
            Assertions.assertFalse(registerResultBAD2.isSuccess());
            assertNull(registerResultBAD2.getUserName());
            assertNull(registerResultBAD2.getAuthToken());
            assertNull(registerResultBAD2.getPersonID());
            Assertions.assertEquals("Invalid or missing email error!", registerResultBAD2.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }
}
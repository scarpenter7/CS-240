package service;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoadRequest;
import request.LoginRequest;
import result.LoginResult;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {

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
    void loginPositive() {
        LoginRequest lr = new LoginRequest("sam1", "hello1");
        LoginService ls = new LoginService(lr);
        try {
            LoginResult loginResult = ls.login(false);
            Assertions.assertTrue(loginResult.isSuccess());
            Assertions.assertEquals("sam1", loginResult.getPersonID());
            assertNull(loginResult.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    void loginNegativeBadUsername() {
        LoginRequest lr = new LoginRequest("sam111", "hello1");
        LoginService ls = new LoginService(lr);
        try {
            LoginResult loginResult = ls.login(false);
            Assertions.assertFalse(loginResult.isSuccess());
            Assertions.assertEquals("Invalid userName error!", loginResult.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    void loginNegativeBadPassword() {
        LoginRequest lr = new LoginRequest("sam1", "hello99");
        LoginService ls = new LoginService(lr);
        try {
            LoginResult loginResult = ls.login(false);
            Assertions.assertFalse(loginResult.isSuccess());
            Assertions.assertEquals("Incorrect password error!", loginResult.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }
}
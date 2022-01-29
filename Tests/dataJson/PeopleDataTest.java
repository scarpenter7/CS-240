package dataJson;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import result.RegisterResult;
import service.RegisterService;

import java.io.IOException;

class PeopleDataTest {

    @Test
    void generateUserPerson() {
        RegisterRequest rr = new RegisterRequest("sam4", "hello4", "sam4@test.com",
                                                "Samuel4", "Carp4", "f");
        RegisterService rs = new RegisterService(rr);
        try {
            RegisterResult registerResult = rs.register(false);
        }
        catch (IOException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    void generateRandomParent() {
    }

    @Test
    void generateEvent() {
    }
}
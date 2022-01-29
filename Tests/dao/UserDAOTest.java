package dao;

import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private Database db;
    private UserDAO userDAO;

    @BeforeEach
    public void  setUp() throws DataAccessException, SQLException {
        db = new Database();
        userDAO = new UserDAO(db.getConnection());
        userDAO.clear();
        User user1 = new User("sam1","sam1", "m", "hello1",
                "sam1@test.com","Samuel1", "Carp1");
        User user2 = new User("sam2","sam2", "f", "hello2",
                "sam2@test.com","Samuel2", "Carp2");
        User user3 = new User("sam3","sam3", "m", "hello3",
                "sam3@test.com","Samuel3", "Carp3");

        userDAO.insert(user1);
        userDAO.insert(user2);
        userDAO.insert(user3);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
        db = null;
    }

    @Test
    void insertPositive() {
        try {
            User user = new User("sam5","sam4", "f", "hello4",
                            "sam99@test.com","Samuel4", "Carp4");
            userDAO.insert(user);
            Assertions.assertEquals(user, userDAO.find("sam5"));
        }
        catch (DataAccessException d) {
            System.out.println("DataAccess exception in UserDAO insertPositive test");
            Assertions.fail();
        }
    }

    @Test
    public void insertNegativeNonUniqueUsername() {
        try {
            String badUsername = "sam5";
            User user1 = new User( badUsername,"sam9", "f",
                    "hello4","sam99@test.com","Samuel4", "Carp4");
            User user2 = new User( badUsername,"sam44", "f",
                    "hello44","sam44@test.com","Samuel44", "Carp44");
            userDAO.insert(user1);
            Assertions.assertThrows(DataAccessException.class, () -> userDAO.insert(user2));
            Assertions.assertNotEquals(user2, userDAO.find("sam5"));
            Assertions.assertEquals(user1, userDAO.find("sam5"));
        }
        catch (DataAccessException d) {
            System.out.println("DataAccess exception in insertNegativeNonUniqueUsername");
            Assertions.fail();
        }
    }

    @Test
    void findPositive() {
        try {
            User user1 = new User("sam1","sam1", "m", "hello1",
                    "sam1@test.com","Samuel1", "Carp1");
            User user2 = new User("sam2","sam2", "f", "hello2",
                    "sam2@test.com","Samuel2", "Carp2");
            User user3 = new User("sam3","sam3", "m", "hello3",
                    "sam3@test.com","Samuel3", "Carp3");

            Assertions.assertEquals(user1, userDAO.find("sam1"));
            Assertions.assertEquals(user2, userDAO.find("sam2"));
            Assertions.assertEquals(user3, userDAO.find("sam3"));
        }
        catch (DataAccessException d) {
            Assertions.fail();
            System.out.println("DataAccess exception in UserDAO findPositive test");
        }
    }

    @Test
    void findNegativeWrongUserNames() {
        try {
            User user11 = new User("sam11","sam1", "m", "hello1",
                    "sam1@test.com","Samuel1", "Carp1");
            User user22 = new User("sam22","sam2", "f", "hello2",
                    "sam2@test.com","Samuel2", "Carp2");
            User user33 = new User("sam33","sam3", "m", "hello3",
                    "sam3@test.com","Samuel3", "Carp3");
            Assertions.assertNotEquals(user11, userDAO.find("sam1"));
            Assertions.assertNotEquals(user22, userDAO.find("sam2"));
            Assertions.assertNotEquals(user33, userDAO.find("sam3"));
        }
        catch (DataAccessException d) {
            Assertions.fail();
            System.out.println("DataAccess exception in UserDAO findNegativeWrongGenders test");
        }
    }

    @Test
    void clear() {
        try {
            User user1 = new User("sam1","sam1", "m", "hello1",
                    "sam1@test.com","Samuel1", "Carp1");
            User user2 = new User("sam2","sam2", "f", "hello2",
                    "sam2@test.com","Samuel2", "Carp2");
            User user3 = new User("sam3","sam3", "m", "hello3",
                    "sam3@test.com","Samuel3", "Carp3");

            Assertions.assertEquals(user1, userDAO.find("sam1"));
            Assertions.assertEquals(user2, userDAO.find("sam2"));
            Assertions.assertEquals(user3, userDAO.find("sam3"));

            userDAO.clear();

            assertNull(userDAO.find("sam1"));
            assertNull(userDAO.find("sam2"));
            assertNull(userDAO.find("sam3"));
        }
        catch (DataAccessException d) {
            Assertions.fail();
            System.out.println("DataAccess exception in clear test");
        }
    }
}
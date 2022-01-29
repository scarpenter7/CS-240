package dao;

import model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AuthTokenDAOTest {

    private Database db;
    private AuthTokenDAO authTokenDAO;

    @BeforeEach
    public void  setUp() throws DataAccessException, SQLException {
        db = new Database();
        authTokenDAO = new AuthTokenDAO(db.getConnection());
        authTokenDAO.clear();
        AuthToken authToken1 = new AuthToken("hello1","sam1");
        AuthToken authToken2 = new AuthToken("hello2","sam2");
        AuthToken authToken3 = new AuthToken("hello22","sam2");
        AuthToken authToken4 = new AuthToken("hello222","sam2");
        AuthToken authToken5 = new AuthToken("hello3","sam3");

        authTokenDAO.insert(authToken1);
        authTokenDAO.insert(authToken2);
        authTokenDAO.insert(authToken3);
        authTokenDAO.insert(authToken4);
        authTokenDAO.insert(authToken5);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
        db = null;
    }

    @Test
    void insertPositive() {
        try {
            AuthToken authToken1 = new AuthToken("hello4","sam4");
            AuthToken authToken2 = new AuthToken("hello2222","sam2");
            AuthToken authToken3 = new AuthToken("hello33","sam3");
            authTokenDAO.insert(authToken1);
            authTokenDAO.insert(authToken2);
            authTokenDAO.insert(authToken3);
            Assertions.assertEquals(authToken1, authTokenDAO.find("hello4"));
            Assertions.assertEquals(authToken2, authTokenDAO.find("hello2222"));
            Assertions.assertEquals(authToken3, authTokenDAO.find("hello33"));
        }
        catch (DataAccessException d) {
            System.out.println("DataAccess exception in AuthTokenDAO insertPositive test");
            Assertions.fail();
        }
    }

    @Test
    void insertNegativeNonUniqueName() {
        try {
            AuthToken authToken1 = new AuthToken("hello4","sam4");
            AuthToken authToken2 = new AuthToken("hello4","sam2");
            AuthToken authToken3 = new AuthToken("hello33","sam3");
            AuthToken authToken4 = new AuthToken("hello33","sam1");
            authTokenDAO.insert(authToken1);
            authTokenDAO.insert(authToken3);
            Assertions.assertThrows(DataAccessException.class, () -> authTokenDAO.insert(authToken2));
            Assertions.assertThrows(DataAccessException.class, () -> authTokenDAO.insert(authToken4));
            Assertions.assertNotEquals(authToken2, authTokenDAO.find("hello4"));
            Assertions.assertNotEquals(authToken4, authTokenDAO.find("hello33"));
            Assertions.assertEquals(authToken1, authTokenDAO.find("hello4"));
            Assertions.assertEquals(authToken3, authTokenDAO.find("hello33"));
        }
        catch (DataAccessException d) {
            System.out.println("DataAccess exception in AuthTokenDAO insertPositive test");
            Assertions.fail();
        }
    }

    @Test
    void findPositive() {
        try {
            AuthToken authToken1 = new AuthToken("hello1","sam1");
            AuthToken authToken2 = new AuthToken("hello2","sam2");
            AuthToken authToken3 = new AuthToken("hello22","sam2");
            AuthToken authToken4 = new AuthToken("hello3","sam3");

            Assertions.assertEquals(authToken1, authTokenDAO.find("hello1"));
            Assertions.assertEquals(authToken2, authTokenDAO.find("hello2"));
            Assertions.assertEquals(authToken3, authTokenDAO.find("hello22"));
            Assertions.assertEquals(authToken4, authTokenDAO.find("hello3"));
        }
        catch (DataAccessException d) {
            System.out.println("DataAccess exception in AuthTokenDAO findPositive test");
            Assertions.fail();
        }
    }

    @Test
    void findNegativeWrongUserName() {
        try {
            AuthToken authToken11 = new AuthToken("hello1","sam2");
            AuthToken authToken22 = new AuthToken("hello2","sam1");
            AuthToken authToken33 = new AuthToken("hello22","sam3");
            AuthToken authToken44 = new AuthToken("hello3","sam2");

            Assertions.assertNotEquals(authToken11, authTokenDAO.find("hello1"));
            Assertions.assertNotEquals(authToken22, authTokenDAO.find("hello2"));
            Assertions.assertNotEquals(authToken33, authTokenDAO.find("hello22"));
            Assertions.assertNotEquals(authToken44, authTokenDAO.find("hello3"));
        }
        catch (DataAccessException d) {
            System.out.println("DataAccess exception in AuthTokenDAO findNegative test");
            Assertions.fail();
        }
    }


    @Test
    void clear() {
        try {
            AuthToken authToken1 = new AuthToken("hello1","sam1");
            AuthToken authToken2 = new AuthToken("hello2","sam2");
            AuthToken authToken3 = new AuthToken("hello22","sam2");
            AuthToken authToken4 = new AuthToken("hello222","sam2");
            AuthToken authToken5 = new AuthToken("hello3","sam3");

            Assertions.assertEquals(authToken1, authTokenDAO.find("hello1"));
            Assertions.assertEquals(authToken2, authTokenDAO.find("hello2"));
            Assertions.assertEquals(authToken3, authTokenDAO.find("hello22"));
            Assertions.assertEquals(authToken4, authTokenDAO.find("hello222"));
            Assertions.assertEquals(authToken5, authTokenDAO.find("hello3"));

            authTokenDAO.clear();

            assertNull(authTokenDAO.find("hello1"));
            assertNull(authTokenDAO.find("hello2"));
            assertNull(authTokenDAO.find("hello22"));
            assertNull(authTokenDAO.find("hello222"));
            assertNull(authTokenDAO.find("hello3"));
        }
        catch (DataAccessException d) {
            System.out.println("DataAccess exception in clear test");
            Assertions.fail();
        }
    }
}
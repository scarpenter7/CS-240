package dao;


import model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EventDAOTest {

    private Database db;
    private EventDAO eventDAO;

    @BeforeEach
    public void  setUp() throws DataAccessException, SQLException {
        db = new Database();
        eventDAO = new EventDAO(db.getConnection());
        eventDAO.clear();
        Event event1 = new Event("hello1", "sam1", "sam1", 1, 1,
                "USA1", "Seattle1", "EventType1", 1);
        Event event2 = new Event("hello2", "sam1", "sam1", 2, 2,
                "USA2", "Seattle2", "EventType2", 2);
        Event event3 = new Event("hello3", "sam1", "sam1", 3, 3,
                "USA3", "Seattle3", "EventType3", 3);
        Event event4 = new Event("hello11", "", "curt11", 1, 1,
                "USA1", "Seattle1", "EventType1", 1);

        eventDAO.insert(event1);
        eventDAO.insert(event2);
        eventDAO.insert(event3);
        eventDAO.insert(event4);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
        db = null;
    }

    @Test
    void insertPositive() {
        try {
            Event event1 = new Event("hello4", "sam1", "sam1", 4, 4,
                    "USA4", "Seattle4", "EventType3", 4);
            Event event2 = new Event("hello44", "curt11", "sam1", 4, 4,
                    "USA4", "Seattle4", "EventType3", 4);
            eventDAO.insert(event1);
            eventDAO.insert(event2);
            Assertions.assertEquals(event1, eventDAO.find("hello4"));
            Assertions.assertEquals(event2, eventDAO.find("hello44"));
        }
        catch (DataAccessException d) {
            System.out.println("DataAccess exception in EventDAO insertPositive test");
            Assertions.fail();
        }
    }

    @Test
    void insertNegativeNonUniqueEventID() {
        try {
            Event event1 = new Event("hello5", "sam1", "sam1", 5, 5,
                    "USA5", "Seattle5", "EventType5", 5);
            Event event2 = new Event("hello5", "curt1", "curt1", 55, 55,
                    "USA55", "Seattle55", "EventType5", 55);
            Event event3 = new Event("hello9", "sam2", "sam1", 2, 2,
                    "USA1", "Seattle1", "EventType1", 1);
            Event event4 = new Event("hello9", "sam1", "sam1", 1, 1,
                    "USA1", "Seattle1", "EventType1", 1);

            eventDAO.insert(event1);
            eventDAO.insert(event4);
            Assertions.assertThrows(DataAccessException.class, () -> eventDAO.insert(event2));
            Assertions.assertThrows(DataAccessException.class, () -> eventDAO.insert(event3));
            Assertions.assertNotEquals(event2, eventDAO.find("hello5"));
            Assertions.assertNotEquals(event3, eventDAO.find("hello9"));
            Assertions.assertEquals(event1, eventDAO.find("hello5"));
            Assertions.assertEquals(event4, eventDAO.find("hello9"));
        }
        catch (DataAccessException d) {
            System.out.println("DataAccess exception in EventDAO insertPositive test");
            Assertions.fail();
        }
    }

    @Test
    void findPositive() {
        try {
            Event event1 = new Event("hello1", "sam1", "sam1", 1, 1,
                    "USA1", "Seattle1", "EventType1", 1);
            Event event2 = new Event("hello2", "sam1", "sam1", 2, 2,
                    "USA2", "Seattle2", "EventType2", 2);
            Event event3 = new Event("hello3", "sam1", "sam1", 3, 3,
                    "USA3", "Seattle3", "EventType3", 3);
            Event event4 = new Event("hello11", "", "curt11", 1, 1,
                    "USA1", "Seattle1", "EventType1", 1);

            Assertions.assertEquals(event1, eventDAO.find("hello1"));
            Assertions.assertEquals(event2, eventDAO.find("hello2"));
            Assertions.assertEquals(event3, eventDAO.find("hello3"));
            Assertions.assertEquals(event4, eventDAO.find("hello11"));
        }
        catch (DataAccessException d) {
            System.out.println("DataAccess exception in EventDAO findPositive test");
            Assertions.fail();
        }
    }

    @Test
    void findNegativeWrongLatitude() {
        try {
            //wrong latitudes
            Event event11 = new Event("hello1", "sam1", "sam1", 11, 1,
                    "USA1", "Seattle1", "EventType1", 1);
            Event event22 = new Event("hello2", "sam1", "sam1", 22, 2,
                    "USA2", "Seattle2", "EventType2", 2);
            Event event33 = new Event("hello3", "sam1", "sam1", 33, 3,
                    "USA3", "Seattle3", "EventType3", 3);
            Event event44 = new Event("hello11", "", "curt11", 11, 1,
                    "USA1", "Seattle1", "EventType1", 1);
            Assertions.assertNotEquals(event11, eventDAO.find("hello1"));
            Assertions.assertNotEquals(event22, eventDAO.find("hello2"));
            Assertions.assertNotEquals(event33, eventDAO.find("hello3"));
            Assertions.assertNotEquals(event44, eventDAO.find("hello11"));
        }
        catch (DataAccessException d) {
            System.out.println("DataAccess exception in EventDAO findNegative test");
            Assertions.fail();
        }
    }

    @Test
    void findAlternatePositive() {
        try {
            Event event1 = new Event("hello1", "sam1", "sam1", 1, 1,
                    "USA1", "Seattle1", "EventType1", 1);
            Event event2 = new Event("hello2", "sam1", "sam1", 2, 2,
                    "USA2", "Seattle2", "EventType2", 2);
            Event event3 = new Event("hello3", "sam1", "sam1", 3, 3,
                    "USA3", "Seattle3", "EventType3", 3);
            Event event4 = new Event("hello11", "", "curt11", 1, 1,
                    "USA1", "Seattle1", "EventType1", 1);

            Assertions.assertEquals(event1, eventDAO.find("sam1","EventType1"));
            Assertions.assertEquals(event2, eventDAO.find("sam1","EventType2"));
            Assertions.assertEquals(event3, eventDAO.find("sam1","EventType3"));
            Assertions.assertEquals(event4, eventDAO.find("curt11","EventType1"));
        }
        catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void findAlternateNegativeBadEventTypes() {
        try {
            Assertions.assertNull(eventDAO.find("sam1","EventType99"));
            Assertions.assertNull(eventDAO.find("sam1","EventType99"));
            Assertions.assertNull(eventDAO.find("sam1","EventType99"));
            Assertions.assertNull(eventDAO.find("curt11","EventType99"));
        }
        catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void findAllAssociatedPositive() {
        try {
            ArrayList<Event> events = eventDAO.findAllAssociated("sam1");
            Assertions.assertEquals(3, events.size());
            Event event0 = events.get(0);
            Event event1 = events.get(1);
            Event event2 = events.get(2);

            Event event00 = new Event("hello1", "sam1", "sam1", 1, 1,
                    "USA1", "Seattle1", "EventType1", 1);
            Event event11 = new Event("hello2", "sam1", "sam1", 2, 2,
                    "USA2", "Seattle2", "EventType2", 2);
            Event event22 = new Event("hello3", "sam1", "sam1", 3, 3,
                    "USA3", "Seattle3", "EventType3", 3);

            Assertions.assertEquals(event00, event0);
            Assertions.assertEquals(event11, event1);
            Assertions.assertEquals(event22, event2);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    void findAllAssociatedNegativeBadUserName() {
        try {
            ArrayList<Event> events = eventDAO.findAllAssociated("curt11");
            Assertions.assertEquals(0, events.size());
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    void clear() {
        try {
            Event event1 = new Event("hello1", "sam1", "sam1", 1, 1,
                    "USA1", "Seattle1", "EventType1", 1);
            Event event2 = new Event("hello2", "sam1", "sam1", 2, 2,
                    "USA2", "Seattle2", "EventType2", 2);
            Event event3 = new Event("hello3", "sam1", "sam1", 3, 3,
                    "USA3", "Seattle3", "EventType3", 3);
            Event event4 = new Event("hello11", "", "curt11", 1, 1,
                    "USA1", "Seattle1", "EventType1", 1);

            Assertions.assertEquals(event1, eventDAO.find("hello1"));
            Assertions.assertEquals(event2, eventDAO.find("hello2"));
            Assertions.assertEquals(event3, eventDAO.find("hello3"));
            Assertions.assertEquals(event4, eventDAO.find("hello11"));

            eventDAO.clear();

            assertNull(eventDAO.find("hello1"));
            assertNull(eventDAO.find("hello2"));
            assertNull(eventDAO.find("hello3"));
            assertNull(eventDAO.find("hello11"));
        }
        catch (DataAccessException d) {
            System.out.println("DataAccess exception in EventDAO clear test");
            Assertions.fail();
        }
    }

    @Test
    void clearAlternate() {
        try {
            //should survive
            Event event1 = new Event("hello1", "sam1", "sam1", 1, 1,
                    "USA1", "Seattle1", "EventType1", 1);
            Event event2 = new Event("hello2", "sam1", "sam1", 2, 2,
                    "USA2", "Seattle2", "EventType2", 2);
            Event event3 = new Event("hello3", "sam1", "sam1", 3, 3,
                    "USA3", "Seattle3", "EventType3", 3);
            Event event4 = new Event("hello11", "", "curt11", 1, 1,
                    "USA1", "Seattle1", "EventType1", 1);
            //should not survive
            Event event5 = new Event("hello999", "sam1", "sam99", 3, 3,
                    "USA3", "Seattle3", "EventType3", 3);

            eventDAO.insert(event5);
            Assertions.assertEquals(event1, eventDAO.find("hello1"));
            Assertions.assertEquals(event2, eventDAO.find("hello2"));
            Assertions.assertEquals(event3, eventDAO.find("hello3"));
            Assertions.assertEquals(event4, eventDAO.find("hello11"));
            Assertions.assertEquals(event5, eventDAO.find("hello999"));

            eventDAO.clear("sam1","sam1");

            assertNotNull(eventDAO.find("hello1"));
            assertNotNull(eventDAO.find("hello2"));
            assertNotNull(eventDAO.find("hello3"));
            assertNotNull(eventDAO.find("hello11"));
            assertNull(eventDAO.find("hello999"));
        }
        catch (DataAccessException d) {
            System.out.println("DataAccess exception in EventDAO clear test");
            Assertions.fail();
        }
    }
}
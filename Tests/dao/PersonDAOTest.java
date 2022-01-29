package dao;

import model.Event;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PersonDAOTest {

    private Database db;
    private PersonDAO personDAO;

    @BeforeEach
    public void  setUp() throws DataAccessException, SQLException {
        db = new Database();
        personDAO = new PersonDAO(db.getConnection());
        personDAO.clear();
        Person person1 = new Person("sam1","sam1", "m", "curt1",
                "hedi1", "spouse1", "Samuel1", "Carp1");
        Person person2 = new Person("sam2","sam2", "f", "curt2",
                "hedi2", "spouse2", "Samuel2", "Carp2");
        Person person3 = new Person("sam3","sam3", "m", "curt3",
                "hedi3", "spouse3", "Samuel3", "Carp3");

        personDAO.insert(person1);
        personDAO.insert(person2);
        personDAO.insert(person3);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
        db = null;
    }

    @Test
    void insertPositive() {
        try {
            Person person = new Person("sam4","sam4", "f", "curt4",
                    "hedi4","spouse4", "Samuel4", "Carp4");
            personDAO.insert(person);
            Assertions.assertEquals(person, personDAO.find("sam4"));
        }
        catch (DataAccessException d) {
            System.out.println("DataAccess exception in PersonDAO insertPositive test");
            Assertions.fail();
        }
    }

    @Test
    public void insertNegativeNonUniquePersonID() {
        try {
            String badPersonID = "sam4";
            Person person1 = new Person( badPersonID, "sam4", "f",
                    "curt4","hedi4","spouse4", "Samuel4", "Carp4");
            Person person2 = new Person( badPersonID, "", "f",
                    "curt4","hedi4","spouse4", "Samuel4", "Carp4");
            personDAO.insert(person1);
            Assertions.assertThrows(DataAccessException.class, () -> personDAO.insert(person2));
            Assertions.assertNotEquals(person2, personDAO.find("sam4"));
            Assertions.assertEquals(person1, personDAO.find("sam4"));
        }
        catch (DataAccessException d) {
            System.out.println("DataAccess exception in insertNegativeNonUniquePersonID");
            Assertions.fail();
        }
    }

    @Test
    void findPositive() {
        try {
            Person person1 = new Person("sam1","sam1", "m", "curt1",
                    "hedi1", "spouse1", "Samuel1", "Carp1");
            Person person2 = new Person("sam2","sam2", "f", "curt2",
                    "hedi2", "spouse2", "Samuel2", "Carp2");
            Person person3 = new Person("sam3","sam3", "m", "curt3",
                    "hedi3", "spouse3", "Samuel3", "Carp3");

            Assertions.assertEquals(person1, personDAO.find("sam1"));
            Assertions.assertEquals(person2, personDAO.find("sam2"));
            Assertions.assertEquals(person3, personDAO.find("sam3"));
        }
        catch (DataAccessException d) {
            System.out.println("DataAccess exception in PersonDAO findPositive test");
            Assertions.fail();
        }
    }

    @Test
    void findNegativeWrongGenders() {
        try {
            //Wrong genders
            Person person11 = new Person("sam1","sam1", "f", "curt1",
                    "hedi1","spouse1", "Samuel1", "Carp1");
            Person person22 = new Person("sam2","sam2", "m", "curt2",
                    "hedi2","spouse2", "Samuel2", "Carp2");
            Person person33 = new Person("sam3","sam3", "f", "curt3",
                    "hedi3","spouse3", "Samuel3", "Carp3");
            Assertions.assertNotEquals(person11, personDAO.find("sam1"));
            Assertions.assertNotEquals(person22, personDAO.find("sam2"));
            Assertions.assertNotEquals(person33, personDAO.find("sam3"));
        }
        catch (DataAccessException d) {
            System.out.println("DataAccess exception in PersonDAO findNegativeWrongGenders test\n" + d);
            Assertions.fail();
        }
    }

    @Test
    void findAllAssociatedPositive() {
        try {
            Person person11 = new Person("sam99","sam1", "m", "curt1",
                    "hedi1", "spouse1", "Samuel1", "Carp1");
            personDAO.insert(person11);

            ArrayList<Person> people = personDAO.findAllAssociated("sam1");
            Assertions.assertEquals(2, people.size());
            Person person0 = people.get(0);
            Person person1 = people.get(1);

            Person person00 = new Person("sam1","sam1", "m", "curt1",
                    "hedi1", "spouse1", "Samuel1", "Carp1");


            Assertions.assertEquals(person0, person00);
            Assertions.assertEquals(person1, person11);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    void findAllAssociatedNegativeBadUserName() {
        try {
            ArrayList<Person> people = personDAO.findAllAssociated("curt1");
            Assertions.assertEquals(0, people.size());
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    void clear() {
        try {
            Person person1 = new Person("sam1","sam1", "m", "curt1",
                    "hedi1","spouse1", "Samuel1", "Carp1");
            Person person2 = new Person("sam2","sam2", "f", "curt2",
                    "hedi2","spouse2", "Samuel2", "Carp2");
            Person person3 = new Person("sam3","sam3", "m", "curt3",
                    "hedi3","spouse3", "Samuel3", "Carp3");

            Assertions.assertEquals(person1, personDAO.find("sam1"));
            Assertions.assertEquals(person2, personDAO.find("sam2"));
            Assertions.assertEquals(person3, personDAO.find("sam3"));

            personDAO.clear();

            assertNull(personDAO.find("sam1"));
            assertNull(personDAO.find("sam2"));
            assertNull(personDAO.find("sam3"));
        }
        catch (DataAccessException d) {
            System.out.println("DataAccess exception in clear test");
            Assertions.fail();
        }
    }

    @Test
    void clearAlternate() {
        try {
            //should survive
            Person person1 = new Person("sam1","sam1", "m", "curt1",
                    "hedi1", "spouse1", "Samuel1", "Carp1");
            //should survive
            Person person2 = new Person("sam2","sam2", "f", "curt2",
                    "hedi2", "spouse2", "Samuel2", "Carp2");
            //should survive
            Person person3 = new Person("sam3","sam3", "m", "curt3",
                    "hedi3", "spouse3", "Samuel3", "Carp3");
            //should not survive
            Person person4 = new Person("sam4","sam1", "m", "curt1",
                    "hedi1", "spouse1", "Samuel1", "Carp1");

            personDAO.insert(person4);
            Assertions.assertEquals(person1, personDAO.find("sam1"));
            Assertions.assertEquals(person2, personDAO.find("sam2"));
            Assertions.assertEquals(person3, personDAO.find("sam3"));
            Assertions.assertEquals(person4, personDAO.find("sam4"));

            personDAO.clear("sam1","sam1");

            assertNotNull(personDAO.find("sam1"));
            assertNotNull(personDAO.find("sam2"));
            assertNotNull(personDAO.find("sam3"));
            assertNull(personDAO.find("sam4"));
        }
        catch (DataAccessException d) {
            System.out.println("DataAccess exception in EventDAO clear test");
            Assertions.fail();
        }
    }
}
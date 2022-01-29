package dao;

import dataJson.PeopleData;
import model.Event;
import model.Person;

import java.sql.*;
import java.util.*;

/** Manages the connection to the Database
 *  Commits changes made into the Database
 */
public class Database {
    private Connection conn;
    private UserDAO userDAO;
    private PersonDAO personDAO;
    private EventDAO eventDAO;
    private AuthTokenDAO authTokenDAO;

    public Database() throws DataAccessException {
        openConnection();
        userDAO = new UserDAO(conn);
        personDAO = new PersonDAO(conn);
        eventDAO = new EventDAO(conn);
        authTokenDAO = new AuthTokenDAO(conn);
    }

    //Whenever we want to make a change to our database we will have to open a connection and use
    //Statements created by that connection to initiate transactions
    /** Opens a connection
     * @throws DataAccessException if something goes wrong in the Database
     * @return Connection
     */
    public Connection openConnection() throws DataAccessException {
        try {
            //The Structure for this Connection is driver:language:path
            //The path assumes you start in the root of your project unless given a non-relative path
            final String CONNECTION_URL = "jdbc:sqlite:FMS.sqlite";

            // Open a database connection to the file given in the path
            conn = DriverManager.getConnection(CONNECTION_URL);

            // Start a transaction
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }
        return conn;
    }

    /** Obtains connection
     * @throws DataAccessException if connection goes wrong in the database
     * @return connection
     */
    public Connection getConnection() throws DataAccessException {
        if(conn == null) {
            return openConnection();
        } else {
            return conn;
        }
    }

    //When we are done manipulating the database it is important to close the connection. This will
    //End the transaction and allow us to either commit our changes to the database or rollback any
    //changes that were made before we encountered a potential error.

    //IMPORTANT: IF YOU FAIL TO CLOSE A CONNECTION AND TRY TO REOPEN THE DATABASE THIS WILL CAUSE THE
    //DATABASE TO LOCK. YOUR CODE MUST ALWAYS INCLUDE A CLOSURE OF THE DATABASE NO MATTER WHAT ERRORS
    //OR PROBLEMS YOU ENCOUNTER
    /** Closes connection
     * @param commit whether or not the changes will be saved to the database
     * @throws DataAccessException if something goes wrong in the database
     */
    public void closeConnection(boolean commit) throws DataAccessException {
        try {
            if (commit) {
                //This will commit the changes to the database
                conn.commit();
            } else {
                //If we find out something went wrong, pass a false into closeConnection and this
                //will rollback any changes we made during this connection
                conn.rollback();
            }

            conn.close();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    /** Fills generations inside the database.
     * This method is called by the fillGenerations method of the fillGenerations
     * service. This function is also called by the register service when it fills
     * the User's generations.
     * @param userName specifies which user is having their generations filled
     * @param numGenerations fills specified amount of generations
     * @return boolean whether or not it was successful
     */
    public int fillGenerations (int numGenerations, String userName, Person userPerson) {
        int peopleGenerated = 0;
        try {
            peopleGenerated = fillGenerationsHelper(numGenerations, userName, userPerson);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (!validEvents(userName)) { //if the events are not valid, start over
            try {
                personDAO.clear(userName, userPerson.getPersonID());
                eventDAO.clear(userName, userPerson.getPersonID());
                return fillGenerations(numGenerations, userName, userPerson);
            }
            catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
        return peopleGenerated;
    }

    private int fillGenerationsHelper(int numGenerations, String userName, Person userPerson) throws Exception {
        int peopleGenerated = 0;
        PeopleData pplData = new PeopleData();
        ArrayList<Person> currentGeneration = new ArrayList<>();
        currentGeneration.add(userPerson);
        for (int i = 0; i < numGenerations; ++i) {
            ArrayList<Person> currentGenerationCopy = new ArrayList<>(currentGeneration);
            currentGeneration.clear();
            boolean isLastGeneration = false;
            //strip off last generation's mother and father ID's
            if (i == numGenerations - 1) { isLastGeneration = true; }
            for (Person person : currentGenerationCopy) {
                Person father = pplData.generateRandomParent(this, person, "m",
                        userName, isLastGeneration);
                Person mother = pplData.generateRandomParent(this, person, "f",
                        userName, isLastGeneration);
                currentGeneration.add(father);
                currentGeneration.add(mother);
                peopleGenerated += 2;
            }
        }
        return peopleGenerated;
    }

    private boolean validEvents(String userName) {
        try {
            ArrayList<Person> people = personDAO.findAllAssociated(userName);

            for (Person person : people) {
                Event birth = eventDAO.find(person.getPersonID(), "birth");
                Event marriage = eventDAO.find(person.getPersonID(), "marriage");

                if (marriage == null) { //the person is a user
                    continue;
                }
                int birthYear = birth.getYear();
                int marriageYear = marriage.getYear();
                if (marriageYear - birthYear < 14) {
                    return false;
                }
            }
        }
        catch (DataAccessException e) {
            e.printStackTrace();
        }
        return true;
    }

    /** Clears all data in all tables
     *
     * @throws DataAccessException if something goes wrong in the database
     */
    public void clearTables() {
        try {
            userDAO.clear();
            authTokenDAO.clear();
            personDAO.clear();
            eventDAO.clear();
        }
        catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    private String generateAlphaNumeric() {
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        StringBuilder authTokenBuilder = new StringBuilder(8);
        //Always generates 8 character long String
        for (int i = 0; i < 8; i++) {
            // generate a random number between 0 to AlphaNumericString variable length
            int index = (int) (AlphaNumericString.length() * Math.random());
            authTokenBuilder.append(AlphaNumericString.charAt(index));
        }
        return authTokenBuilder.toString();
    }

    private String checkUniqueAuthTokenName(String authTokenName) throws DataAccessException {
        if (this.getAuthTokenDAO().find(authTokenName) != null) {
            authTokenName = generateAlphaNumeric();
            return checkUniqueAuthTokenName(authTokenName);
        }
        return authTokenName;
    }

    private String checkUniquePersonID (String personID) throws DataAccessException {
        if (this.getAuthTokenDAO().find(personID) != null) {
            personID = generateAlphaNumeric();
            return checkUniquePersonID(personID);
        }
        return personID;
    }

    private String checkUniqueEventID (String eventID) throws DataAccessException {
        if (this.getAuthTokenDAO().find(eventID) != null) {
            eventID = generateAlphaNumeric();
            return checkUniqueEventID(eventID);
        }
        return eventID;
    }

    public String generateUniqueAuthTokenName () throws  DataAccessException{
        return checkUniqueAuthTokenName(generateAlphaNumeric());
    }

    public String generateUniquePersonID () throws  DataAccessException{
        return checkUniquePersonID(generateAlphaNumeric());
    }

    public String generateUniqueEventID () throws  DataAccessException{
        return checkUniqueEventID(generateAlphaNumeric());
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public PersonDAO getPersonDAO() {
        return personDAO;
    }

    public EventDAO getEventDAO() {
        return eventDAO;
    }

    public AuthTokenDAO getAuthTokenDAO() {
        return authTokenDAO;
    }
}


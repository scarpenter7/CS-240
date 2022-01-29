package service;

import dao.DataAccessException;
import dao.Database;
import model.AuthToken;
import model.Person;
import request.PersonRequest;
import result.PersonsResult;

import java.util.ArrayList;

/** Returns the single Person object with the specified ID.
 *
 */
public class PersonService {
    private PersonRequest request;

    /** Constructor
     *
     * @param request containing authToken and personID
     */
    public PersonService(PersonRequest request) {
        this.request = request;
    }

    /** Featured function of the Person Service
     * finds the person
     * @return a person inside a Person Response object or a message of failure upon failure
     */
    public PersonsResult person() {
        PersonsResult personsResult = null;
        try {
            Database db = new Database();
            String authTokenName = request.getAuthToken();
            AuthToken authToken = db.getAuthTokenDAO().find(authTokenName);
            if (authToken == null) {
                personsResult = new PersonsResult("error: invalid authToken");
                db.closeConnection(false);
                return personsResult;
            }

            String userName = authToken.getAssociatedUsername();
            String personID = request.getPersonID();
            if (personID == null) {
                personsResult = people(userName, db);
            }
            else {
                personsResult = onePerson(userName, personID, db);
            }
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
        }
        return personsResult;
    }

    private PersonsResult people(String userName, Database db) {
        ArrayList<Person> people = null;
        try {
            people = db.getPersonDAO().findAllAssociated(userName);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new PersonsResult(people);
    }

    private PersonsResult onePerson(String userName, String personID, Database db) {
        Person person = null;
        try {
            person = db.getPersonDAO().find(personID);
            if (person == null) {
                return new PersonsResult("error: invalid personID parameter");
            }
            if (!person.getAssociatedUsername().equals(userName)) {
                return new PersonsResult("error: requested person does not belong to this user");
            }
        }
        catch (DataAccessException e) {
            e.printStackTrace();
        }
        return new PersonsResult(person);
    }
}

package service;

import dao.*;
import model.Event;
import model.Person;
import model.User;
import request.LoadRequest;
import result.LoadResult;

/** Clears all data from the database (just like the /clear API).
 * Then, it loads the posted user, person, and event data into the database.
 */
public class LoadService {
    private LoadRequest request;

    /** Constructor
     * @param request Takes in this package of parameters, see LoadRequest for more info
     */
    public LoadService(LoadRequest request) {
        this.request = request;
    }

    /** Featured function of the Load Service
     * Clears all data from the database (just like the /clear API).
     * Then, it loads the posted user, person, and event data into the database.
     * @return response of success or failure
     */
    public LoadResult load(boolean commit) {
        LoadResult loadResult = null;
        try {
            Database db = new Database();
            db.clearTables();

            User[] users = request.getUsers();
            Person[] people = request.getPersons();
            Event[] events = request.getEvents();
            if (!(insertUsers(users, db) && insertPeople(people, db) && insertEvents(events, db))) {
                db.closeConnection(false);
                loadResult = new LoadResult("error: Invalid request data", false);
                return loadResult;
            }
            db.closeConnection(commit);
            String message = generateMessage(users.length, people.length, events.length);
            loadResult = new LoadResult(message,true);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
        }
        return loadResult;
    }

    private boolean insertUsers(User[] users, Database db) {
        UserDAO uDAO = db.getUserDAO();
        for (User user: users) {
            try {
                if (checkValidUser(user)) {
                    uDAO.insert(user);
                }
                else {
                    return false;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private boolean checkValidUser(User user) {
        if (user.getUserName() == null || user.getPassword() == null ||
                user.getPersonID() == null || user.getFirstName() == null ||
                user.getLastName() == null || user.getEmail() == null ||
                user.getGender() == null) {
            return false;
        }
        if (user.getUserName().equals("") || user.getPassword().equals("") ||
            user.getPersonID().equals("") || user.getFirstName().equals("") ||
            user.getLastName().equals("") || user.getEmail().equals("")) {
            return false;
        }
        if (!(user.getGender().equals("m") || user.getGender().equals("f"))) {
            return false;
        }
        return true;
    }

    private boolean insertPeople(Person[] people, Database db) {
        PersonDAO pDAO = db.getPersonDAO();
        for (Person person: people) {
            try {
                if (checkValidPerson(person)) {
                    pDAO.insert(person);
                }
                else {
                    return false;
                }
            }
            catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    private boolean checkValidPerson(Person person) {
        if (person.getPersonID() == null || person.getFirstName() == null ||
            person.getLastName() == null || person.getGender() == null) {
            return false;
        }
        if ((person.getAssociatedUsername() != null && person.getAssociatedUsername().equals("")) || person.getPersonID().equals("") ||
            person.getFirstName().equals("") || person.getLastName().equals("")) {
            return false;
        }
        if (!(person.getGender().equals("m") || person.getGender().equals("f"))) {
            return false;
        }
        return true;
    }

    private boolean insertEvents(Event[] events, Database db) {
        EventDAO eDAO = db.getEventDAO();
        for (Event event: events) {
            try {
                if (checkValidEvent(event)) {
                    eDAO.insert(event);
                }
                else {
                    return false;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private boolean checkValidEvent(Event event) {
        if (event.getAssociatedUsername() == null || event.getEventID() == null ||
                event.getCity() == null || event.getCountry() == null ||
                event.getEventType() == null) {
            return false;
        }
        if (event.getAssociatedUsername().equals("") || event.getEventID().equals("") ||
            event.getCity().equals("") || event.getCountry().equals("") ||
            event.getEventType().equals("")) {
            return false;
        }
        return true;
    }

    private String generateMessage(int numUsers, int numPeople, int numEvents) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Successfully added ");
        messageBuilder.append(numUsers);
        messageBuilder.append(" users, ");
        messageBuilder.append(numPeople);
        messageBuilder.append(" persons, and ");
        messageBuilder.append(numEvents);
        messageBuilder.append(" events to the database.");
        return messageBuilder.toString();
    }
}

package service;

import dao.DataAccessException;
import dao.Database;
import dataJson.PeopleData;
import model.AuthToken;
import model.Person;
import model.User;
import request.RegisterRequest;
import result.RegisterResult;

import java.io.IOException;

/** Creates a new user account, generates 4 generations of ancestor data for the new user, logs the user in,
 * and returns an auth token.
 */
public class RegisterService {
    private RegisterRequest request;

    /** Constructor
     *
     * @param request contains the account registration info
     */
    public RegisterService(RegisterRequest request) {
        this.request = request;
    }

    /** Featured Function of the Register Service
     * Generates 4 generations of ancestor data for the new user
     * @return an authToken, username, personID or a message of failure upon failure
     */
    public RegisterResult register(boolean commit) throws IOException {
        try {
            Database db = new Database();
            User newUser = createUser(db);
            RegisterResult rr = checkValidUser(db, newUser);
            if (rr != null) {
                //then the user was invalid
                return rr;
            } //else the user is valid
            db.getUserDAO().insert(newUser);
            PeopleData pplData = new PeopleData();
            try {
                Person userPerson = pplData.generateUserPerson(db, newUser);
                db.fillGenerations(4, newUser.getUserName(), userPerson);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            String authTokenName = db.generateUniqueAuthTokenName();
            AuthToken authToken = new AuthToken(authTokenName, newUser.getUserName());
            db.getAuthTokenDAO().insert(authToken);
            db.closeConnection(commit);
            return new RegisterResult(authTokenName, newUser.getUserName(), newUser.getPersonID());
        }
        catch (DataAccessException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        throw new IOException();
    }

    private User createUser(Database db) {
        String userName = request.getUserName();
        String personID;
        try {
            personID = db.generateUniquePersonID();
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            personID = "";
        }
        String password = request.getPassword();
        String gender = request.getGender();
        String email = request.getEmail();
        String firstName = request.getFirstName();
        String lastName = request.getLastName();

        return new User(userName,personID, gender, password, email, firstName, lastName);
    }

    private RegisterResult checkValidUser(Database db, User user) {
        String userName = user.getUserName();
        try {
            if (db.getUserDAO().find(userName) != null) {
                db.closeConnection(false);
                return new RegisterResult("userName already taken error!");
            }
            if (userName.equals("")) {
                db.closeConnection(false);
                return new RegisterResult("Missing userName error!");
            }
            String password = user.getPassword();
            if (password.equals("")) {
                db.closeConnection(false);
                return new RegisterResult("Missing password error!");
            }
            String email = user.getEmail();
            if (email.equals("") || !email.contains("@")) {
                db.closeConnection(false);
                return new RegisterResult("Invalid or missing email error!");
            }
            String firstName = user.getFirstName();
            if (firstName.equals("")) {
                db.closeConnection(false);
                return new RegisterResult("Missing firstName error!");
            }
            String lastName = user.getLastName();
            if (lastName.equals("")) {
                db.closeConnection(false);
                return new RegisterResult("Missing lastName error!");
            }
            String gender = user.getGender();
            if (!(gender.equals("m") || gender.equals("f"))) {
                db.closeConnection(false);
                return new RegisterResult("Invalid or missing gender error!");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

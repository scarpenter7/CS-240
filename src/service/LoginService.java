package service;

import dao.AuthTokenDAO;
import dao.DataAccessException;
import dao.Database;
import dao.UserDAO;
import model.AuthToken;
import model.User;
import request.LoginRequest;
import result.LoginResult;

import java.io.IOException;

/** Logs in the user and returns an auth token.
 *
 */
public class LoginService {
    private LoginRequest request;

    /** Constructor
     * @param request containing the login information (username and password etc.)
     */
    public LoginService(LoginRequest request) {
        this.request = request;
    }

    /** Featured function of the Login Service
     * @return a response containing an authToken, userName, and personID upon success
     * or a message of failure upon failure.
     */
    public LoginResult login(boolean commit) throws IOException {
        try {
            Database db = new Database();
            UserDAO userDAO = db.getUserDAO();
            String username = request.getUsername();
            String password = request.getPassword();
            User user = userDAO.find(username);
            if (user == null) {
                //that's bad, username doesn't exist
                db.closeConnection(false);
                return new LoginResult("Invalid userName error!");
            }
            else if (!user.getPassword().equals(password)) {
                //that's bad, wrong password
                db.closeConnection(false);
                return new LoginResult("Incorrect password error!");
            }
            else { //successful login
                String authTokenName = db.generateUniqueAuthTokenName();
                AuthToken authToken = new AuthToken(authTokenName, username);
                AuthTokenDAO authTokenDAO = new AuthTokenDAO(db.getConnection());
                authTokenDAO.insert(authToken);
                db.closeConnection(commit);
                return new LoginResult(authTokenName, username, user.getPersonID());
            }
        }
        catch (DataAccessException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        throw new IOException();
    }
}

package result;

import model.AuthToken;

/** Output of a Login Service
 *
 */
public class LoginResult {
    private String authToken;
    private String userName;
    private String personID;
    private String message;
    private boolean success;

    /** Success Constructor
     *
     * @param authToken authToken given from the login
     * @param username username used
     * @param personID corresponding personID
     */
    public LoginResult(String authToken, String username, String personID) {
        this.authToken = authToken;
        this.userName = username;
        this.personID = personID;
        success = true;
    }

    public LoginResult(String message) { //fail constructor
        success = false;
        this.message = message;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

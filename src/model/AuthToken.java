package model;

import java.util.Objects;

/** A token given a to the user upon each successful login
 *  This token is required for use of several services
 */
public class AuthToken {
    private String authToken;
    private String userName;

    /** Constructor
     *
     * @param authToken unique name of the token
     * @param userName username linked to the authToken
     */
    public AuthToken(String authToken, String userName) {
        this.authToken = authToken;
        this.userName = userName;
    }


    public String getAuthTokenName() {
        return authToken;
    }

    public void setAuthTokenName(String authTokenName) {
        this.authToken = authTokenName;
    }

    public String getAssociatedUsername() {
        return userName;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.userName = associatedUsername;
    }

    /** Check if two tokens are equal
     *
     * @param o the second token being compared
     * @return boolean for whether or not they are the same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken = (AuthToken) o;
        return Objects.equals(getAuthTokenName(), authToken.getAuthTokenName()) &&
                Objects.equals(userName, authToken.userName);
    }
}

package request;

import java.io.IOException;

/** Package of parameters for the LoginService
 *
 */
public class LoginRequest {
    private String userName;
    private String password;

    public LoginRequest(String userName, String password) { //testing only
        this.userName = userName;
        this.password = password;
    }

    public String getUsername() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

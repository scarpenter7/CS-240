package request;

public class PersonRequest {
    private String personID;
    private String authToken;

    public PersonRequest(String personID, String authTokenName) {
        this.personID = personID;
        this.authToken = authTokenName;
    }

    public PersonRequest(String authTokenName) {
        this.authToken = authTokenName;
    }

    public String getPersonID() {
        return personID;
    }

    public String getAuthToken() {
        return authToken;
    }
}

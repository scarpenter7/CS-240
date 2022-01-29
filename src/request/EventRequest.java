package request;

public class EventRequest {
    private String eventID;
    private String authToken;

    public EventRequest(String eventID, String authToken) {
        this.eventID = eventID;
        this.authToken = authToken;
    }

    public EventRequest(String authToken) {
        this.authToken = authToken;
    }

    public String getEventID() {
        return eventID;
    }

    public String getAuthToken() {
        return authToken;
    }
}

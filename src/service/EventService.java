package service;

import dao.DataAccessException;
import dao.Database;
import model.AuthToken;
import model.Event;
import request.EventRequest;
import result.EventsResult;

import java.util.ArrayList;

/** Returns the single Event object with the specified ID
 *
 */
public class EventService {
    private EventRequest request;

    /** Constructor
     *
     * @param request containing authToken and eventID
     */
    public EventService(EventRequest request) {
        this.request = request;
    }

    /** Featured function of the Event Service
     *
     * @return event given by the eventID in the form of an EventResponse
     */
    public EventsResult event() {
        EventsResult eventsResult = null;
        try {
            Database db = new Database();
            String authTokenName = request.getAuthToken();
            AuthToken authToken = db.getAuthTokenDAO().find(authTokenName);
            if (authToken == null) {
                eventsResult = new EventsResult("error: invalid authToken");
                db.closeConnection(false);
                return eventsResult;
            }

            String userName = authToken.getAssociatedUsername();
            String eventID = request.getEventID();
            if (eventID == null) {
                eventsResult = events(userName, db);
            }
            else {
                eventsResult = oneEvent(userName, eventID, db);
            }
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
        }
        return eventsResult;
    }

    private EventsResult events(String userName, Database db) {
        ArrayList<Event> events = null;
        try {
            events = db.getEventDAO().findAllAssociated(userName);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new EventsResult(events);
    }

    private EventsResult oneEvent(String userName, String eventID, Database db) {
        Event event = null;
        try {
            event = db.getEventDAO().find(eventID);
            if (event == null) {
                return new EventsResult("error: invalid eventID parameter");
            }
            if (!event.getAssociatedUsername().equals(userName)) {
                return new EventsResult("error: requested event does not belong to this user");
            }
        }
        catch (DataAccessException e) {
            e.printStackTrace();
        }
        return new EventsResult(event);
    }
}

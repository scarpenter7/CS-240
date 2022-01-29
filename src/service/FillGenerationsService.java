package service;

import dao.DataAccessException;
import dao.Database;
import model.Person;
import model.User;
import request.FillGenerationsRequest;
import result.FillGenerationsResult;

/** Fills generations for a registered user
 * Generates a default of 4 generations but can be specified differently
 * Deletes any data associated with this username
 */
public class FillGenerationsService {
    private FillGenerationsRequest request;
    /** Constructor
     *
     */
    public FillGenerationsService(FillGenerationsRequest request) { this.request = request;}

    /**Featured function of Fill Generations Service
     * @return a response of success or failure
     */
    public FillGenerationsResult fillGenerations(boolean commit) { //only used in the fillGenerations service
        FillGenerationsResult response = null;
        try {
            Database db = new Database();
            int numGenerations = request.getGenerations();
            String userName = request.getUsername();
            User user = db.getUserDAO().find(userName);
            if (user == null) {
                response = new FillGenerationsResult(false, "error: Invalid username");
                db.closeConnection(false);
                return response;
            }
            String userPersonID = user.getPersonID();
            Person userPerson = db.getPersonDAO().find(userPersonID);
            //Clear everything except the userPerson and his/her bday
            db.getPersonDAO().clear(userName, userPersonID);
            db.getEventDAO().clear(userName, userPersonID);
            final int USER_PERSON = 1;
            final int BIRTH_MARRIAGE_DEATH_EVENTS = 3;
            final int USER_MARRIAGE_DEATH_EVENTS = 2;
            int numPeopleGenerated = db.fillGenerations(numGenerations, userName, userPerson) + USER_PERSON;
            int numEventsGenerated = numPeopleGenerated * BIRTH_MARRIAGE_DEATH_EVENTS - USER_MARRIAGE_DEATH_EVENTS;
            db.closeConnection(commit);
            String responseMessage = generateResponseMessage(numPeopleGenerated, numEventsGenerated);
            response = new FillGenerationsResult(true, responseMessage);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
        }
        return response;
    }

    private String generateResponseMessage(int numPersons, int numEvents) {
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("Successfully added ");
        responseBuilder.append(numPersons);
        responseBuilder.append(" persons and ");
        responseBuilder.append(numEvents);
        responseBuilder.append(" events to the database.");
        return responseBuilder.toString();
    }
}

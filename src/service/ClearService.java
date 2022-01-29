package service;

import dao.DataAccessException;
import dao.Database;
import result.ClearResult;

/** Deletes ALL data from the database, including user accounts, auth tokens, and generated person and event data.
 *
 */
public class ClearService {
    /** Constructor
     *
     */
    public ClearService() {}

    /** The featured function of the clear service
     *
     * @return the response after operation
     */
    public ClearResult clear(boolean commit) {
        try {
            Database db = new Database();
            db.clearTables();
            db.closeConnection(commit);
            return new ClearResult(true, "Clear succeeded.");
        }
        catch (DataAccessException e) {
            e.printStackTrace();
        }
        return new ClearResult(false, "Internal service error");
    }
}

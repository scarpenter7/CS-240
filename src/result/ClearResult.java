package result;

/** The output of a Clear Service command
 *
 */
public class ClearResult {
    private String message;
    private boolean success;

    /** Constructor
     *
     * @param success determines the message to be displayed (pass or fail message)
     */
    public ClearResult(boolean success, String message) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }
}

package result;

/** Output of a Load Service
 *
 */
public class LoadResult {
    private String message;
    private boolean success;

    /** Constructor
     *
     * @param success determines the message to be displayed (pass or fail message)
     */
    public LoadResult(String message, boolean success) {
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

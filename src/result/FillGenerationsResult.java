package result;

/** Output of a FillGenerations Service
 *
 */
public class FillGenerationsResult {
    private String message;
    private boolean success;

    /** Constructor
     * Any amount of generations displayed
     * @param success determines the message that will be displayed (pass or fail message)
     * @param message description of the result of the method
     */
    public FillGenerationsResult(boolean success, String message) {
        this.success = success;
        this.message  = message;
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

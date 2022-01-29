package dao;

/** Exception for Database access errors
 *
 */
public class DataAccessException extends Exception {
    /** Exception for any type of Database access error
     * @param message will display this message
     */

    DataAccessException(String message)
    {
        super(message);
    }

    /** Exception for any type of Database access error
     */
    DataAccessException()
    {
        super();
    }
}
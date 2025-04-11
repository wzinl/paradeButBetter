package parade.exceptions;

/**
 * Exception thrown when an invalid or unsupported selection is made during a player's turn.
 */
public class SelectionException extends Exception {

    /**
     * Constructs a new SelectionException with the specified error message.
     *
     * @param message the detail message explaining the nature of the selection error
     */
    public SelectionException(String message) {
        super(message);
    }
}

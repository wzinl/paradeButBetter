package parade.exceptions;

/**
 * Thrown when a player attempts to play a card that does not meet the game's conditions.
 * 
 * This unchecked exception is used to signal that a card play is invalid,
 * for example due to rules about color, value, or board state.
 */
public class InvalidCardException extends RuntimeException {

    /**
     * Constructs a new InvalidCardException with a specified detail message.
     *
     * @param message the detail message explaining why the card is considered invalid
     */
    public InvalidCardException(String message) {
        super(message);
    }
}

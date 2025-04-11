package parade.helpers.inputTypes;

/**
 * Represents an action-based input during a player's turn,
 * This input is captured as a single character corresponding to the chosen action.
 */
public final class ActionInput implements SelectionInput {

    /** The character representing the chosen action (e.g., 'E' for End Turn). */
    private final char actionChar;

    /**
     * Constructs an ActionInput with the given action character.
     *
     * @param actionChar the character representing the action
     */
    public ActionInput(char actionChar) {
        this.actionChar = actionChar;
    }

    /**
     * Returns the character that corresponds to the action selected by the player.
     *
     * @return the action character
     */
    public char getActionChar() {
        return actionChar;
    }
}

package parade.models.selections;

/**
 * Represents a player-selected card action to be executed during a turn.
 * Wraps a {@link Runnable} that handles logic such as removing the card
 * from the player's hand and placing it on the board.
 */
public final class CardSelection implements TurnSelection {

    /** The card action to execute when selected. */
    private final Runnable executable;

    /**
     * Constructs a CardSelection with the given action.
     *
     * @param action a {@link Runnable} containing the logic to execute
     */
    public CardSelection(Runnable action) {
        this.executable = action;
    }

    /**
     * Executes the wrapped card action.
     */
    @Override
    public void execute() {
        executable.run();
    }
}

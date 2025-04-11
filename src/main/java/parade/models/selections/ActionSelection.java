package parade.models.selections;

/**
 * Represents a player-selected action that can be executed during a turn.
 * Wraps a {@link Runnable} to perform actions like exiting the game,
 * switching input modes, or displaying other players' boards.
 */
public final class ActionSelection implements TurnSelection {

    /** The action to execute when selected. */
    private final Runnable executable;

    /**
     * Constructs an ActionSelection with the given action.
     *
     * @param action a {@link Runnable} containing the logic to execute
     */
    public ActionSelection(Runnable action) {
        this.executable = action;
    }

    /**
     * Executes the wrapped action.
     */
    @Override
    public void execute() {
        executable.run();
    }
}

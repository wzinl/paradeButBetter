package parade.models.selections;

/**
 * A sealed interface representing a selectable turn action in the Parade game.
 * This can either be a card selection ({@link CardSelection}) or a general
 * action selection ({@link ActionSelection}).
 * <p>
 * All turn selections must implement the {@code execute()} method,
 * which performs the desired action.
 * </p>
 */
public sealed interface TurnSelection permits ActionSelection, CardSelection {

    /**
     * Executes the logic associated with this turn selection.
     */
    void execute();
}

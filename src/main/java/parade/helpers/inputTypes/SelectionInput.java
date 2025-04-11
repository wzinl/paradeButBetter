package parade.helpers.inputTypes;

/**
 * A sealed interface representing a player's selection input during their turn.
 * It can either be a {@link CardInput} (to play a card) or an {@link ActionInput}
 * (to perform an auxiliary action like exiting or displaying the board).
 *
 * <p>This interface is sealed to only permit specific subtypes.</p>
 */
public sealed interface SelectionInput permits CardInput, ActionInput {
    // Marker interface for selection types.
}

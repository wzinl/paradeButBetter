package parade.helpers.inputTypes;

/**
 * Represents a card selection made by a player during their turn.
 * Stores the index of the selected card from the player's hand.
 */
public final class CardInput implements SelectionInput {

    /** The index of the selected card in the player's hand. */
    private final int cardIndex;

    /**
     * Constructs a CardInput with the specified card index.
     *
     * @param cardIndex the index of the selected card (zero-based)
     */
    public CardInput(int cardIndex) {
        this.cardIndex = cardIndex;
    }

    /**
     * Returns the index of the card selected by the player.
     *
     * @return the card index
     */
    public int getCardIndex() {
        return cardIndex;
    }
}

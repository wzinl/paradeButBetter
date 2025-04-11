package parade.models.cards;

import java.util.ArrayList;

import parade.exceptions.InvalidCardException;

/**
 * Represents a general interface for any collection of cards in the game,
 * such as a hand, board, or deck. Supports adding and removing cards.
 */
public interface CardCollection {

    /**
     * Adds a list of cards to the collection.
     *
     * @param cards the list of cards to add
     */
    void addCards(ArrayList<Card> cards);

    /**
     * Adds a single card to the collection.
     *
     * @param card the card to add
     */
    void addCard(Card card);

    /**
     * Removes the specified card from the collection.
     *
     * @param card the card to remove
     * @throws InvalidCardException if the card is not found or cannot be removed
     */
    void removeCard(Card card) throws InvalidCardException;
}

package parade.models.cards;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a deck of 66 face-up cards used in the game.
 * The deck includes cards of 6 different colors, each with values from 0 to 10.
 * Cards are shuffled upon initialization.
 */
public final class Deck {

    /** The full list of cards in the deck. */
    private final ArrayList<Card> deck;

    /** The six color names used across the game. */
    public static final String[] colours = {"Green", "Purple", "Red", "Blue", "Orange", "Grey"};

    /**
     * Constructs a new shuffled deck of 66 cards (6 colors × 11 values).
     */
    public Deck() {
        this.deck = new ArrayList<>();
        initialiseDeck();   // Populate the deck
        shuffle();          // Shuffle it
    }

    /**
     * Initializes the deck with 66 face-up cards, one for each value (0–10) per color.
     */
    private void initialiseDeck() {
        for (String color : colours) {
            for (int value = 0; value <= 10; value++) {
                Card card = new Card(value, color, true);
                deck.add(card);
            }
        }
    }

    /**
     * Shuffles the deck using {@link Collections#shuffle}.
     */
    public void shuffle() {
        Collections.shuffle(deck);
    }

    /**
     * Checks if the deck is empty.
     *
     * @return true if the deck has no more cards, false otherwise
     */
    public boolean isEmpty() {
        return deck.isEmpty();
    }

    /**
     * Gets the current number of cards remaining in the deck.
     *
     * @return the size of the deck
     */
    public int getDeckSize() {
        return deck.size();
    }

    /**
     * Returns a reference to the deck's underlying card list.
     * (Consider unmodifiable wrapper if needed.)
     *
     * @return the list of cards in the deck
     */
    public ArrayList<Card> getDeck() {
        return deck;
    }

    /**
     * Draws a card from the top of the deck (end of the list).
     * Returns null if the deck is empty.
     *
     * @return the drawn card, or null if empty
     */
    public Card drawCard() {
        return deck.isEmpty() ? null : deck.remove(deck.size() - 1);
    }
}

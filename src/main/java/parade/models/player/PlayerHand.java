package parade.models.player;

import java.util.ArrayList;
import java.util.List;

import parade.exceptions.InvalidCardException;
import parade.models.cards.Card;
import parade.models.cards.Deck;

/**
 * Represents a player's hand in the Parade game.
 * A hand holds up to a fixed number of cards and supports operations like drawing, removing, and displaying cards.
 */
public class PlayerHand {

    /** The maximum number of cards a player can hold in hand. */
    public static final int MAXHANDCOUNT = 5;

    /** The list of cards currently in the player's hand. */
    private final ArrayList<Card> cardList;

    /**
     * Constructs an empty PlayerHand.
     */
    public PlayerHand() {
        this.cardList = new ArrayList<>();
    }

    /**
     * Adds a card to the hand if it is not full.
     *
     * @param card the card to add
     * @throws IllegalStateException if the hand already has {@code MAXHANDCOUNT} cards
     */
    public void addCard(Card card) {
        if (cardList.size() < MAXHANDCOUNT) {
            cardList.add(card);
        } else {
            throw new IllegalStateException("Cannot add more cards. Hand is full.");
        }
    }

    /**
     * Removes a card from the hand.
     *
     * @param card the card to remove
     * @throws InvalidCardException if the card is not in the hand
     */
    public void removeCard(Card card) throws InvalidCardException {
        if (!cardList.contains(card)) {
            throw new InvalidCardException("Card not in hand!");
        }
        cardList.remove(card);
    }

    /**
     * Draws one card from the given deck and adds it to the hand.
     * Prints a message if the deck is empty.
     *
     * @param deck the deck to draw from
     */
    public void drawCard(Deck deck) {
        if (deck.isEmpty()) {
            System.out.println("Cannot draw, deck empty");
        } else {
            Card drawnCard = deck.getDeck().remove(deck.getDeckSize() - 1);
            addCard(drawnCard);
        }
    }

    /**
     * Initializes the player's hand by drawing 5 cards from the deck.
     *
     * @param deck the deck to draw from
     */
    public void initHand(Deck deck) {
        for (int i = 0; i < MAXHANDCOUNT; i++) {
            drawCard(deck);
        }
    }

    /**
     * Returns a visual string representation of the hand, with cards displayed horizontally.
     *
     * @return formatted string representing the hand
     */
    @Override
    public String toString() {
        List<String[]> cardLines = new ArrayList<>();
        List<String> colorCodes = new ArrayList<>();
        
        for (Card card : cardList) {
            colorCodes.add(card.getAnsiColorCode());
            cardLines.add(card.toString()
                        .replaceAll("\u001B\\[[;\\d]*m", "")
                        .split("\n"));
        }

        StringBuilder result = new StringBuilder();
        int linesPerCard = cardLines.get(0).length;

        for (int line = 0; line < linesPerCard; line++) {
            for (int i = 0; i < cardList.size(); i++) {
                result.append(colorCodes.get(i))
                      .append(cardLines.get(i)[line])
                      .append(Card.ANSI_RESET + "  ");
            }
            result.append("\n");
        }

        return result.toString();
    }

    /**
     * Returns the list of cards in the hand.
     *
     * @return the card list
     */
    public ArrayList<Card> getCardList() {
        return cardList;
    }

}

package parade.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import parade.models.cards.Card;
import parade.models.cards.Deck;

/**
 * Represents the Parade Board in the game, which holds a row of cards
 * and provides methods for displaying and manipulating them.
 */
public class ParadeBoard {

    /** The list of cards currently on the parade board. */
    private final ArrayList<Card> cardList;

    /**
     * Constructs a ParadeBoard by drawing the first 6 cards from the deck.
     *
     * @param deck the deck to draw cards from
     */
    public ParadeBoard(Deck deck) {
        this.cardList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            cardList.add(deck.drawCard());
        }
    }

    /**
     * Returns the number of cards currently on the parade board.
     *
     * @return the number of cards on the board
     */
    public int getNumberOfCards() {
        return cardList.size();
    }

    /**
     * Adds a card to the end of the parade board.
     *
     * @param card the card to be added
     */
    public void addToBoard(Card card) {
        cardList.add(card);
    }

    /**
     * Removes a specific card from the parade board.
     *
     * @param card the card to remove
     */
    public void remove(Card card) {
        cardList.remove(card);
    }

    /**
     * Returns the list of cards currently on the parade board.
     *
     * @return the list of cards on the board
     */
    public ArrayList<Card> getCardList() {
        return cardList;
    }

    /**
     * Generates a string representation of the board for display,
     * using compact formatting if there are more than 10 cards.
     *
     * @return the formatted string representation of the board
     */
    @Override
    public String toString() {
        boolean useCompact = cardList.size() > 10;

        List<String[]> cardLines = new ArrayList<>();
        List<String> colorCodes = new ArrayList<>();
        
        for (Card card : cardList) {
            colorCodes.add(card.getAnsiColorCode());
            String cardString = useCompact ? card.toString(true) : card.toString();
            cardLines.add(cardString
                        .replaceAll("\u001B\\[[;\\d]*m", "")
                        .split("\n"));
        }

        StringBuilder result = new StringBuilder();
        int linesPerCard = cardLines.get(0).length;

        for (int line = 0; line < linesPerCard; line++) {
            for (int i = 0; i < cardList.size(); i++) {
                result.append(colorCodes.get(i))
                      .append(cardLines.get(i)[line])
                      .append(Card.ANSI_RESET)
                      .append("  ");
            }
            result.append("\n");
        }

        return result.toString();
    }

    /**
     * Generates a detailed string representation of the board showing:
     * - Which cards will be removed
     * - Which cards are kept
     * - The chosen card
     *
     * This view is used during a player's turn when calculating card removal.
     *
     * @param toRemove    the list of cards that will be removed from the board
     * @param chosenCard  the card the player has chosen to play
     * @return the formatted string representation of the board with visual indicators
     */
    public String toString(ArrayList<Card> toRemove, Card chosenCard) {
        int safeCardCount = Math.min(chosenCard.getValue(), cardList.size());

        List<String[]> firstLine = new ArrayList<>();
        List<String[]> secondLine = new ArrayList<>();
        List<String> colorCodes = new ArrayList<>();
        boolean useCompact = cardList.size() > 10;

        for (Card card : cardList) {
            colorCodes.add(card.getAnsiColorCode());
            String cardString = useCompact ? card.toString(true) : card.toString();
            String[] cardLines = cardString
                                    .replaceAll("\u001B\\[[;\\d]*m", "")
                                    .split("\n");

            if (toRemove.contains(card)) {
                firstLine.add(cardLines);
                secondLine.add(null);
            } else {
                firstLine.add(null);
                secondLine.add(cardLines);
            }
        }

        String chosenCardString = useCompact ? chosenCard.toString(true) : chosenCard.toString();
        String[] chosenCardLines = chosenCardString
                                        .replaceAll("\u001B\\[[;\\d]*m", "")
                                        .split("\n");
        String chosenColor = chosenCard.getAnsiColorCode();

        StringBuilder result = new StringBuilder();
        int linesPerCard = useCompact ? 5 : 8;

        boolean hasCardsToRemove = firstLine.stream().anyMatch(Objects::nonNull);

        // First row (cards to remove)
        if (hasCardsToRemove) {
            for (int line = 0; line < linesPerCard; line++) {
                for (int i = 0; i < cardList.size() - safeCardCount; i++) {
                    if (firstLine.get(i) != null) {
                        result.append(colorCodes.get(i))
                              .append(firstLine.get(i)[line])
                              .append(Card.ANSI_RESET + "  ");
                    } else {
                        result.append(" ".repeat(secondLine.get(i)[line].length()))
                              .append("  ");
                    }
                }
                result.append("\n");
            }
            result.append("\n");
        }

        // Second row (kept cards + safe cards + chosen card)
        for (int line = 0; line < linesPerCard; line++) {
            for (int i = 0; i < cardList.size() - safeCardCount; i++) {
                if (secondLine.get(i) != null) {
                    result.append(colorCodes.get(i))
                          .append(secondLine.get(i)[line])
                          .append(Card.ANSI_RESET + "  ");
                } else {
                    result.append(" ".repeat(firstLine.get(i)[line].length()))
                          .append("  ");
                }
            }

            // Safe card separator
            if (safeCardCount > 0) {
                if (line == linesPerCard / 2) {
                    result.append(":  ");
                } else {
                    result.append("   ");
                }
            }

            // Safe cards
            for (int i = cardList.size() - safeCardCount; i < cardList.size(); i++) {
                result.append(colorCodes.get(i))
                      .append(secondLine.get(i)[line])
                      .append(Card.ANSI_RESET + "  ");
            }

            // Chosen card
            if (line == linesPerCard / 2) {
                result.append("|  ");
            } else {
                result.append("   ");
            }
            result.append(chosenColor)
                  .append(chosenCardLines[line])
                  .append(Card.ANSI_RESET + "\n");
        }

        return result.toString();
    }
}

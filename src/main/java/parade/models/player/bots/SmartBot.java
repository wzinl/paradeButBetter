package parade.models.player.bots;

import java.util.List;

import parade.models.ParadeBoard;
import parade.models.cards.Card;

/**
 * A smarter bot that chooses the card with the least number of kicks (removed cards)
 * based on a simulation of the parade rules.
 * Extends {@link Bot}.
 */
public class SmartBot extends Bot {

    /**
     * Constructs a SmartBot with the specified name.
     *
     * @param name the name of the bot
     */
    public SmartBot(String name) {
        super(name);
    }

    /**
     * Selects the best card to play based on minimizing the number of kicked cards.
     *
     * @param hand         the list of cards in the bot's hand
     * @param paradeBoard  the current state of the parade board
     * @return the index of the card that results in the least kicks
     */
    @Override
    public int getNextCardIndex(ParadeBoard paradeBoard) {
        List<Card> cardList = getPlayerHand().getCardList();
        int bestIndex = 0;
        int leastKicked = Integer.MAX_VALUE;

        for (int i = 0; i < cardList.size(); i++) {
            Card card = cardList.get(i);
            int kicked = simulateTurn(card, paradeBoard);
            if (kicked < leastKicked) {
                leastKicked = kicked;
                bestIndex = i;
            }
        }

        return bestIndex;
    }

    /**
     * Simulates how many cards would be "kicked" from the parade board
     * if the specified card were played, according to parade rules.
     *
     * @param chosenCard   the card being evaluated
     * @param paradeBoard  the current state of the parade board
     * @return the number of cards that would be removed
     */
    public static int simulateTurn(Card chosenCard, ParadeBoard paradeBoard) {
        int count = 0;
        int chosenValue = chosenCard.getValue();
        for (int i = 0; i < paradeBoard.getCardList().size() - chosenValue; i++) {
            Card current = paradeBoard.getCardList().get(i);
            if (current.getValue() <= chosenValue || current.getColor().equals(chosenCard.getColor())) {
                count++;
            }
        }
        return count;
    }
}

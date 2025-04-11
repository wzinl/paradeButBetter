package parade.models.player.bots;

import java.util.List;

import parade.models.ParadeBoard;
import parade.models.cards.Card;

/**
 * A more advanced bot that not only minimizes the number of kicked cards,
 * but also considers the total score of those cards to make better decisions.
 * Extends {@link Bot}.
 */
public class SmarterBot extends Bot {

    /**
     * Constructs a SmarterBot with the specified name.
     *
     * @param name the name of the bot
     */
    public SmarterBot(String name) {
        super(name);
    }

    /**
     * Chooses the best card to play by simulating each option and selecting
     * the one that results in the least number of kicks and lowest score impact.
     *
     * @param hand         the list of cards in the bot's hand
     * @param paradeBoard  the current state of the parade board
     * @return the index of the best card to play
     */
    @Override
    public int getNextCardIndex(ParadeBoard paradeBoard) {
        List<Card> cardList = getPlayerHand().getCardList();
        int bestIndex = 0;
        int leastKicked = Integer.MAX_VALUE;
        int lowestScore = Integer.MAX_VALUE;

        for (int i = 0; i < cardList.size(); i++) {
            Card card = cardList.get(i);
            int[] kicked = smarterSimulation(card, paradeBoard);
            if (kicked[0] < leastKicked && kicked[1] <= lowestScore) {
                leastKicked = kicked[0];
                lowestScore = kicked[1];
                bestIndex = i;
            }
        }

        return bestIndex;
    }

    /**
     * Simulates how many cards would be kicked and the total score of those cards
     * if the given card were played.
     *
     * @param chosenCard   the card to evaluate
     * @param paradeBoard  the current state of the parade board
     * @return an array of size 2: [number of kicked cards, total score of kicked cards]
     */
    public int[] smarterSimulation(Card chosenCard, ParadeBoard paradeBoard) {
        int count = 0;
        int score = 0;
        int chosenValue = chosenCard.getValue();

        for (int i = 0; i < paradeBoard.getCardList().size() - chosenValue; i++) {
            Card current = paradeBoard.getCardList().get(i);
            if (current.getValue() <= chosenValue || current.getColor().equals(chosenCard.getColor())) {
                count++;
                score += current.getValue();
            }
        }

        return new int[] {count, score};
    }
}

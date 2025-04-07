package main.models.player.bots;

import java.util.List;

import main.models.ParadeBoard;
import main.models.cards.Card;
import main.models.player.Player;
import main.models.player.PlayerHand;


public class SmarterBot extends Player implements Bot{

    public SmarterBot(String name) {
        super(name);
    }

    @Override
    public int getNextCardIndex(List<Card> hand, ParadeBoard paradeBoard) {
        int bestIndex = 0;
        int leastKicked = Integer.MAX_VALUE;
        int lowestScore = Integer.MAX_VALUE;

        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);
            int[] kicked = smarterSimulation(card, paradeBoard);
            if (kicked[0] < leastKicked && kicked[1] <= lowestScore) {
                leastKicked = kicked[0];
                lowestScore = kicked[1];
                bestIndex = i;
            }
        }

        return bestIndex;
    }

    
    @Override
    public int discardCardEndgame(PlayerHand hand, ParadeBoard paradeBoard){
        List<Card> list = hand.getCardList();
        int bestIndex = 0;
        int leastKicked = Integer.MAX_VALUE;
        int lowestScore = Integer.MAX_VALUE;

        for (int i = 0; i < list.size(); i++) {
            Card card = list.get(i);
            int[] kicked = smarterSimulation(card, paradeBoard);
            if (kicked[0] < leastKicked && kicked[1] <= lowestScore) {
                leastKicked = kicked[0];
                lowestScore = kicked[1];
                bestIndex = i;
            }
        }

        return bestIndex;
    }

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
        int[] ret = {count, score};
        return ret;
    }
}
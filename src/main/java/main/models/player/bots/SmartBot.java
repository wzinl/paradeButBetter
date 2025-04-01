package main.models.player.bots;

import java.io.Serializable;
import java.util.List;
import main.helpers.CardEffects;
import main.models.ParadeBoard;
import main.models.cards.Card;
import main.models.player.Player;

public class SmartBot extends Player implements Serializable{

    public SmartBot(String name) {
        super(name);
    }

    public int getNextCardIndex(List<Card> hand, ParadeBoard paradeBoard) {
        int bestIndex = 0;
        int leastKicked = Integer.MAX_VALUE;

        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);
            int kicked = CardEffects.simulate(card, paradeBoard);
            if (kicked < leastKicked) {
                leastKicked = kicked;
                bestIndex = i;
            }
        }

        return bestIndex+1;
    }
}

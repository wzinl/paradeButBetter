package main.models.player.bots;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import main.models.cards.Card;
import main.models.player.Player;

public class RandomBot extends Player implements Serializable{

    public RandomBot(String name) {
        super(name);
    }

    public int getNextCardIndex(List<Card> hand) {
        return new Random().nextInt(1,hand.size()+1);
    }
}

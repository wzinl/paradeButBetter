package main.models.player.bots;

import java.util.List;
import java.util.Random;

import main.models.cards.Card;
import main.models.player.Player;

public class RandomBot extends Player {

    public RandomBot(String name) {
        super(name);
    }

    public int getNextCardIndex(List<Card> hand) {
        return new Random().nextInt(hand.size());
    }
}

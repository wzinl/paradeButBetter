package main.models.player.bots;

import java.util.List;
import java.util.Random;

import main.models.ParadeBoard;
import main.models.cards.Card;
import main.models.player.Player;


public class RandomBot extends Player implements Bot{

    public RandomBot(String name) {
        super(name);
    }

    @Override
    public int getNextCardIndex(List<Card> hand, ParadeBoard paradeBoard) {
        return new Random().nextInt(hand.size());
    }
}

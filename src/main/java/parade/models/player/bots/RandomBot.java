package parade.models.player.bots;

import java.util.List;
import java.util.Random;

import parade.models.ParadeBoard;
import parade.models.cards.Card;
import parade.models.player.Player;
import parade.models.player.PlayerHand;


public class RandomBot extends Player implements Bot{

    public RandomBot(String name) {
        super(name);
    }

    @Override
    public int getNextCardIndex(List<Card> hand, ParadeBoard paradeBoard) {
        return new Random().nextInt(hand.size());
    }

    @Override
    public int discardCardEndgame(PlayerHand hand, ParadeBoard paradeBoard){
        List<Card> list = hand.getCardList();
        int min = 0;
        int max = list.size()-1;

        Random rand = new Random();
        int res = rand.nextInt(max - min + 1) + min;
        return res;
    }
}

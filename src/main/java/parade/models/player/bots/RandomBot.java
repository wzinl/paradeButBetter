package parade.models.player.bots;

import java.util.List;
import java.util.Random;

import parade.models.ParadeBoard;
import parade.models.cards.Card;
import parade.models.player.Player;

/**
 * A simple bot that chooses moves randomly from its available cards.
 * Implements the {@link Bot} interface and extends {@link Player}.
 */
public class RandomBot extends Bot {

    /**
     * Constructs a RandomBot with the given name.
     *
     * @param name the bot's display name
     */
    public RandomBot(String name) {
        super(name);
    }

    /**
     * Selects a random card index from the bot's hand to play.
     *
     * @param hand         the list of cards in the bot's hand
     * @param paradeBoard  the current state of the parade board
     * @return a random valid card index from the hand
     */
    @Override
    public int getNextCardIndex(ParadeBoard paradeBoard ) {
        List<Card> cardList = this.getPlayerHand().getCardList();
        return new Random().nextInt(cardList.size());
    }

    /**
     * Selects a random card index from the bot's hand to discard during the endgame phase.
     *
     * @param hand         the bot's hand
     * @param paradeBoard  the current state of the parade board
     * @return a random index of a card to discard
     */
    @Override
    public int discardCardEndgame(List<Player> playerList){
        List<Card> cardList = this.getPlayerHand().getCardList();
        int min = 0;
        int max = cardList.size() - 1;

        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min;
    }
}

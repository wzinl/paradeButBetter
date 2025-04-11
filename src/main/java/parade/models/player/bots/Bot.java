package parade.models.player.bots;

import java.util.List;

import parade.models.ParadeBoard;
import parade.models.cards.Card;
import parade.models.player.PlayerHand;

/**
 * Interface representing a bot (automated player) in the Parade game.
 * Defines methods that bots must implement to decide on their moves during
 * both the main game and the endgame discard phase.
 */
public interface Bot {

    /**
     * Determines the index of the next card the bot will play from its hand.
     *
     * @param hand         the list of cards currently in the bot's hand
     * @param paradeBoard  the current state of the parade board
     * @return the index of the card to be played
     */
    int getNextCardIndex(List<Card> hand, ParadeBoard paradeBoard);

    /**
     * Determines which card index the bot will discard during the endgame discard phase.
     *
     * @param hand         the bot's hand (wrapped in {@link PlayerHand})
     * @param paradeBoard  the current state of the parade board
     * @return the index of the card to discard
     */
    int discardCardEndgame(PlayerHand hand, ParadeBoard paradeBoard);
}

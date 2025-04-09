package parade.models.player.bots;

import java.util.List;

import parade.models.ParadeBoard;
import parade.models.cards.Card;
import parade.models.player.PlayerHand;

public interface Bot {
    public int getNextCardIndex(List<Card> hand, ParadeBoard paradeBoard);
    public int discardCardEndgame(PlayerHand hand, ParadeBoard paradeBoard);
}

package main.models.player.bots;

import java.util.List;

import main.models.ParadeBoard;
import main.models.cards.Card;
import main.models.player.PlayerHand;

public interface Bot {
    public int getNextCardIndex(List<Card> hand, ParadeBoard paradeBoard);
    public int discardCardEndgame(PlayerHand hand, ParadeBoard paradeBoard);
}

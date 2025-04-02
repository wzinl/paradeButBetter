package main.models.player.bots;

import java.util.List;

import main.models.ParadeBoard;
import main.models.cards.Card;

public interface Bot {
    public int getNextCardIndex(List<Card> hand, ParadeBoard paradeBoard);
}

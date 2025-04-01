package main.models.cards;

import java.util.ArrayList;
import main.exceptions.InvalidCardException;

public interface CardCollection {
    void addCards(ArrayList<Card> Cards);
    void addCard(Card Card);
    void removeCard(Card card) throws InvalidCardException;

}

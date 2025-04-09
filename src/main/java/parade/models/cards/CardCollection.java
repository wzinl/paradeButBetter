package parade.models.cards;

import java.util.ArrayList;

import parade.exceptions.InvalidCardException;

public interface CardCollection {
    void addCards(ArrayList<Card> Cards);
    void addCard(Card Card);
    void removeCard(Card card) throws InvalidCardException;

}

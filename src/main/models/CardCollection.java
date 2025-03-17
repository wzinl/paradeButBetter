package main.models;

import java.util.ArrayList;
import main.error.InvalidCardException;

public interface CardCollection {
    void addCards(ArrayList<Card> Cards);
    void addCard(Card Card);
    void removeCard(Card card) throws InvalidCardException;

}

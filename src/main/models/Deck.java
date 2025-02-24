

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> deck;

    public Deck() {
        this.deck = new ArrayList<Card>();
        initialiseDeck();
        
    }

    //initialising the deck with 66 cards
    private void initialiseDeck() {
        String[] colors = {"Green", "Purple", "Red", "Blue", "Orange", "Grey"};
        for (String color : colors) {
            for (int value = 0; value <= 10; value++) {
                Card card = new Card(value, color, false);
                deck.add(card);
            }
        }
    }
    //shuffle
    public void shuffle() {
        Collections.shuffle(deck);
    }

    public boolean isDeckEmpty () {
        return deck.isEmpty();
    }

    public int getDeckSize() {
        return deck.size();
    }

    public ArrayList<Card> getDeck() { 
        return deck;
    }
    public Card drawCard() {
        return deck.isEmpty() ? null : deck.remove(deck.size() - 1);
    }
    @Override
    public String toString() {
        return deck.toString();
    }
}

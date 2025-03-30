package main.models.cards;

import java.util.ArrayList;
import java.util.Collections;

public final class Deck {
    private final ArrayList<Card> deck;  // unmodifiable after initialisation    
    public static final String[] colours = {"Green", "Purple", "Red", "Blue", "Orange", "Grey"};

    public Deck() {
        this.deck = new ArrayList<>();
        initialiseDeck();   // initialise the deck of cards
        shuffle();  // shuffle the cards 
    }

    //initialising the deck with 66 cards
    private void initialiseDeck() {
        for (String color : colours) {
            for (int value = 0; value <= 10; value++) {
                Card card = new Card(value, color, true);
                deck.add(card);
            }
        }
    }

    //shuffle
    public void shuffle() {
        Collections.shuffle(deck);
    }

    //check for isEmpty => true, Last Round
    public boolean isEmpty() {
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

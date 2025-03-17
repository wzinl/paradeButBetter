package main.models;

import java.util.ArrayList;
import main.error.InvalidCardException;

public class PlayerHand {
    private ArrayList<Card> cardList;
    //to double check
    public static final int MAXHANDCOUNT = 5; 

    public PlayerHand(){
        this.cardList = new ArrayList<>();  // in GameManager class need to draw card from the deck and add cards to empty hand
    }

    // this method not needed right...
    public void addCard(Card card){
        if (cardList.size() < MAXHANDCOUNT){
            cardList.add(card);
        } else {
            throw new IllegalStateException("Cannot add more cards. Hand is full.");
        }
    }

    // Implements removeCard from CardCollection interface
    
    public void removeCard(Card card) throws InvalidCardException{
        // If hand does not have that card
        if (!cardList.contains(card)){
            throw new InvalidCardException("Card not in hand!");

        }
        // else remove card from hand
        cardList.remove(card);

    }
    //from the board

    // can do custom error to make it final round if deck is empty?
    public void drawCard (Deck deck){
        //first, check if drawing a card is possible
        if (deck.isEmpty()) {
            System.out.println("Cannot draw, deck empty");
            
        } else {
            // if not, draw from the top of the deck
            Card drawnCard = deck.getDeck().remove(deck.getDeckSize() - 1); 
            
            // Add the drawn card to the player's hand
            addCard(drawnCard);
        }
    }

    public void initHand(Deck deck){
        for(int i = 0; i < 5; i++){
            drawCard(deck);
        }
    }



    public void playCardFromHand(Card card, ParadeBoard paradeBoard) {
        //TODO WHY IS THIS NOT DONE
    }

    @Override
    public String toString() {
        String result = "";
        for (Card card : cardList) {
            result  += card + "  ";
        }
        return result.trim();
    }



    public ArrayList<Card> getCardList(){
        return cardList;
    }
}
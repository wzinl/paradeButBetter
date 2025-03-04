package main.models;

import java.util.ArrayList;

public class PlayerHand {
    private ArrayList<Card> cardList;
    //to double check
    public static final int MAXHANDCOUNT = 5; //int?

    public PlayerHand(ArrayList<Card> cardList){
        this.cardList = cardList;  
    }

    //from the board

    // can do custom error to make it final round if deck is empty?
    public Card drawCard (Deck deck){
        //first, check if drawing a card is possible
        if (deck.isDeckEmpty()) {
            System.out.println("Cannot draw, deck empty");
            return null;
        } else {
            // if not, draw from the top
            Card drawnCard = deck.getDeck().remove(deck.getDeckSize() - 1); 
            return drawnCard;
        }
    }

    //  Remove a certain card from hand 
    public void removeCardFromHand(Card card){
        cardList.remove(card);
    }

    //  Place a card from hand to the end of the parade 
    public void playCardFromHand (Card card , ParadeBoard parade) {
        removeCardFromHand(card);   
        parade.addToBoard(card);    
    }

    public ArrayList<Card> displayHand(){
        return cardList;
    }

}

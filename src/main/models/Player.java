package main.models;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String playerName;
    private int playerID;
    private int playerScore;
    private List<Card> playerCards;
    private PlayerBoard playerBoard;
    // HashMap<Character, ArrayList<Card>> playerCollectedCards; // ????? 

    public Player (String playerName, int playerID) {
        this.playerName = playerName;
        this.playerCards = new ArrayList<>();
        this.playerID = playerID;
        
        
    }

    public String getPlayerName () {
        return playerName;
    }
    
    public int getPlayerScore () {
        return playerScore;
    }

    public List<Card> getPlayerCards() {
        return playerCards;
    }

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    public int calculateScore() { //calculates and sets player score

        if (playerBoard == null || playerBoard.getPlayerBoard() == null) {
            return 0; // No board means no score
        }
        
        playerScore = 0;//so that playerScore does not add up if calculateScore is called multiple times
        //check through each value per color

        //loop to iterate through each Arraylist of cards in playerBoard hashmap
        for (ArrayList<Card> playerCards : playerBoard.getPlayerBoard().values()) {
            if (playerCards == null) {
                continue; //skip through (if any) null values
            }
            //loop to iterate through each card in the arraylist of cards
            for (Card card : playerCards) {
                if (card.getIsFaceUp()) { //adds direct value of card if card is face up
                    playerScore += card.getValue();
                } else { //if card is face down, just add one score instead
                    playerScore += 1;
                }
            }
        }

        return playerScore;

    }
}

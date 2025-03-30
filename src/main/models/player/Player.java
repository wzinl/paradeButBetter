package main.models.player;

import java.util.ArrayList;
import java.util.UUID;

import main.models.cards.Card;

public class Player {
    private String playerID;
    static int current_id = 0;
    private String playerName;
    private int playerScore;
    private PlayerHand playerhand;
    private PlayerBoard playerBoard;

    
    // HashMap<Character, ArrayList<Card>> playerCollectedCards; // ????? 

    public Player (String playerName) {
        this.playerName = playerName;
        playerID = UUID.randomUUID().toString();
        current_id++;
        this.playerhand = new PlayerHand();
        this.playerBoard = new PlayerBoard();
    }

    public String getPlayerName () {
        return playerName;
    }
    
    public String getPlayerID () {
        return playerID;
    }

    public int getPlayerScore () {
        return playerScore;
    }

    public PlayerHand getPlayerHand() { 
        return playerhand;
    }

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    public int calculateScore() { //calculates and sets player score

        if (playerBoard == null || playerBoard.getPlayerBoardHash() == null) {
            return 0; // No board means no score
        }
        
        playerScore = 0;//so that playerScore does not add up if calculateScore is called multiple times
        //check through each value per color
        // System.out.println(playerBoard);
        //loop to iterate through each Arraylist of cards in playerBoard hashmap
        for (ArrayList<Card> playerCards : playerBoard.getPlayerBoardHash().values()) {
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

    // check if player has collect all 6 colors
    public boolean hasCollectedAllColours() {
        return playerBoard.getPlayerBoardHash().keySet().size() == 6;
    }

}

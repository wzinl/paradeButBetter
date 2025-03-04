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

    
}

package main.gameStates;

import java.util.ArrayList;
import main.context.GameContext;
import main.models.Deck;
import main.models.ParadeBoard;
import main.models.Player;
import main.models.PlayerBoard;
import main.models.PlayerHand;

public abstract class GameState {
    protected final GameStateManager gsm;
    protected GameContext context;
    protected ArrayList<Player> playerList;
    protected int currentPlayerIndex;
    protected ParadeBoard paradeBoard;
    protected Deck deck;
    protected int finalPlayerIndex;

    // constructor for init state
    public GameState(GameStateManager gsm) { 
        this.gsm = gsm;
        
    }
    public GameState(GameStateManager gsm, GameContext context) {
        this.gsm = gsm;
        this.context = context;
        this.playerList = context.getPlayerList();
        this.currentPlayerIndex = context.getCurrentPlayerIndex();
        this.paradeBoard = context.getParadeBoard();
        this.deck = context.getDeck();
        this.finalPlayerIndex = context.getFinalRoundTriggerPlayerIndex();
    }

    // Method to set context when it's available (can be called later when context is initialized)
    public void setContext(GameContext context) {
        this.context = context;
        // Initialize state-dependent fields
        if (context != null) {
            this.playerList = context.getPlayerList();
            this.currentPlayerIndex = context.getCurrentPlayerIndex();
            this.paradeBoard = context.getParadeBoard();
            this.deck = context.getDeck();
        }
    }

    public abstract void enter();    // Called when the state is entered
    public abstract void exit();     // Called when switching to another state

    //Below are all the relevant display functions
    public String getDisplay(Player currentPlayer) {
        StringBuilder result = new StringBuilder();
        result.append("\u001B[0m"); // triggers ANSI processing
    
        result.append("Parade Board:\n");
        result.append(paradeBoard.toString()).append("\n\n");
    
        result.append("Here is your board:\n");
        result.append(getPlayerBoardDisplay(currentPlayer.getPlayerBoard()));
    
        result.append("Here is your hand:\n");
        result.append("\n"); // Insert extra newline to ensure the hand starts on a fresh line
        result.append(getHandDisplay(currentPlayer.getPlayerHand()));
    
        return result.toString();
    }
    
    public String getDisplay() {
        String result = "";

        // Display the parade board
        result += "Parade Board:\n";
        result += paradeBoard + "\n".repeat(3);

        for (Player curr : playerList) {
            result += curr.getPlayerName() + "'s board\n";
            result += getPlayerBoardDisplay(curr.getPlayerBoard());
            if (!curr.getPlayerHand().getCardList().isEmpty()) {
                result += curr.getPlayerName() + "'s hand\n";
                result += getHandDisplay(curr.getPlayerHand());
            }
        }

        result += "\n";
        return result;
    }

    public String getHandDisplay(PlayerHand playerHand) {
        String result = "";

        result += playerHand + "\n";
        return result;
    }

    public String getPlayerBoardDisplay(PlayerBoard currentplayerBoard) {
        String result = "";
        if (currentplayerBoard.isEmpty()) {
            System.out.println();
            result += "Playerboard is empty.\n\n";
        } else {
            result += currentplayerBoard + "\n\n";
        }
        return result;
    }
}
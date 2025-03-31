package main.gameStates;

import java.util.ArrayList;
import java.util.List;

import main.context.GameContext;
import main.models.*;
import main.models.cards.Card;
import main.models.cards.Deck;
import main.models.player.Player;
import main.models.player.PlayerBoard;
import main.models.player.PlayerHand;

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
        this.finalPlayerIndex = context.getFinalRoundStarterIndex();
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

    // For turnstate
    public String getDisplay(Player currentPlayer) {
        StringBuilder result = new StringBuilder();
        PlayerHand hand = currentPlayer.getPlayerHand();

        result.append("\u001B[0m"); // triggers ANSI processing
    
        result.append("Parade Board:\n");
        result.append(paradeBoard.toString()).append("\n\n");
    
        result.append("Here is your board:\n");
        result.append(getPlayerBoardDisplay(currentPlayer.getPlayerBoard()));
    
        result.append("Here is your hand:\n");
        result.append("\n");
        result.append(getHandDisplay(hand));
        result.append(getIndexString(hand));
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

        // Get all cards and their lines
        List<Card> cards = playerHand.getCardList();
        List<String[]> cardLines = new ArrayList<>();
        List<String> colorCodes = new ArrayList<>();
        
        for (Card card : cards) {
            // Store color code for each card
            colorCodes.add(card.getAnsiColorCode());
            // Store card lines (without color codes)
            cardLines.add(card.toString().replaceAll("\u001B\\[[;\\d]*m", "").split("\n"));
        }
        
        // Build output
        StringBuilder result = new StringBuilder();
        int linesPerCard = cardLines.get(0).length;
        
        for (int line = 0; line < linesPerCard; line++) {
            for (int i = 0; i < cards.size(); i++) {
                // Apply card's color, then line, then reset
                result.append(colorCodes.get(i))
                    .append(cardLines.get(i)[line])
                    .append("\u001B[0m ")
                    .append(" "); // Space between cards
            }
            result.append("\n");
        }
        
        return result.toString();
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

    public String getIndexString(PlayerHand playerHand) {
        ArrayList<Card> cardsList = playerHand.getCardList();   //  ArrayList of the cards on the player's hand
        StringBuilder index = new StringBuilder();  //  second line displays the indexes of each cards 
        
        for (int i = 0; i < cardsList.size(); i++) {
            Card currentCard = cardsList.get(i);  //  index (for users) start from 1, readjust by 1

            int cardLength = currentCard.length();

            String cardIndexStr = "{" + (i+1) + "}";
            int indexLength = cardIndexStr.length();
            int leftPadding = (cardLength - indexLength) / 2;
            int rightPadding = cardLength - indexLength - leftPadding;            

            index.append(" ".repeat(leftPadding));
            index.append(cardIndexStr);           
            index.append(" ".repeat(rightPadding));
            index.append("  ");
        }
        return index.toString();
    }
}
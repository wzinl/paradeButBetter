package main.context;

import java.util.ArrayList;

import main.models.ParadeBoard;
import main.models.cards.Deck;
import main.models.player.Player;


public class GameContext{

    //declaration of variables
    private int finalRoundStarterIndex; // index of player who triggered the final round
    private int currentStateIdx;
    private final ArrayList<Player> playerList;
    private int currentPlayerIndex;
    private final Deck deck;
    private final ParadeBoard paradeBoard;



    /*
    creating Game Context from Init, populates all the necessary
    fields to start the game
     */
    public GameContext(ArrayList<Player> playerList, int currentStateIdx, int currentPlayerIndex, Deck deck, ParadeBoard paradeBoard){
        this.currentPlayerIndex = currentPlayerIndex;
        this.currentStateIdx = currentStateIdx;
        this.deck = deck;
        this.paradeBoard = paradeBoard;
        this.finalRoundStarterIndex = -1;
        this.playerList = playerList;
    }

    // public access to game context instance 
    public GameContext getInstance() {
        return this;
    }

    // Manage game state transition --> delegate state transition to game state manager
    public void setCurrentStateIdx(int currentStateIdx){
        this.currentStateIdx = currentStateIdx;
    }

    //The player who triggered the final round
    public void setFinalRoundStarterIndex(int index){
        this.finalRoundStarterIndex = index;
    }

    //Relevant Setters and Getters


    public int getCurentStateIndex(){
        return this.currentStateIdx;
    }


    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }


    public int getFinalRoundStarterIndex(){
        return this.finalRoundStarterIndex;
    }

    public int getCurrentPlayerIndex(){
        return currentPlayerIndex;
    }

    public Player getCurrentPlayer(){
        return playerList.get(currentPlayerIndex);
    }

    public ArrayList<Player> getPlayerList() {
        return this.playerList;
    }

    public ParadeBoard getParadeBoard(){
        return this.paradeBoard;
    }

    public Deck getDeck(){
        return this.deck;
    }


}


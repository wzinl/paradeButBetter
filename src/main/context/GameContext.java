package main.context;

import java.util.ArrayList;
import main.gameStates.GameState;
import main.models.*;

public class GameContext {

    //declaration of variables
    private int finalRoundTriggerPlayerIndex; // index of player who triggered the final round
    private GameState currentState;
    private ArrayList<Player> playerList;
    private int currentPlayerIndex;
    private Deck deck;
    private ParadeBoard paradeBoard;
    private boolean isInFinalRound;



    // constructor for game context
    public GameContext(){

    }
    /*
    creating Game Context from Init, populates all the necessary
    fields to start the game
     */
    public GameContext(ArrayList<Player> playerList, GameState currentState, int currentPlayerIndex, Deck deck, ParadeBoard paradeBoard){
        this();
        this.currentPlayerIndex = currentPlayerIndex;
        this.currentState = currentState;
        this.deck = deck;
        this.paradeBoard = paradeBoard;
        this.isInFinalRound = false;
        this.finalRoundTriggerPlayerIndex = -1;
        this.playerList = playerList;
        System.out.println("GameContext constructed!");
    }

    // public access to game context instance 
    public GameContext getInstance() {
        return this;
    }

    // Manage game state transition --> delegate state transition to game state manager
    public void setCurrentState(GameState newState){
        this.currentState = newState;
    }

    //The player who triggered the final round
    public void setFinalRoundTriggerPlayerIndex(int index){
        this.finalRoundTriggerPlayerIndex = index;
    }

    //Relevant Setters and Getters

    public void setInFinalRound(boolean isInFinalRound){
        this.isInFinalRound = isInFinalRound;
    }

    public GameState setCurrentState(){
        return this.currentState;
    }

    public GameState getCurrentState(){
        return this.currentState;
    }


    public int getFinalRoundTriggerPlayerIndex(){
        return this.finalRoundTriggerPlayerIndex;
    }

    public boolean getIsInFinalRound(){
        return isInFinalRound;
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


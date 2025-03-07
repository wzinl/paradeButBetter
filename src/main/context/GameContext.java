package main.context;

import java.util.ArrayList;
import main.models.*;
import main.gameStates.GameState;
import main.gameStates.GameStateManager;
import main.gameStates.InitState;
import main.gameStates.TurnState;

public class GameContext {
    // ensure that only one instance of the game context
    private static GameContext instance;

    private GameState currentState;
    private GameStateManager gsm;
    private ArrayList<Player> playerList;
    private int currentPlayerIndex;
    private Deck deck;
    private ParadeBoard paradeBoard;

    // constructor for game context
    public GameContext(){
        this.currentPlayerIndex = 0;

    }

    // public access to game context instance 
    public static GameContext getInstance() {
        if (instance == null) {
            instance = new GameContext();
        }
        return instance;
    }

    // Initialise game components
    public void initialiseGame(ArrayList<Player> players, Deck deck){
        this.playerList = players;
        this.deck = deck;
        this.paradeBoard = new ParadeBoard(deck);
        this.currentState = new InitState(null, this); // set initial state *might need change parameter*
        this.gsm = new GameStateManager(currentState); // initialise gsm with current state
        gsm.setState(currentState); // calls enter() for the InitState
    }

    // Manage game state transition
    public void changeState(GameState newState){
        this.currentState = newState; // update current state
        newState.enter();
    }

    public Player getCurrentPlayer(){
        return playerList.get(currentPlayerIndex);
    }

    public void nextTurn(){
        this.currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size(); // circular queue
        changeState(new TurnState(gsm, this));
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


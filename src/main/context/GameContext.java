package main.context;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import main.gameStates.GameState;
import main.models.*;





public class GameContext {
    // ensure that only one instance of the game context
    private static GameContext instance;
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

    //creating Game Context from Init
    public GameContext(ArrayList<String> nameList, GameState currentState){
        this();
        
        playerList = new ArrayList<Player>();
        for(String name : nameList){
            playerList.add(new Player(name));
        }
        int startingIndex = ThreadLocalRandom.current().nextInt(0, nameList.size());
        this.currentPlayerIndex = startingIndex;
        this.currentState = currentState;
        this.deck = new Deck();
        this.paradeBoard = new ParadeBoard(deck);
        this.isInFinalRound = false;
        this.finalRoundTriggerPlayerIndex = -1;
        System.out.println("GameContext constructed!");
    }

    // public access to game context instance 
    public static GameContext getInstance() {
        if (instance == null) {
            instance = new GameContext();
        }
        return instance;
    }

    // Manage game state transition --> delegate state transition to game state manager
    public void setCurrentState(GameState newState){
        this.currentState = newState;
    }

    public void setFinalRoundTriggerPlayerIndex(int index){
        this.finalRoundTriggerPlayerIndex = index;
    }

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

    public boolean isInFinalRound(){
        return isInFinalRound;
    }

    public int getCurrentPlayerIndex(){
        return currentPlayerIndex;
    }

    public Player getCurrentPlayer(){
        return playerList.get(currentPlayerIndex);
    }

    // not for gamecontext to handle
    // public void nextTurn(){
    //     this.currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size(); // circular queue
    //     changeState(new TurnState(gsm, this));
    // }

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


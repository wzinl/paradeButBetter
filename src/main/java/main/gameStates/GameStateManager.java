package main.gameStates;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

import main.Game;
import main.context.GameContext;
import main.gameStates.GamePlayStates.GameEndState;
import main.gameStates.GamePlayStates.GameTurnStates.NotFinalRoundTurnState;
import main.helpers.InputManager;

public class GameStateManager {
    private final Game game;
    private final String gameStateID;
    private GameState currentState;
    private GameContext currentContext;
    private final InputManager inputManager;
    
    public GameStateManager(InputManager inputManager, Game game){
        gameStateID = UUID.randomUUID().toString();
        this.inputManager = inputManager;
        this.game = game;
    }

    public void init() {
        currentState = new InitState(this, inputManager);
        currentState.enter();

        this.currentContext = ((InitState)currentState).createGameContext();
    }
    
    public void setState(GameState newState) {
        //exit the turn state 
        if (currentState != null) {
            currentState.exit();
        } 
        //Set the new current state, then enter the next state of the game
        currentState = newState;
        currentState.enter();
    }

    public InputManager getInputManager() {
        return inputManager;
    }
    
    //when the game is done
    public void closeGame() {
        if (currentState != null) {
            currentState.exit();
        }

    }
    //for transition into the next Game State
    public void nextState() {
        if(currentState instanceof InitState){
            setState(new NotFinalRoundTurnState(this, currentContext, inputManager));
        }

        if(currentState instanceof NotFinalRoundTurnState){
            setState(new GameEndState(this, currentContext, inputManager));
        }

        if(currentState instanceof GameEndState){
            closeGame();
        }
    }
    
    public String getGameStateID() {
        return gameStateID;
    }
    

    public void saveGame(String saveName) throws IOException {
        saveName = "saves/"+ saveName + ".dat";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveName))) {
            oos.writeObject(currentContext);
        }
    }

    public GameContext loadGame(String saveName) throws IOException, ClassNotFoundException {
        saveName = "saves/"+ saveName + ".dat";
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveName))) {
            return (GameContext) ois.readObject();
        }
    }
    
    public void exitGame(){
        game.exit();
    }

}

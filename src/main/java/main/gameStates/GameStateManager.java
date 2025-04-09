package main.gameStates;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import main.Game;
import main.context.GameContext;
import main.gameStates.GamePlayStates.GameEndState;
import main.gameStates.GamePlayStates.GameTurnStates.FinalRoundTurnState;
import main.gameStates.GamePlayStates.GameTurnStates.NotFinalRoundTurnState;
import main.helpers.inputHandlers.InputManager;

public class GameStateManager {
    private final Game game;
    private final String gameStateID;
    private GameState currentState;
    private GameContext currentContext;
    private final InputManager inputManager;
    private final Map<Class<? extends GameState>, Class<? extends GameState>> stateFlow = new HashMap<>();

    
    public GameStateManager(InputManager inputManager, Game game){
        gameStateID = UUID.randomUUID().toString();
        this.inputManager = inputManager;
        this.game = game;

        stateFlow.put(InitState.class, NotFinalRoundTurnState.class);
        stateFlow.put(NotFinalRoundTurnState.class, FinalRoundTurnState.class);
        stateFlow.put(FinalRoundTurnState.class, GameEndState.class);
    }

    public void init() {
        currentState = new InitState(this, inputManager);
        currentState.run();

        this.currentContext = ((InitState)currentState).createGameContext();
    }
    
    public void setState(GameState newState) {
        //Set the new current state, then enter the next state of the game
        currentState = newState;
        currentState.run();
    }

    public InputManager getInputManager() {
        return inputManager;
    }
    
    //for transition into the next Game State
    public void nextState() {
        Class<? extends GameState> nextStateClass = stateFlow.get(currentState.getClass());

        if (nextStateClass != null) {
            try {
                GameState nextState = nextStateClass
                    .getConstructor(GameStateManager.class, GameContext.class, InputManager.class)
                    .newInstance(this, currentContext, inputManager);
                setState(nextState);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Failed to transition to the next state", e);
            }
        } else if (currentState instanceof GameEndState) {
            game.exit();
        }
    }
    
    public String getGameStateID() {
        return gameStateID;
    }
    
    
    public void exitGame(){
        game.exit();
    }

}

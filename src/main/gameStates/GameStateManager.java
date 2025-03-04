package main.gameStates;

import main.context.GameContext;

public class GameStateManager {
    private GameState currentState;
    private GameContext currentContext;

    public GameStateManager(GameState currentState) {
        this.currentState = currentState;
    }
    
    public void setState(GameState newState) {
        if (currentState != null) {
            currentState.exit();
        }
        currentState = newState;
        currentState.enter();
    }

    public GameState getState() {
        return currentState;
    }
}

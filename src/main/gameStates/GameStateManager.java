package main.gameStates;

import main.context.GameContext;

public class GameStateManager {
    private GameState currentState;
    private GameContext currentContext;
    
    public void setState(GameState newState) {
        if (currentState != null) {
            currentState.exit();
        }
        currentState = newState;
        // System.out.println("newstate");
        currentState.enter();
    }

    public void closeGame() {
        if (currentState != null) {
            currentState.exit();
        }

    }

    public void nextState() {
        if(currentState instanceof InitState){
            // setState(new TurnState(this, currentContext));
            System.out.println("moving to turnstate");
        }

        // if(currentState instanceof TurnState){
        //     setState(new GameEndState(this, currentContext));
        // }

        if(currentState instanceof GameEndState){
            closeGame();
        }
    }

    public GameState getState() {
        return currentState;
    }
}

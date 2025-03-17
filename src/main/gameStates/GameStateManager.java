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
        currentState.enter();

        if(currentState instanceof InitState){
            InitState currentInitState = (InitState)currentState;
            this.currentContext = currentInitState.createGameContext();
        }
        // System.out.println("newstate");
    }

    public void closeGame() {
        if (currentState != null) {
            currentState.exit();
        }

    }

    public void nextState() {
        if(currentState instanceof InitState){
            System.out.println("moving to turnstate");
            setState(new TurnState(this, currentContext));
        }

        if(currentState instanceof TurnState){
            setState(new GameEndState(this, currentContext));
        }

        if(currentState instanceof GameEndState){
            closeGame();
        }
    }

    public GameState getState() {
        return currentState;
    }
}

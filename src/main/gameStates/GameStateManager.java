package main.gameStates;

import main.context.GameContext;

public class GameStateManager {
    private GameState currentState;
    private GameContext currentContext;
    
    public void setState(GameState newState) {
        //exit the turn state 
        if (currentState != null) {
            currentState.exit();
        } 
        //Set the new current state, then enter the next state of the game
        currentState = newState;
        currentState.enter();

        //When the current state is InitState, we create the game context
        if(currentState instanceof InitState currentInitState){
            this.currentContext = currentInitState.createGameContext();
        }
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
            System.out.println("Moving to Turnstate");
            setState(new TurnState(this, currentContext));
        }

        if(currentState instanceof TurnState){
            setState(new GameEndState(this, currentContext));
        }

        if(currentState instanceof GameEndState){
            closeGame();
        }
    }

}

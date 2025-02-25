package main.gameStates;

import javax.security.auth.PrivateCredentialPermission;

public class GameStateManager {
    private GameState currentState;
    private GameContext currentContext;

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

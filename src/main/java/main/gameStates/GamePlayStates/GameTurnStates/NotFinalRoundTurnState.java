package main.gameStates.GamePlayStates.GameTurnStates;

import main.context.GameContext;
import main.gameStates.GameStateManager;
import main.helpers.InputManager;
import main.helpers.ui.UIManager;
import main.models.player.Player;


public class NotFinalRoundTurnState extends GameTurnState {

    public NotFinalRoundTurnState(GameStateManager gsm, GameContext context, InputManager inputManager) {
        super(gsm, context, inputManager);
    }

    @Override
    public void enter() {
        UIManager.clearScreen();
        boolean finished = false;

        while (!finished) {
            Player currentPlayer = playerList.get(currentPlayerIndex);
            playTurn(currentPlayer);

            if (currentPlayer.hasCollectedAllColours() || deck.isEmpty()) {
                finished = true;
                context.setFinalRoundStarterIndex(currentPlayerIndex);
            }

            this.currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
            context.setCurrentPlayerIndex(currentPlayerIndex);

        }
    }

    @Override
    public void exit() {
        System.out.println("Exiting TurnState.");
    }
}
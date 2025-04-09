package parade.gameStates.GamePlayStates.GameTurnStates;

import parade.context.GameContext;
import parade.gameStates.GameStateManager;
import parade.helpers.inputHandlers.InputManager;
import parade.helpers.ui.UIManager;
import parade.models.player.Player;


public class NotFinalRoundTurnState extends GameTurnState {

    public NotFinalRoundTurnState(GameStateManager gsm, GameContext context, InputManager inputManager) {
        super(gsm, context, inputManager);
    }

    @Override
    public void run() {
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
}
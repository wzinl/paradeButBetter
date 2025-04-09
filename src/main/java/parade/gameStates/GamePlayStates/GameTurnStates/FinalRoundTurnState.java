package parade.gameStates.GamePlayStates.GameTurnStates;

import parade.context.GameContext;
import parade.gameStates.GameStateManager;
import parade.helpers.inputHandlers.InputManager;
import parade.helpers.ui.UIManager;
import parade.models.player.Player;

public class FinalRoundTurnState extends GameTurnState {
    protected final int finalPlayerIndex;

    public FinalRoundTurnState(GameStateManager gsm, GameContext context, InputManager inputManager) {
        super(gsm, context, inputManager);
        this.finalPlayerIndex = context.getFinalRoundStarterIndex();
    }

    @Override
    public void run() {
        UIManager.displayFinalRoundMessage();
        inputManager.getEnter();
        UIManager.clearScreen();
        while (this.currentPlayerIndex != finalPlayerIndex) {
            Player currentPlayer = playerList.get(currentPlayerIndex);
            playTurn(currentPlayer);
    
            this.currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
            context.setCurrentPlayerIndex(currentPlayerIndex);

        }

        playTurn(playerList.get(finalPlayerIndex));
        this.currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
        currentPlayerIndex = context.getCurrentPlayerIndex();

    }
}

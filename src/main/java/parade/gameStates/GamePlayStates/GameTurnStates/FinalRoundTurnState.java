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
        UIManager.displayFinalRoundMessage(context.getDeck().isEmpty());
        inputManager.getEnter();
        while (this.currentPlayerIndex != finalPlayerIndex) {
            UIManager.clearScreen();

            Player currentPlayer = playerList.get(currentPlayerIndex);
            playTurn(currentPlayer);
    
            this.currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
            context.setCurrentPlayerIndex(currentPlayerIndex);
        }
        UIManager.clearScreen();
        playTurn(playerList.get(finalPlayerIndex));
        this.currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
        currentPlayerIndex = context.getCurrentPlayerIndex();
        UIManager.clearScreen();

    }
}

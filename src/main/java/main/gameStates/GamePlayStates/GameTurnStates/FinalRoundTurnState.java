package main.gameStates.GamePlayStates.GameTurnStates;

import main.context.GameContext;
import main.gameStates.GameStateManager;
import main.helpers.inputHandlers.InputManager;
import main.helpers.ui.UIManager;
import main.models.player.Player;

public class FinalRoundTurnState extends GameTurnState {
    protected final int finalPlayerIndex;

    public FinalRoundTurnState(GameStateManager gsm, GameContext context, InputManager inputManager) {
        super(gsm, context, inputManager);
        this.finalPlayerIndex = context.getFinalRoundStarterIndex();
    }

    @Override
    public void enter() {
        while (this.currentPlayerIndex != finalPlayerIndex) {
            UIManager.displayFinalRoundMessage();
            Player currentPlayer = playerList.get(currentPlayerIndex);
            playTurn(currentPlayer);
    
            this.currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
            context.setCurrentPlayerIndex(currentPlayerIndex);

        }

        playTurn(playerList.get(finalPlayerIndex));
        this.currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
        currentPlayerIndex = context.getCurrentPlayerIndex();

    }

    @Override
    public void exit() {
        // Logic to clean up or transition out of the final round state
        System.out.println("Exiting Final Round...");
    }
    
}

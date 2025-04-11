package parade.gameStates.GamePlayStates.GameTurnStates;

import parade.context.GameContext;
import parade.gameStates.GameStateManager;
import parade.helpers.inputHandlers.InputManager;
import parade.helpers.ui.UIManager;
import parade.models.player.Player;

/**
 * Represents the game state during the final round of the Parade game.
 * 
 * This state is entered once a player has collected all 6 colors or the deck is empty.
 * Each remaining player (starting from the next after the one who triggered the final round)
 * gets exactly one last turn.
 */
public class FinalRoundTurnState extends GameTurnState {

    /** The index of the player who triggered the final round. */
    protected final int finalPlayerIndex;

    /**
     * Constructs the FinalRoundTurnState.
     *
     * @param gsm the GameStateManager managing the game state transitions
     * @param context the current GameContext containing game data
     * @param inputManager the InputManager handling user input
     */
    public FinalRoundTurnState(GameStateManager gsm, GameContext context, InputManager inputManager) {
        super(gsm, context, inputManager);
        this.finalPlayerIndex = context.getFinalRoundStarterIndex();
    }

    /**
     * Executes the logic of the final round:
     * Displays a message indicating the final round has started, 
     * then allows each player (after the one who triggered the round) to take one final turn.
     * The player who triggered the final round goes last.
     */
    @Override
    public void run() {
        // Show message explaining the final round trigger (deck empty or 6-color collection)
        UIManager.displayFinalRoundMessage(context.getDeck().isEmpty());
        inputManager.getEnter();  // Wait for player to press Enter

        // Let all players starting from the one after the trigger player take one final turn
        while (this.currentPlayerIndex != finalPlayerIndex) {
            UIManager.clearScreen();

            Player currentPlayer = playerList.get(currentPlayerIndex);
            playTurn(currentPlayer);

            // Move to next player
            this.currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
            context.setCurrentPlayerIndex(currentPlayerIndex);
        }

        // Finally, let the triggering player play
        UIManager.clearScreen();
        playTurn(playerList.get(finalPlayerIndex));

        // Advance index in case it's needed later (e.g. for end state transition)
        this.currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
        currentPlayerIndex = context.getCurrentPlayerIndex();  // Reset if needed
        UIManager.clearScreen();
    }
}

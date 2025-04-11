package parade.gameStates.GamePlayStates.GameTurnStates;

import parade.context.GameContext;
import parade.gameStates.GameStateManager;
import parade.helpers.inputHandlers.InputManager;
import parade.helpers.ui.UIManager;
import parade.models.player.Player;

/**
 * Represents the main game loop during the non-final rounds of the game.
 * This state will transition to the final round if a player collects all 6 colors
 * or if the deck is empty.
 */
public class NotFinalRoundTurnState extends GameTurnState {

    /**
     * Constructs the NotFinalRoundTurnState.
     *
     * @param gsm           The GameStateManager to manage transitions.
     * @param context       The shared GameContext for the session.
     * @param inputManager  The input handler used for player input.
     */
    public NotFinalRoundTurnState(GameStateManager gsm, GameContext context, InputManager inputManager) {
        super(gsm, context, inputManager);
    }

    /**
     * Runs the game loop for this state.
     * Each player takes a turn until either:
     * - A player has collected all 6 colours, or
     * - The deck becomes empty.
     * When one of the conditions is met, this state finishes and sets the index
     * of the player who triggered the final round.
     */
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

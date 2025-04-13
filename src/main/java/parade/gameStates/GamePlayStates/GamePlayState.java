package parade.gameStates.GamePlayStates;

import java.util.HashMap;
import java.util.Map;

import parade.context.GameContext;
import parade.gameStates.GameState;
import parade.gameStates.GameStateManager;
import parade.helpers.inputHandlers.InputManager;
import parade.helpers.inputTypes.SelectionInput;
import parade.helpers.ui.UIManager;
import parade.models.player.Player;

/**
 * Abstract superclass for game states that involve actual gameplay.
 * Handles common logic for input selection, input switching, context updating,
 * and board viewing for both human and bot players.
 */
public abstract class GamePlayState extends GameState {

    /** Maps action characters to the corresponding methods to execute. */
    private final Map<Character, Runnable> actionHandlers = new HashMap<>();

    /** Index of the current player in the player list. */
    protected int currentPlayerIndex;

    /**
     * Constructs a GamePlayState with access to game context and input.
     *
     * @param gsm           The GameStateManager managing this state.
     * @param context       The current GameContext holding shared game data.
     * @param inputManager  The InputManager used to get player input.
     */
    public GamePlayState(GameStateManager gsm, GameContext context, InputManager inputManager) {
        super(gsm, context, inputManager);
        this.currentPlayerIndex = context.getCurrentPlayerIndex();
        initializeActionHandlers();
    }

    /**
     * Initializes the map of valid in-game actions and their associated methods.
     */
    private void initializeActionHandlers() {
        actionHandlers.put('C', this::changeInputType);
        actionHandlers.put('E', this::exitGame);
        actionHandlers.put('D', this::displayAllPlayerBoards);
    }

    /**
     * Executes a registered action based on the input character.
     *
     * @param action The character representing the player's chosen action.
     */
    protected void performAction(char action) {
        // UIManager.clearScreen();
        Runnable actionHandler = actionHandlers.get(action);
        if (actionHandler != null) {
            actionHandler.run();
        } else {
            System.out.println("Invalid action: " + action);
        }
    }

    /**
     * Exits the game immediately.
     */
    private void exitGame() {
        gsm.exitGame();
    }

    /**
     * Displays all player boards in the current game context.
     */
    public void displayAllPlayerBoards() {
        UIManager.displayBoardOverview(context.getPlayerList(), context.getParadeBoard());
        inputManager.getEnter();
    }

    /**
     * Switches the current player's preferred input method (menu or line).
     */
    private void changeInputType() {
        Player currentPlayer = context.getPlayerList().get(context.getCurrentPlayerIndex());

        currentPlayer.setPreferMenu(!currentPlayer.getPreferMenu());
    }

    /**
     * Gets the appropriate selection input from the current player.
     *
     * @param current The player whose turn it is.
     * @return A SelectionInput representing the player's choice (action or card).
     */
    protected SelectionInput getSelectionInput(Player current) {
        return inputManager.turnSelect(paradeBoard, current);
    }
}

package parade.gameStates;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import parade.Game;
import parade.context.GameContext;
import parade.gameStates.GamePlayStates.GameEndState;
import parade.gameStates.GamePlayStates.GameTurnStates.FinalRoundTurnState;
import parade.gameStates.GamePlayStates.GameTurnStates.NotFinalRoundTurnState;
import parade.helpers.inputHandlers.InputManager;

/**
 * Manages the flow and transitions between different game states.
 * Responsible for initializing the game, moving between phases (init, turns, final round, end),
 * and providing access to shared resources like the input manager and game context.
 */
public class GameStateManager {

    /** Reference to the main game instance. */
    private final Game game;

    /** Unique identifier for the current game session. */
    private final String gameStateID;

    /** The currently active game state. */
    private GameState currentState;

    /** The shared game context storing players, board, deck, etc. */
    private GameContext currentContext;

    /** Handles all user input during state transitions. */
    private final InputManager inputManager;

    /** Defines the flow of game states in order. */
    private final Map<Class<? extends GameState>, Class<? extends GameState>> stateFlow = new HashMap<>();

    /**
     * Constructs a new GameStateManager and initializes the game state flow.
     *
     * @param inputManager the input manager to use for all states
     * @param game         the Game instance controlling this session
     */
    public GameStateManager(InputManager inputManager, Game game) {
        gameStateID = UUID.randomUUID().toString();
        this.inputManager = inputManager;
        this.game = game;

        // Define the game flow: Init → Normal Rounds → Final Round → Game End
        stateFlow.put(InitState.class, NotFinalRoundTurnState.class);
        stateFlow.put(NotFinalRoundTurnState.class, FinalRoundTurnState.class);
        stateFlow.put(FinalRoundTurnState.class, GameEndState.class);
    }

    /**
     * Initializes the game by setting the initial state and generating the game context.
     */
    public void init() {
        currentState = new InitState(this, inputManager);
        currentState.run();
        this.currentContext = ((InitState) currentState).createGameContext();
    }

    /**
     * Transitions the game to the specified new state and executes it.
     *
     * @param newState the new GameState to transition to
     */
    public void setState(GameState newState) {
        currentState = newState;
        currentState.run();
    }

    /**
     * Retrieves the input manager used for player input during the game.
     *
     * @return the current InputManager
     */
    public InputManager getInputManager() {
        return inputManager;
    }

    /**
     * Proceeds to the next logical game state based on the current state.
     * Uses the state flow map to determine the next transition.
     */
    public void nextState() {
        Class<? extends GameState> nextStateClass = stateFlow.get(currentState.getClass());

        if (nextStateClass != null) {
            try {
                GameState nextState = nextStateClass
                    .getConstructor(GameStateManager.class, GameContext.class, InputManager.class)
                    .newInstance(this, currentContext, inputManager);
                setState(nextState);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Failed to transition to the next state", e);
            }
        } else if (currentState instanceof GameEndState) {
            game.exit();
        }
    }
    /**
     * Exits the game by calling the Game's exit method.
     */
    public void exitGame() {
        game.exit();
    }
}

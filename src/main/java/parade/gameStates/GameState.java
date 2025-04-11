package parade.gameStates;

import java.util.ArrayList;

import parade.context.GameContext;
import parade.helpers.inputHandlers.InputManager;
import parade.models.ParadeBoard;
import parade.models.cards.Deck;
import parade.models.player.Player;

/**
 * Abstract base class representing a game state in the Parade game.
 * 
 * Each concrete state (e.g. InitState, GameTurnState, GameEndState) should implement
 * the {@code run()} method to define its behavior when activated.
 */
public abstract class GameState {

    /** Reference to the GameStateManager that controls state transitions. */
    protected final GameStateManager gsm;

    /** Shared context of the game holding players, deck, board, etc. */
    protected GameContext context;

    /** List of players currently in the game. */
    protected ArrayList<Player> playerList;

    /** The central parade board in play. */
    protected ParadeBoard paradeBoard;

    /** The current deck of cards used in the game. */
    protected Deck deck;

    /** Handles user input (line or menu input). */
    protected InputManager inputManager;

    /**
     * Constructor used specifically for InitState before the context is created.
     *
     * @param gsm the game state manager controlling transitions
     * @param inputManager input manager used to capture player input
     */
    public GameState(GameStateManager gsm, InputManager inputManager) { 
        this.gsm = gsm;
        this.inputManager = inputManager;
    }

    /**
     * Constructor used after the GameContext is available.
     * Sets up shared references to game data for all states.
     *
     * @param gsm the game state manager controlling transitions
     * @param context the initialized shared game context
     * @param inputManager input manager used to capture player input
     */
    public GameState(GameStateManager gsm, GameContext context, InputManager inputManager) {
        this.gsm = gsm;
        this.inputManager = inputManager;
        this.context = context;
        this.playerList = context.getPlayerList();
        this.paradeBoard = context.getParadeBoard();
        this.deck = context.getDeck();
    }
    /**
     * Main logic of this game state. Called once the state becomes active.
     * Should be implemented by each subclass to define behavior.
     */
    public abstract void run();
}

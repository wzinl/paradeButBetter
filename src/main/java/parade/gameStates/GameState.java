package parade.gameStates;

import java.util.ArrayList;

import parade.context.GameContext;
import parade.helpers.inputHandlers.InputManager;
import parade.models.ParadeBoard;
import parade.models.cards.Deck;
import parade.models.player.Player;

public abstract class GameState {
    protected final GameStateManager gsm;
    protected GameContext context;
    protected ArrayList<Player> playerList;
    protected ParadeBoard paradeBoard;
    protected Deck deck;
    protected InputManager inputManager;

    // constructor for init state
    public GameState(GameStateManager gsm, InputManager inputManager) { 
        this.gsm = gsm;
        this.inputManager = inputManager;
        
    }
    public GameState(GameStateManager gsm, GameContext context, InputManager inputManager) {
        this.gsm = gsm;
        this.inputManager = inputManager;
        this.context = context;
        this.playerList = context.getPlayerList();
        this.paradeBoard = context.getParadeBoard();
        this.deck = context.getDeck();
    }

    // Method to set context when it's available (can be called later when context is initialized)
    public void setContext(GameContext context) {
        this.context = context;
    }
    public abstract void run();    // Called when the state is entered
}
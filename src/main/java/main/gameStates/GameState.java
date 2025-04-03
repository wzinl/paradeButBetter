package main.gameStates;

import java.util.ArrayList;

import main.context.GameContext;
import main.helpers.InputHandler;
import main.models.ParadeBoard;
import main.models.cards.Deck;
import main.models.player.Player;

public abstract class GameState {
    protected final GameStateManager gsm;
    protected GameContext context;
    protected ArrayList<Player> playerList;
    protected int currentPlayerIndex;
    protected ParadeBoard paradeBoard;
    protected Deck deck;
    protected int finalPlayerIndex;
    protected InputHandler inputHandler;

    // constructor for init state
    public GameState(GameStateManager gsm, InputHandler inputHandler) { 
        this.gsm = gsm;
        this.inputHandler = inputHandler;
        
    }
    public GameState(GameStateManager gsm, GameContext context, InputHandler inputHandler) {
        this.gsm = gsm;
        this.inputHandler = inputHandler;
        this.context = context;
        this.playerList = context.getPlayerList();
        this.currentPlayerIndex = context.getCurrentPlayerIndex();
        this.paradeBoard = context.getParadeBoard();
        this.deck = context.getDeck();
        this.finalPlayerIndex = context.getFinalRoundStarterIndex();
    }

    // Method to set context when it's available (can be called later when context is initialized)
    public void setContext(GameContext context) {
        this.context = context;
        // Initialize state-dependent fields
        if (context != null) {
            this.playerList = context.getPlayerList();
            this.currentPlayerIndex = context.getCurrentPlayerIndex();
            this.paradeBoard = context.getParadeBoard();
            this.deck = context.getDeck();
        }
    }

    public abstract void enter();    // Called when the state is entered
    public abstract void exit();     // Called when switching to another state
}
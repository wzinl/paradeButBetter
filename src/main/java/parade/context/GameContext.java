package parade.context;

import java.util.ArrayList;

import parade.models.ParadeBoard;
import parade.models.cards.Deck;
import parade.models.player.Player;

/**
 * Holds all core data relevant to a single game session.
 * This includes the current player index, deck, parade board, list of players,
 * and index of the player who triggered the final round.
 * 
 * This context object is passed between game states to allow shared access to game state.
 */
public class GameContext {

    /** Index of the player who triggered the final round. Default is -1 if not triggered. */
    private int finalRoundStarterIndex;

    /** List of players participating in the game. */
    private final ArrayList<Player> playerList;

    /** Index of the current player whose turn it is. */
    private int currentPlayerIndex;

    /** The deck of cards used during the game. */
    private final Deck deck;

    /** The parade board containing active cards. */
    private final ParadeBoard paradeBoard;

    /**
     * Constructs a GameContext object with all the essential information to run the game.
     *
     * @param playerList         the list of players
     * @param currentPlayerIndex the index of the current player
     * @param deck               the deck used for drawing cards
     * @param paradeBoard        the active parade board
     */
    public GameContext(ArrayList<Player> playerList, int currentPlayerIndex, Deck deck, ParadeBoard paradeBoard) {
        this.currentPlayerIndex = currentPlayerIndex;
        this.deck = deck;
        this.paradeBoard = paradeBoard;
        this.finalRoundStarterIndex = -1;
        this.playerList = playerList;
    }
    /**
     * Sets the index of the player who triggered the final round.
     *
     * @param index the index of the triggering player
     */
    public void setFinalRoundStarterIndex(int index) {
        this.finalRoundStarterIndex = index;
    }

    /**
     * Sets the index of the current player.
     *
     * @param currentPlayerIndex the new current player index
     */
    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    /**
     * Gets the index of the player who triggered the final round.
     *
     * @return the final round starter index
     */
    public int getFinalRoundStarterIndex() {
        return this.finalRoundStarterIndex;
    }

    /**
     * Gets the index of the current player.
     *
     * @return the current player index
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
    /**
     * Gets the full list of players in the game.
     *
     * @return the player list
     */
    public ArrayList<Player> getPlayerList() {
        return this.playerList;
    }

    /**
     * Gets the ParadeBoard currently in play.
     *
     * @return the parade board
     */
    public ParadeBoard getParadeBoard() {
        return this.paradeBoard;
    }

    /**
     * Gets the deck being used for the game.
     *
     * @return the card deck
     */
    public Deck getDeck() {
        return this.deck;
    }
}

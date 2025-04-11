package parade.models.player;

import java.util.ArrayList;

import parade.models.cards.Card;

/**
 * Represents a player in the Parade game.
 * Each player has a name, a unique ID, a hand, a board, a score, and a UI preference.
 */
public class Player {
    /** The display name of the player. */
    private final String playerName;

    /** The current score of the player, calculated from their board. */
    private int playerScore;

    /** The player's hand of cards. */
    private final PlayerHand playerhand;

    /** The player's board where collected cards are stored. */
    private final PlayerBoard playerBoard;

    /** Indicates whether the player prefers a menu-based UI. */
    private boolean preferMenu;

    /**
     * Constructs a Player with the specified name.
     *
     * @param playerName the name of the player
     */
    public Player(String playerName) {
        this.playerName = playerName;
        this.playerhand = new PlayerHand();
        this.playerBoard = new PlayerBoard();
        this.preferMenu = true;
    }

    /**
     * Returns the player's display name.
     *
     * @return the player name
     */
    public String getPlayerName() {
        return playerName;
    }
    /**
     * Returns the player's current score.
     *
     * @return the score
     */
    public int getPlayerScore() {
        return playerScore;
    }

    /**
     * Returns the player's hand.
     *
     * @return the player's {@link PlayerHand}
     */
    public PlayerHand getPlayerHand() {
        return playerhand;
    }

    /**
     * Returns the player's board.
     *
     * @return the player's {@link PlayerBoard}
     */
    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    /**
     * Returns whether the player prefers menu-based interaction.
     *
     * @return true if the player prefers menu input, false if line input
     */
    public boolean getPreferMenu() {
        return preferMenu;
    }

    /**
     * Sets whether the player prefers menu-based interaction.
     *
     * @param preferMenu true for menu input, false for line input
     */
    public void setPreferMenu(boolean preferMenu) {
        this.preferMenu = preferMenu;
    }

    /**
     * Calculates and updates the player's score based on their board.
     * Face-up cards contribute their actual value; face-down cards contribute 1 point.
     *
     * @return the newly calculated score
     */
    public int calculateScore() {
        if (playerBoard == null || playerBoard.getPlayerBoardMap() == null) {
            return 0;
        }

        playerScore = 0;

        for (ArrayList<Card> playerCards : playerBoard.getPlayerBoardMap().values()) {
            if (playerCards == null) continue;

            for (Card card : playerCards) {
                playerScore += card.getIsFaceUp() ? card.getValue() : 1;
            }
        }

        return playerScore;
    }

    /**
     * Checks if the player has collected at least one card of all six colors.
     *
     * @return true if all colors are present, false otherwise
     */
    public boolean hasCollectedAllColours() {
        return playerBoard.getPlayerBoardMap().keySet().size() == 6;
    }
}

package main.helpers.ui;

import java.util.ArrayList;
import java.util.List;

import main.models.ParadeBoard;
import main.models.cards.Card;
import main.models.player.Player;
import main.models.player.PlayerBoard;

/**
 * UIManager acts as a controller for user interface-related operations,
 * delegating rendering tasks to DisplayFactory while managing flow and formatting.
 */
public class UIManager {

    /** Clears the console screen. */
    public static void clearScreen() {
        DisplayFactory.clearScreen();
    }

    /**
     * Pauses the execution for a given number of milliseconds.
     * @param ms Milliseconds to pause
     */
    public static void pauseExecution(int ms) {
        DisplayFactory.pause(ms);
    }

    /**
     * Displays a basic message to the console.
     * @param message The message to print
     */
    public static void displayMessage(String message) {
        System.out.println(message);
    }

    /** Displays the game introduction and clears the screen after it's shown. */
    public static void displayIntroduction() {
        UIManager.clearScreen();
        DisplayFactory.showIntroduction();
        UIManager.clearScreen();
    }

    /** Displays the game instructions using a formatted rule display. */
    public static void displayInstructions() {
        System.out.println(GameRulesDisplay.constructGameInstructions());
    }

    /**
     * Shows a loading message followed by animated dots.
     * @param message The message to display before the dots
     * @param dots The number of dots to animate
     */
    public static void displayLoadingMessage(String message, int dots) {
        System.out.print(message);
        for (int i = 0; i < dots; i++) {
            pauseExecution(500);
            System.out.print(".");
        }
        System.out.println();
    }

    /**
     * Displays final scores along with a board overview.
     * @param players The list of players
     * @param board The final game board state
     */
    public static void displayFinalScores(List<Player> players, ParadeBoard board) {
        displayBoardOverview(players, board);
        System.out.println(DisplayFactory.showFinalScores(players, board));
    }

    /** Displays a prompt for a discard action. */
    public static void displayDiscardPrompt() {
        System.out.println(DisplayFactory.getDiscardPrompt());
    }

    /**
     * Displays a bot's selected action in its turn.
     * @param bot The bot player
     * @param cardIndex The index of the card it chose to play
     */
    public static void displayBotAction(Player bot, int cardIndex) {
        DisplayFactory.getBotAction(bot.getPlayerName(), cardIndex);
    }

    /**
     * Displays an error message in the console.
     * @param message The error message
     */
    public static void displayErrorMessage(String message) {
        System.out.println("ERROR: " + message);
    }

    /**
     * Clears the screen and displays an overview of all player boards and the parade board.
     * @param playerlist The list of players
     * @param paradeBoard The parade board
     */
    public static void displayBoardOverview(List<Player> playerlist, ParadeBoard paradeBoard) {
        UIManager.clearScreen();
        System.out.println();
        System.out.println(DisplayEffects.BOLD + DisplayEffects.ANSI_CYAN + 
                           "Here are all of the players' boards:" + DisplayEffects.ANSI_RESET);
        System.out.println(DisplayFactory.getDisplayBoardOverview(playerlist, paradeBoard));
    }

    /**
     * Displays the player's turn with a focus on a selected card.
     * @param currentPlayer The player taking the turn
     * @param paradeBoard The game board state
     * @param selectedIndex The card index being hovered or chosen
     * @param actionOptions Available actions
     * @param onCardRow Whether the selection is on the card row
     */
    public static void printFormattedTurnDisplay(Player currentPlayer, ParadeBoard paradeBoard, int selectedIndex, String[] actionOptions, boolean onCardRow) {
        System.out.println(DisplayFactory.getTurnDisplay(currentPlayer, paradeBoard, selectedIndex, actionOptions, onCardRow));
    }

    /**
     * Displays the player's turn when no specific card is selected.
     * @param currentPlayer The player taking the turn
     * @param paradeBoard The current parade board state
     * @param actionOptions Available actions
     */
    public static void printFormattedTurnDisplay(Player currentPlayer, ParadeBoard paradeBoard, String[] actionOptions) {
        System.out.println(DisplayFactory.getTurnDisplay(currentPlayer, paradeBoard, actionOptions));
    }

    /**
     * Displays the scoreboard showing the winning players.
     * @param winners List of players with the highest score
     */
    public static void displayScoreboard(List<Player> winners) {
        System.out.println(DisplayFactory.getScoreboard(winners));
    }

    /**
     * Displays the winner of the game.
     * @param winner The winning player
     */
    public static void displayWinner(Player winner) {
        DisplayFactory.displayWinner(winner);
    }

    /**
     * Displays the result of a tie between players.
     * @param tiedPlayers The list of players who tied
     */
    public static void displayTieResults(List<Player> tiedPlayers) {
        DisplayFactory.displayTieResults(tiedPlayers);
    }

    /** Displays a message announcing the start of the final round. */
    public static void displayFinalRoundMessage() {
        clearScreen();
        System.out.println(DisplayEffects.BOLD + DisplayEffects.ANSI_PURPLE +
                           "Each player gets one final turn! No more cards will be drawn!" +
                           DisplayEffects.ANSI_RESET);
    }

    /**
     * Announces the condition that triggered the final round.
     * @param deckEmpty Whether the final round was triggered by the deck running out
     */
    public static void displayFinalRoundTrigger(Boolean deckEmpty) {
        if (deckEmpty) {
            System.out.println("The last card has been drawn");
        } else {
            System.out.println("One player has collected all 6 colours in his board!");
        }
        System.out.println(DisplayEffects.BOLD + DisplayEffects.YELLOW_BG +
                           "Moving on to the final round!🙀🙀🙀" +
                           DisplayEffects.ANSI_RESET);
    }

    /**
     * Displays which card a bot discarded.
     * @param player The bot player
     * @param discardedCard The card discarded
     */
    public static void displayBotDiscard(Player player, Card discardedCard) {
        DisplayFactory.getBotDiscardDisplay(player, discardedCard);
    }

    /**
     * Displays the action of a player playing a card and the resulting board changes.
     * @param player The player who played the card
     * @param chosenCard The card played
     * @param paradeBoard The parade board after the move
     * @param playerBoard The player's personal board after the move
     * @param removedCards Any cards removed due to the play
     */
    public static void displayCardPlay(Player player, Card chosenCard, ParadeBoard paradeBoard, PlayerBoard playerBoard, ArrayList<Card> removedCards) {
        DisplayFactory.getCardPlayDisplay(player, chosenCard, paradeBoard, playerBoard, removedCards);
    }
}

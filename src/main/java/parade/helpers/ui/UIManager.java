package parade.helpers.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.fusesource.jansi.Ansi;

import parade.models.ParadeBoard;
import parade.models.cards.Card;
import parade.models.player.Player;
import parade.models.player.PlayerBoard;

/**
 * UIManager acts as a controller for user interface-related operations,
 * delegating rendering tasks to DisplayFactory while managing flow and formatting.
 */
public class UIManager {

    public static final int BLINK_COUNT = 20;

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
    }

    /** Displays the game instructions using a formatted rule display. */
    public static void displayInstructions() {
        clearScreen();
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
        System.out.println(Ansi.ansi().bold().fg(Ansi.Color.RED).a("ERROR: " + message).reset());
    }

    /**
     * Clears the screen and displays an overview of all player boards and the parade board.
     * @param playerlist The list of players
     * @param paradeBoard The parade board
     */
    public static void displayBoardOverview(List<Player> playerlist, ParadeBoard paradeBoard) {
        UIManager.clearScreen();
        System.out.println();
        System.out.println(Ansi.ansi().bold().fg(Ansi.Color.CYAN).a("Here are all of the players' boards:").reset());
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

    public static void displayTieResults(List<Player> tiedPlayers) {
        DisplayFactory.displayTieResults(tiedPlayers);
    }
    
    /**
     * Announces the condition that triggered the final round.
     * @param deckEmpty Whether the final round was triggered by the deck running out
     */
    public static void displayFinalRoundMessage(Boolean deckEmpty) {
        clearScreen();
        if (deckEmpty) {
            System.out.println("The last card has been drawn");
        } else {
            System.out.println("One player has collected all 6 colours in their board!");
        }
        System.out.println(Ansi.ansi().bold().fg(Ansi.Color.YELLOW).a(
                "Moving on to the final round! 🙀🙀🙀").reset());
        System.out.println(Ansi.ansi().bold().fgBright(Ansi.Color.MAGENTA).a(
                           "Each player gets one final turn! No more cards will be drawn!").reset());
    }

    /*
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
        clearScreen();
        DisplayFactory.getCardPlayDisplay(player, chosenCard, paradeBoard, playerBoard, removedCards);
    }

    public static void typeWriter(String text, int delay) throws InterruptedException {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        try {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            executor.schedule(() -> {}, delay, TimeUnit.MILLISECONDS).get();

        }
        System.out.println();
        } catch (ExecutionException e) {
            throw new RuntimeException("Error during typewriter effect", e);
        } finally {
            executor.shutdown();
        }
    }

    public static void blinkingEffect(String text) throws InterruptedException {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        try {
            for (int i = 0; i < BLINK_COUNT; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException("Blinking effect interrupted");
                }
                System.out.print("\r" + text);
                executor.schedule(() -> {}, 300, TimeUnit.MILLISECONDS).get();
                System.out.print("\r" + " ".repeat(text.length()));
                executor.schedule(() -> {}, 300, TimeUnit.MILLISECONDS).get();
            }
        } catch (ExecutionException e) {
            throw new RuntimeException("Error during blinking effect", e);
        } finally {
            executor.shutdown();
        }
        System.out.println("\r" + text);
    }   

    public static String getNamePrompt(boolean isBot, int index) {
        return DisplayFactory.getNamePrompt(isBot, index);
    }

}
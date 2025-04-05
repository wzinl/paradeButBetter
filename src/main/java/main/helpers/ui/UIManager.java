package main.java.main.helpers.ui;

import java.util.ArrayList;
import java.util.List;

import main.models.ParadeBoard;
import main.models.cards.Card;
import main.models.player.Player;
import main.models.player.PlayerBoard;
import main.models.player.PlayerHand;

public class UIManager {

    private static final UIController controller = new UIController();

    public static void clearScreen() {
        controller.clearScreen();
    }

    public static void pauseExecution(int ms) {
        controller.pause(ms);
    }

    public static void displayMessage(String message) {
        controller.showMessage(message);
    }

    public static void displayIntroduction() {
        controller.showIntroduction();
    }

    public static void displayLoadingMessage(String message, int dots) {
        controller.showLoadingMessage(message, dots);
    }

    public static void displayFinalScores(List<Player> players, ParadeBoard board) {
        controller.showFinalScores(new ArrayList<>(players), board);
    }

    public static void displayPlayerTurn(Player player, ParadeBoard board) {
        controller.showPlayerView(player, board);
    }

    public static void displayTurnPrompt(Player player) {
        controller.showTurnPrompt(player);
    }

    public static void displayDiscardPrompt() {
        controller.showDiscardPrompt();
    }

    public static void displayBotAction(Player bot, int cardIndex) {
        controller.showBotAction(bot.getPlayerName(), cardIndex);
    }

    public static void displayFinalRoundMessage() {
        controller.showFinalRoundAnnouncement();
    }

    public static void displayGameInitMessage() {
        controller.showGameInitMessage();
    }

    public static void displayGameSetupMessage() {
        controller.showGameSetupMessage();
    }

    public static void displayStateExitMessage(String state) {
        controller.showStateExit(state);
    }

    public static void displayErrorMessage(String message) {
        controller.showError(message);
    }

    public static void displayAllColorsCollectedMessage() {
        controller.showAllColorsCollectedMessage();
    }

    public static void printFormattedHand(PlayerHand hand, Card selectedCard) {
        System.out.println(controller.getFormattedHandWithIndex(hand, selectedCard));
    }

    public static String getFormattedTurnDisplay(Player currentPlayer, ParadeBoard paradeBoard, int selectedIndex, String[] actionOptions, boolean onCardRow) {
        return controller.getTurnDisplay(currentPlayer, paradeBoard, selectedIndex, actionOptions, onCardRow);
    }

    public static String getFormattedPlayerStatus(List<Player> players, ParadeBoard paradeBoard) {
        return controller.getPlayerStatusDisplay(players, paradeBoard);
    }

    public static void displayScoreboard(List<Player> winners) {
        controller.getScoreboard(winners);
    }

    public static void displayWinner(Player winner) {
        controller.displayWinner(winner);
    }
    
    public static void displayTieResults(List<Player> tiedPlayers) {
        controller.displayTieResults(tiedPlayers);
    }

    
}

package main.helpers.ui;

import java.util.ArrayList;
import java.util.List;

import main.models.ParadeBoard;
import main.models.player.Player;

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
        UIManager.clearScreen();
        controller.showIntroduction();
        UIManager.clearScreen();
        
    }

    public static void displayInstructions() {
        controller.showMessage(GameRulesDisplay.constructGameInstructions());
    }

    public static void displayLoadingMessage(String message, int dots) {
        controller.showLoadingMessage(message, dots);
    }

    public static void displayFinalScores(List<Player> players, ParadeBoard board) {
        controller.showFinalScores(new ArrayList<>(players), board);
    }


    public static void displayDiscardPrompt() {
        System.out.println(controller.getDiscardPrompt());
    }

    public static void displayBotAction(Player bot, int cardIndex) {
        controller.getBotAction(bot.getPlayerName(), cardIndex);
    }

    public static void displayErrorMessage(String message) {
        System.out.println("ERROR: " + message);    }

    public static void printFormattedTurnDisplay(Player currentPlayer, ParadeBoard paradeBoard, int selectedIndex, String[] actionOptions, boolean onCardRow) {
        System.out.println(controller.getTurnDisplay(currentPlayer, paradeBoard, selectedIndex, actionOptions, onCardRow));
    }

    public static void printFormattedTurnDisplay(Player currentPlayer, ParadeBoard paradeBoard, String[] actionOptions) {
        System.out.println(controller.getTurnDisplay(currentPlayer, paradeBoard, actionOptions));
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
    
    public static void displayFinalRoundMessage() {
        System.out.println(DisplayEffects.BOLD+DisplayEffects.ANSI_PURPLE+
                           "Each player gets one final turn! No more cards will be drawn!"
                           +DisplayEffects.ANSI_RESET);
    }
    public static void displayFinalRoundTrigger(Boolean deckEmpty) {

        if(deckEmpty){
            System.out.println("The last card has been drawn");
        }
        else{
            System.out.println("One player has collected all 6 colours in his board!");
        }
        System.out.println(DisplayEffects.BOLD+DisplayEffects.YELLOW_BG+"Moving on to the final round!🙀🙀🙀"+DisplayEffects.ANSI_RESET);

    }



    
}

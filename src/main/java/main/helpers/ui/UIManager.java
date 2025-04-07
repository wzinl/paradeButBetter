package main.helpers.ui;

import java.util.List;

import main.models.ParadeBoard;
import main.models.player.Player;

public class UIManager {

    public static void clearScreen() {
        DisplayFactory.clearScreen();
    }

    public static void pauseExecution(int ms) {
        DisplayFactory.pause(ms);
    }

    public static void displayMessage(String message) {
        System.out.println(message);
}

    public static void displayIntroduction() {
        UIManager.clearScreen();
        DisplayFactory.showIntroduction();
        UIManager.clearScreen();
        
    }

    public static void displayInstructions() {
        System.out.println(GameRulesDisplay.constructGameInstructions());
    }

    public static void displayLoadingMessage(String message, int dots) {
        System.out.print(message);
        for (int i = 0; i < dots; i++) {
            pauseExecution(500);
            System.out.print(".");
        }
        System.out.println();
    }

    public static void displayFinalScores(List<Player> players, ParadeBoard board) {
        displayBoardOverview(players, board);
        System.out.println(DisplayFactory.showFinalScores(players, board));
    }


    public static void displayDiscardPrompt() {
        System.out.println(DisplayFactory.getDiscardPrompt());
    }

    public static void displayBotAction(Player bot, int cardIndex) {
        DisplayFactory.getBotAction(bot.getPlayerName(), cardIndex);
    }

    public static void displayErrorMessage(String message) {
        System.out.println("ERROR: " + message);    }

    public static void displayBoardOverview(List<Player> playerlist, ParadeBoard paradeBoard) {
        UIManager.clearScreen();
        System.out.println();
        System.out.println(DisplayEffects.BOLD+DisplayEffects.ANSI_CYAN + "Here are all of the players' boards:" + DisplayEffects.ANSI_RESET);
        System.out.println(DisplayFactory.getDisplayBoardOrverview(playerlist, paradeBoard));
    }

    public static void printFormattedTurnDisplay(Player currentPlayer, ParadeBoard paradeBoard, int selectedIndex, String[] actionOptions, boolean onCardRow) {
        System.out.println(DisplayFactory.getTurnDisplay(currentPlayer, paradeBoard, selectedIndex, actionOptions, onCardRow));
    }

    public static void printFormattedTurnDisplay(Player currentPlayer, ParadeBoard paradeBoard, String[] actionOptions) {
        System.out.println(DisplayFactory.getTurnDisplay(currentPlayer, paradeBoard, actionOptions));
    }

    public static void displayScoreboard(List<Player> winners) {
        System.out.println(DisplayFactory.getScoreboard(winners));
    }

    public static void displayWinner(Player winner) {
        DisplayFactory.displayWinner(winner);
    }
    
    public static void displayTieResults(List<Player> tiedPlayers) {
        DisplayFactory.displayTieResults(tiedPlayers);
    }
    
    public static void displayFinalRoundMessage() {
        clearScreen();
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

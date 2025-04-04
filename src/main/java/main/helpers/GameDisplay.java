package main.helpers;

import java.util.ArrayList;
import java.util.Scanner;

import main.models.ParadeBoard;
import main.models.cards.Card;
import main.models.player.Player;
import main.models.player.PlayerHand;
import main.models.player.PlayerBoard;

public class GameDisplay {
    
    
    public void clearScreen() {
        System.out.print("\033c");
    }

    public static void showIntroduction() {
        Scanner sc = new Scanner(System.in);
        System.out.print("\033[H\033[2J");
        System.out.flush();
        
        final int SLEEP_TIME = 100;
        
        try {
   
            String[] welcome = {
                "██╗    ██╗███████╗██╗      ██████╗ ██████╗ ███╗   ███╗███████╗    ████████╗ ██████╗ ",
                "██║    ██║██╔════╝██║     ██╔════╝██╔═══██╗████╗ ████║██╔════╝    ╚══██╔══╝██╔═══██╗",
                "██║ █╗ ██║█████╗  ██║     ██║     ██║   ██║██╔████╔██║█████╗         ██║   ██║   ██║",
                "██║███╗██║██╔══╝  ██║     ██║     ██║   ██║██║╚██╔╝██║██╔══╝         ██║   ██║   ██║",
                "╚███╔███╔╝███████╗███████╗╚██████╗╚██████╔╝██║ ╚═╝ ██║███████╗       ██║   ╚██████╔╝",
                " ╚══╝╚══╝ ╚══════╝╚══════╝ ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚══════╝       ╚═╝    ╚═════╝ "
            };
            
            for (String line : welcome) {
                System.out.println(line);
                Thread.sleep(SLEEP_TIME);
            }
            
            Thread.sleep(500);
            System.out.println();
            
            String[] title = {
                "██████╗  █████╗ ██████╗  █████╗ ██████╗ ███████╗",
                "██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔════╝",
                "██████╔╝███████║██████╔╝███████║██║  ██║█████╗  ",
                "██╔═══╝ ██╔══██║██╔══██╗██╔══██║██║  ██║██╔══╝  ",
                "██║     ██║  ██║██║  ██║██║  ██║██████╔╝███████╗",
                "╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝ ╚══════╝"
            };
            
            for (String line : title) {
                System.out.println(line);
                Thread.sleep(SLEEP_TIME);
            }
            
            Thread.sleep(500);
            System.out.println();
            System.out.println("========================================");
            Thread.sleep(300);
            
            System.out.println("\nPress enter to start your journey...");
            sc.nextLine();
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    
    public void showLoadingMessage(String message, int dotCount) {
        System.out.print(message);
        for (int i = 0; i < dotCount; i++) {
            try {
                Thread.sleep(10);
                System.out.print(".");
            } catch (InterruptedException e) {
                System.out.println("Sleep has been interrupted!");
            }
        }
        System.out.println();
    }
    
    
    public void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            System.out.println("Sleep has been interrupted!");
        }
    }
    
    
    public void showPlayerView(Player player, ParadeBoard paradeBoard) {
        System.out.println("========== " + player.getPlayerName() + "'s Turn ==========");
        System.out.println("\nParade Board:");
        showParadeBoard(paradeBoard);
        System.out.println("\nYour Hand:");
        showPlayerHand(player.getPlayerHand());
        System.out.println("\nYour Board:");
        showPlayerBoard(player.getPlayerBoard());
    }
    
    
    public void showParadeBoard(ParadeBoard paradeBoard) {
        System.out.println(paradeBoard.toString());
    }
    
    
    public void showPlayerHand(PlayerHand hand) {
        System.out.println(hand.toString());
    }
    
    
    public void showPlayerBoard(PlayerBoard board) {
        System.out.println(board.toString());
    }
    
    
    public void showTurnPrompt(Player currentPlayer) {
        System.out.println(currentPlayer.getPlayerName() + "'s turn");
    }
    
    
    public void showDiscardPrompt() {
        System.out.println("Discard 2 cards from hand.\n");
    }
    
    
    public String getCardSelectionPrompt(int handSize) {
        return String.format("Which card would you like to play? (%d to %d): ", 1, handSize);
    }
    
    
    public void showFinalScores(ArrayList<Player> players, ParadeBoard paradeBoard) {
        System.out.println("=========== FINAL SCORES ===========");
        for (Player player : players) {
            System.out.printf("%s has scored: %d%n", player.getPlayerName(), player.getPlayerScore());
        }
        
        if (players.get(0).getPlayerScore() == players.get(1).getPlayerScore()) {
            System.out.println("\nThe game is a tie!");
        } else {
            System.out.println("\n" + players.get(0).getPlayerName() + " wins!");
        }
    }
    
    
    public void showBotAction(String botName, int cardIndex) {
        System.out.printf("%s is going to play card #%d...\n", botName, cardIndex);
    }
    
    
    public void showFinalRoundAnnouncement() {
        System.out.println("Each player gets one final turn! No more cards will be drawn!");
    }
    
    
    public void showStateExit(String stateName) {
        System.out.println("Exiting " + stateName);
    }
    
    
    public void showGameInitMessage() {
        System.out.println("Game initialized");
    }
    
    
    public void showGameSetupMessage() {
        System.out.println("Game setup will now take place.");
        System.out.println();
    }
    
    
    public void showError(String message) {
        System.out.println("ERROR: " + message);
    }
    
    
    public void showAllColorsCollectedMessage() {
        System.out.println("You have collected all 6 colors! Moving on to the final round!");
    }
}
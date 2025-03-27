package main.gameStates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import main.context.GameContext;
import main.error.InvalidCardException;
import main.helpers.InputValidator;
import main.models.*;

public class GameEndState extends GameState {


    // Constructor that initializes player list and parade board from shared game
    // context
    public GameEndState(GameStateManager gsm, GameContext context) {
        super(gsm, context);
    }

    /**
     * Main entry point of the GameEndState.
     * Handles the final card discarding, card flipping, and scoring logic.
     */
    @Override
    public void enter() {
        try {
            System.out.println("Game End State entering");
            System.out.println("\033c");

            // Each player takes turns to discard 2 cards and adds the rest to their board
            // Start from finalPlayerIndex and loop through all players in turn order
            for (int i = 0; i < playerList.size(); i++) {
                int index = (finalPlayerIndex + i) % playerList.size();
                Player player = playerList.get(index);
                performDiscardPhase(player);
            }

            // Handling card flipping logic
            for (String color : Deck.colours) { // looping through each color
                int highestColor = -1;

                // more than 2 players
                if (playerList.size() > 2) {
                    // get player with the highest cards per color
                    for (Player player : playerList) {
                        int playerColor = player.getPlayerBoard().getCardNumberByColor(color);
                        highestColor = Math.max(highestColor, playerColor);
                    }
                    // flip face down all cards of players who have the highest count for this color
                    for (Player player : playerList) {
                        if (player.getPlayerBoard().getCardNumberByColor(color) == highestColor) {
                            for (Card card : player.getPlayerBoard().getPlayerBoardHash().get(color)) {
                                card.setIsFaceUp(false);
                            }
                        }
                    }
                    // 2 player logic: flip cards if color count difference is >= 2
                } else {
                    Player player1 = playerList.get(0);
                    Player player2 = playerList.get(1);
                    int player1Count = player1.getPlayerBoard().getCardNumberByColor(color);
                    int player2Count = player2.getPlayerBoard().getCardNumberByColor(color);

                    if (Math.abs(player1Count - player2Count) >= 2) {
                        PlayerBoard currentBoard = player1Count > player2Count ? player1.getPlayerBoard()
                                : player2.getPlayerBoard();
                        for (Card card : currentBoard.getPlayerBoardHash().get(color)) {
                            card.setIsFaceUp(false); // will not be counted in scoring
                        }
                    }

                }
            }

            System.out.print("Calculating score");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for(int i = 2; i >=0; i--){
                System.out.print(".");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println();
            
          

            ArrayList<Player> winners = new ArrayList<>(playerList);
            // sort by descending order so that the winner with the highest score comes
            // first
            Collections.sort(winners, Comparator.comparing(Player::calculateScore)); // lowest score is in first place

            System.out.println(getDisplay());

            revealWinnersWithPoints(winners);

            System.out.println();
            
        } catch (InvalidCardException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

    }

    private void performDiscardPhase(Player player) throws InvalidCardException {
        PlayerHand currentHand = player.getPlayerHand();
        PlayerBoard currentBoard = player.getPlayerBoard();

        System.out.println(player.getPlayerName() + "'s turn");
        // System.out.println("Current Game State");
        getDisplay(player);
        System.out.println("Discard 2 cards from hand.\n");

        System.out.println(currentHand); // Show full hand

        // Discard first card
        int discardIndex1 = InputValidator.getIntInRange("Select first card to discard: ",
                1, currentHand.getCardList().size()) - 1;
        Card discardCard1 = currentHand.getCardList().get(discardIndex1);
        currentHand.removeCard(discardCard1);

        System.out.println(currentHand); // Show updated hand

        // Discard second card
        int discardIndex2 = InputValidator.getIntInRange("Select second card to discard: ",
                1, currentHand.getCardList().size()) - 1;
        Card discardCard2 = currentHand.getCardList().get(discardIndex2);
        currentHand.removeCard(discardCard2);

        // Add any remaining cards to the player’s board
        for (Card card : currentHand.getCardList()) {
            currentBoard.addCard(card);
        }

        // Clear the hand
        currentHand.getCardList().clear();
        System.out.println("\033c");

    }

    public static void countdown(int seconds) throws InterruptedException {
        for (int i = seconds; i > 0; i--) {
            System.out.print("⏳ " + i + "... ");
            Thread.sleep(1000);
        }
        System.out.println("\n🎉 And the results are in!");
    }

    public static void revealWinnersWithPoints(ArrayList<Player> rankedPlayers) throws InterruptedException {
        int size = rankedPlayers.size();
    
        System.out.println("Calculating results...");
        countdown(3);
        System.out.println("\n=== FINAL STANDINGS ===\n");
    
        if (size == 0) {
            System.out.println("No players found.");
            return;
        }
    
        Player first = rankedPlayers.get(size - 1);
        System.out.println("       <<< WINNER >>>");
        System.out.println("      " + first.getPlayerName() + " (" + first.getPlayerScore() + " pts)");
        System.out.println("-----------------------------");
    
 
        if (size >= 2) {
            Player second = rankedPlayers.get(size - 2);
            System.out.print("2nd: " + second.getPlayerName() + " (" + second.getPlayerScore() + " pts)");
            if (size >= 3) {
                Player third = rankedPlayers.get(size - 3);
                System.out.print("     3rd: " + third.getPlayerName() + " (" + third.getPlayerScore() + " pts)");
            }
            System.out.println();
        }
    
        if (size > 3) {
            System.out.println("\nOther Participants:");
            for (int i = 0; i < size - 3; i++) {
                Player p = rankedPlayers.get(i);
                int rank = size - i;
                System.out.println(rank + "th: " + p.getPlayerName() + " (" + p.getPlayerScore() + " pts)");
            }
        }
    }
    

    @Override
    public void exit() {

    }

    // public String getDisplay(Player currentPlayer) {
    //     String result = "";

    //     // Display the parade board
    //     result += "Parade Board:\n";
    //     result += paradeBoard + "\n".repeat(3);

    //     result += "Here is your board:\n";
    //     result += getPlayerBoardDisplay(currentPlayer.getPlayerBoard());
    //     result += "Here is your hand:\n";
    //     result += getHandDisplay(currentPlayer.getPlayerHand());
    //     result += "\n";
    //     return result;
    // }

    // public String getDisplay() {
    //     String result = "";

    //     // Display the parade board
    //     result += "Parade Board:\n";
    //     result += paradeBoard + "\n".repeat(3);

    //     for (Player curr : playerList) {
    //         result += curr.getPlayerName() + "'s board\n";
    //         result += getPlayerBoardDisplay(curr.getPlayerBoard());
    //         if (!curr.getPlayerHand().getCardList().isEmpty()) {
    //             result += curr.getPlayerName() + "'s hand\n";
    //             result += getHandDisplay(curr.getPlayerHand());
    //         }

    //     }

    //     result += "\n";
    //     return result;
    // }

    // public String getHandDisplay(PlayerHand playerHand) {
    //     String result = "";

    //     result += playerHand + "\n".repeat(3);
    //     return result;
    // }

    // public String getPlayerBoardDisplay(PlayerBoard currentplayerBoard) {
    //     String result = "";
    //     if (currentplayerBoard.isEmpty()) {
    //         System.out.println();
    //         result += "Your playerboard is empty.\n";
    //     } else {
    //         result += currentplayerBoard + "\n";
    //     }
    //     return result;
    // }
}

package main.helpers;

import java.io.Console;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import main.models.ParadeBoard;
import main.models.cards.Card;
import main.models.player.Player;
import main.models.player.PlayerHand;

// import org.jline.reader.LineReader;
// import org.jline.reader.LineReaderBuilder;
// import org.jline.reader.EndOfFileException;
// import org.jline.reader.UserInterruptException;
// import org.jline.terminal.Terminal; 
// import org.jline.terminal.TerminalBuilder;

public class MenuSelector {

    public static String turnSelect(ParadeBoard paradeBoard, Player currentPlayer, List<String> actionOptions) {
        int selectedIndex = 0;
        PlayerHand currHand = currentPlayer.getPlayerHand();
        List<Card> cardList = currHand.getCardList();
        int handSize = cardList.size();
        int actionOptionsCount = actionOptions.size();
        boolean onCardRow = true; // true = hand row, false = action row


        try {
            Console console = System.console();
            if (console == null) {
                throw new UnsupportedOperationException("Console not available. Run from a terminal.");
            }

            Reader reader = console.reader();

            while (true) {
                // Clear screen
                System.out.print("\033[H\033[2J");
                System.out.flush();
                Card selectedCard = cardList.get(selectedIndex);

                System.out.println(ScreenUtils.getDisplay(currentPlayer, paradeBoard, selectedCard));

                // Spacer
                System.out.println("\n");

                // Bottom row highlight
                for (int i = 0; i < actionOptionsCount; i++) {
                    System.out.print((!onCardRow && i == selectedIndex ? "  ^   " : "      "));
                }
                System.out.println();

                // Action options
                for (String option : actionOptions) {
                    System.out.print(String.format("%-10s", option));
                }
                System.out.println();

                // Footer
                System.out.println("\nUse ←/→ to move, ↑/↓ to switch rows, Enter to select.");

                int first = reader.read();

                if (first == 27) { // ESC
                    int second = reader.read();
                    if (second == 91) {
                        int third = reader.read();
                        switch (third) {
                            case 65 -> {
                                // up
                                if (!onCardRow) {
                                    System.out.println("up ENTERED");
                                    onCardRow = true;
                                    selectedIndex = Math.min(selectedIndex, handSize - 1);
                                }
                            }
                            case 66 -> {
                                // down
                                if (onCardRow) {
                                    System.out.println("down ENTERED");
                                    onCardRow = false;
                                    selectedIndex = Math.min(selectedIndex, handSize - 1);
                                }
                            }
                            case 67 -> {
                                // right
                                System.out.println("right ENTERED");
                                selectedIndex++;
                                if (onCardRow && selectedIndex >= handSize) {
                                    selectedIndex = 0;
                                } else if (!onCardRow && selectedIndex >= actionOptionsCount) {
                                    selectedIndex = 0;
                                }
                            }
                            case 68 -> {
                                // left
                                System.out.println("left ENTERED");
                                selectedIndex--;
                                if (onCardRow && selectedIndex < 0) {
                                    selectedIndex = handSize - 1;
                                } else if (!onCardRow && selectedIndex < 0) {
                                    selectedIndex = actionOptionsCount - 1;
                                }
                            }
                        }
                    }
                } else if (first == '\n' || first == '\r') {
                    System.out.println("NEWLINE ENTERED");
                    if (onCardRow) {
                        return String.valueOf(selectedIndex); // e.g., card:3 → 4th card
                    } else {
                        return "action:" + actionOptions.get(selectedIndex); // e.g., action:Save Game
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Input reading error", e);
        }
    }

    // public static String turnSelect(ParadeBoard paradeBoard, Player currentPlayer, List<String> actionOptions) {
    //     int selectedIndex = 0;
    //     PlayerHand currHand = currentPlayer.getPlayerHand();
    //     List<Card> cardList = currHand.getCardList();
    //     int handSize = cardList.size();
    //     int actionOptionsCount = actionOptions.size();
    //     boolean onCardRow = true;

    //     try {
    //         Terminal terminal = TerminalBuilder.builder().system(true).build();
    //         LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();

    //         terminal.enterRawMode(); // read character-by-character, not line-by-line
    //         while (true) {
    //             // Clear screen
    //             System.out.print("\033[H\033[2J");
    //             System.out.flush();
    //             Card selectedCard = cardList.get(selectedIndex);
    //             System.out.println(ScreenUtils.getDisplay(currentPlayer, paradeBoard, selectedCard));
    //             System.out.println();

    //             // Highlight arrow row
    //             for (int i = 0; i < actionOptionsCount; i++) {
    //                 System.out.print((!onCardRow && i == selectedIndex ? "  ^   " : "      "));
    //             }
    //             System.out.println();

    //             for (String option : actionOptions) {
    //                 System.out.print(String.format("%-10s", option));
    //             }
    //             System.out.println();
    //             System.out.println("\nUse ←/→ to move, ↑/↓ to switch rows, Enter to select.");

    //             int key = reader.readCharacter();

    //             switch (key) {
    //                 case 10, 13 -> { // Enter
    //                     if (onCardRow) {
    //                         return String.valueOf(selectedIndex);
    //                     } else {
    //                         return "action:" + actionOptions.get(selectedIndex);
    //                     }
    //                 }
    //                 case 27 -> { // Arrow keys: start with ESC (27)
    //                     int next1 = reader.readCharacter();
    //                     int next2 = reader.readCharacter();

    //                     if (next1 == 91) {
    //                         switch (next2) {
    //                             case 65 -> { // up
    //                                 onCardRow = true;
    //                                 selectedIndex = Math.min(selectedIndex, handSize - 1);
    //                             }
    //                             case 66 -> { // down
    //                                 onCardRow = false;
    //                                 selectedIndex = Math.min(selectedIndex, actionOptionsCount - 1);
    //                             }
    //                             case 67 -> { // right
    //                                 selectedIndex++;
    //                                 if (onCardRow && selectedIndex >= handSize) {
    //                                     selectedIndex = 0;
    //                                 } else if (!onCardRow && selectedIndex >= actionOptionsCount) {
    //                                     selectedIndex = 0;
    //                                 }
    //                             }
    //                             case 68 -> { // left
    //                                 selectedIndex--;
    //                                 if (onCardRow && selectedIndex < 0) {
    //                                     selectedIndex = handSize - 1;
    //                                 } else if (!onCardRow && selectedIndex < 0) {
    //                                     selectedIndex = actionOptionsCount - 1;
    //                                 }
    //                             }
    //                         }
    //                     }
    //                 }
    //                 default -> {
    //                     // Ignore other keys
    //                 }
    //             }
    //         }

    //     } catch (UserInterruptException | EndOfFileException e) {
    //         System.out.println("Input cancelled.");
    //         return null;
    //     } catch (Exception e) {
    //         throw new RuntimeException("Input reading error", e);
    //     }
    // }
}

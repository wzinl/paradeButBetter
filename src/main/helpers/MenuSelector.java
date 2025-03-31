package main.helpers;

import java.io.Console;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class MenuSelector {
    public static String getCardOrActionSelection(String boardDisplay, String playerBoard,
                                              List<String> handDisplay, List<String> actionOptions) {
    int selectedIndex = 0;
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

            // Print game state
            System.out.println(boardDisplay);
            System.out.println(playerBoard);
            System.out.println("\nYour Hand:");

            // Top row highlight
            for (int i = 0; i < handDisplay.size(); i++) {
                System.out.print((onCardRow && i == selectedIndex ? "  ^   " : "      "));
            }
            System.out.println();

            // Hand display
            for (String card : handDisplay) {
                System.out.print(String.format("%-6s", card));
            }
            System.out.println();

            // Hand labels
            for (int i = 0; i < handDisplay.size(); i++) {
                System.out.print(String.format("{%d}   ", i + 1));
            }
            System.out.println();

            // Spacer
            System.out.println("\n");

            // Bottom row highlight
            for (int i = 0; i < actionOptions.size(); i++) {
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
                        case 65: // ↑
                            if (!onCardRow) {
                                onCardRow = true;
                                selectedIndex = Math.min(selectedIndex, handDisplay.size() - 1);
                            }
                            break;
                        case 66: // ↓
                            if (onCardRow) {
                                onCardRow = false;
                                selectedIndex = Math.min(selectedIndex, actionOptions.size() - 1);
                            }
                            break;
                        case 67: // →
                            selectedIndex++;
                            if (onCardRow && selectedIndex >= handDisplay.size()) {
                                selectedIndex = 0;
                            } else if (!onCardRow && selectedIndex >= actionOptions.size()) {
                                selectedIndex = 0;
                            }
                            break;
                        case 68: // ←
                            selectedIndex--;
                            if (onCardRow && selectedIndex < 0) {
                                selectedIndex = handDisplay.size() - 1;
                            } else if (!onCardRow && selectedIndex < 0) {
                                selectedIndex = actionOptions.size() - 1;
                            }
                            break;
                    }
                }
            } else if (first == '\n' || first == '\r') {
                if (onCardRow) {
                    return "card:" + selectedIndex; // e.g., card:3 → 4th card
                } else {
                    return "action:" + actionOptions.get(selectedIndex); // e.g., action:Save Game
                }
            }
        }

    } catch (IOException e) {
        throw new RuntimeException("Input reading error", e);
    }
}

}

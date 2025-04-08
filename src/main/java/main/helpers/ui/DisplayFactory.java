package main.helpers.ui;

import java.util.ArrayList;
import java.util.List;

import main.models.ParadeBoard;
import main.models.cards.Card;
import main.models.player.Player;
import main.models.player.PlayerBoard;
import main.models.player.PlayerHand;

/**
 * DisplayFactory handles all low-level formatting and UI presentation logic.
 * It supports displaying player hands, game states, scoreboards, and animations
 * using ANSI styling and custom formatting.
 */
public class DisplayFactory {

    /** Clears the console using ANSI escape code. */
    public static void clearScreen() {
        System.out.print("\033c");
    }

    /** Pauses execution for a given number of milliseconds. */
    public static void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /** Shows the stylized ASCII game introduction splash screen. */
    public static void showIntroduction() {
        // Create the header art
        StringBuilder openingMsg = new StringBuilder();
        openingMsg.append("    ██████╗ ██████╗ ██████╗  █████╗ ██████╗ ███████╗\n")
                  .append("     ██╔══██╗██╔══██╗██╔══██╗ ██╔══██╗██╔══██╗██╔════╝\n")
                  .append("     ██████╔╝███████║██████╔╝ ███████║██║  ██║█████╗ \n")
                  .append("     ██╔═══╝ ██╔══██║██╔══██╝ ██╔══██║██║  ██║██╔══╝  \n")
                  .append("     ██║     ██║  ██║██║  ██║ ██║  ██║██████╔╝███████╗\n")
                  .append("     ╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝ ╚══════╝");

        try {
            System.out.println("\n\n" + DisplayEffects.BOLD + "------------------------Welcome To-------------------------" + DisplayEffects.ANSI_RESET);
            DisplayEffects.typeWriter(openingMsg.toString(), 6);
            System.out.println(DisplayEffects.BOLD + "-----------------------------------------------------------" + DisplayEffects.ANSI_RESET);
            DisplayEffects.blinkingEffect(DisplayEffects.BOLD + DisplayEffects.ANSI_RED + "                       [Enter Game]                         " + DisplayEffects.ANSI_RESET);
            DisplayEffects.blinkingEffect(DisplayEffects.BOLD + DisplayEffects.ANSI_RED + "                    [Game Instructions]                      " + DisplayEffects.ANSI_RESET);
        } catch (InterruptedException e) {
            System.out.println("An error has occurred trying to create introduction display!");
        }
    }

    /** Generates a card selection prompt based on hand size. */
    public static String getCardSelectionPrompt(int handSize) {
        return String.format(DisplayEffects.BOLD + DisplayEffects.ANSI_GREEN + "Which card would you like to play? (%d to %d): " + DisplayEffects.ANSI_RESET, 1, handSize);
    }

    /** Builds the final scores display with winner or tie result. */
    public static String showFinalScores(List<Player> players, ParadeBoard paradeBoard) {
        StringBuilder finalScores = new StringBuilder();
        finalScores.append(DisplayEffects.BOLD)
                   .append(DisplayEffects.ANSI_MAGENTA)
                   .append("=========== FINAL SCORES ===========")
                   .append(DisplayEffects.ANSI_RESET)
                   .append("\n");

        for (Player player : players) {
            finalScores.append(String.format("%s has scored: %d%n", player.getPlayerName(), player.getPlayerScore()));
        }

        // Handles tie display logic
        if (players.get(0).getPlayerScore() == players.get(1).getPlayerScore()) {
            finalScores.append("\nThe game is a tie!");
        } else {
            finalScores.append("\n").append(players.get(0).getPlayerName()).append(" wins!");
        }

        return finalScores.toString();
    }

    /** Returns formatted bot action announcement. */
    public static String getBotAction(String botName, int cardIndex) {
        return String.format(DisplayEffects.BOLD + DisplayEffects.ANSI_PURPLE +
                "%s is going to play Card %d" + DisplayEffects.ANSI_RESET + "\n", botName, cardIndex);
    }

    /**
     * Renders the player's hand with optional highlighting of a selected card.
     */
    public static String getFormattedHandWithIndex(PlayerHand hand, Card selectedCard) {
        List<Card> cards = hand.getCardList();
        if (cards.isEmpty()) return "No cards in hand.\n";

        List<String[]> cardLines = new ArrayList<>();
        List<String> colorCodes = new ArrayList<>();

        // Generate ANSI-coded versions of each card line
        for (Card card : cards) {
            colorCodes.add(
                selectedCard != null && card.equals(selectedCard)
                    ? card.getHighlightedAnsiColorCode()
                    : card.getAnsiColorCode()
            );
            cardLines.add(card.toString().replaceAll("\\u001B\\[[;\\d]*m", "").split("\n"));
        }

        StringBuilder result = new StringBuilder();
        int linesPerCard = cardLines.get(0).length;

        // Build card rows line by line
        for (int line = 0; line < linesPerCard; line++) {
            for (int i = 0; i < cards.size(); i++) {
                result.append(colorCodes.get(i))
                      .append(cardLines.get(i)[line])
                      .append("\u001B[0m  ");
            }
            result.append("\n");
        }

        result.append(getIndexString(cards.size()));
        return result.toString();
    }

    /** Returns index line (e.g. {1} {2}...) aligned under cards. */
    private static String getIndexString(int size) {
        StringBuilder index = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int cardLength = 10;
            String cardIndexStr = "{" + (i + 1) + "}";
            int indexLength = cardIndexStr.length();
            int leftPadding = (cardLength - indexLength) / 2;
            int rightPadding = cardLength - indexLength - leftPadding;
            index.append(" ".repeat(leftPadding)).append(cardIndexStr).append(" ".repeat(rightPadding)).append("  ");
        }
        return index.toString();
    }

    /** Prompt to inform players about endgame discard phase. */
    public static String getDiscardPrompt() {
        return "We have reached the end of the game!\nEach player will choose two cards to discard. The rest of your hand will go into your player board.\n";
    }

    /** Displays a simplified turn message (used when no card is selected). */
    public static String getTurnDisplay(Player currentPlayer, ParadeBoard paradeBoard, String[] actionOptions) {
        return getPlayerDisplay(currentPlayer, paradeBoard) + "\n";
    }

    /**
     * Displays a fully formatted turn view with highlighted card or action.
     */
    public static String getTurnDisplay(Player currentPlayer, ParadeBoard paradeBoard, int selectedIndex, String[] actionOptions, Boolean onCardRow) {
        StringBuilder result = new StringBuilder();
        result.append("==========" + DisplayEffects.BOLD + DisplayEffects.ANSI_GREEN)
              .append(currentPlayer.getPlayerName())
              .append("'s turn!" + DisplayEffects.ANSI_RESET + "==========\n\n");

        PlayerHand currHand = currentPlayer.getPlayerHand();
        List<Card> cardList = currHand.getCardList();

        result.append(
            onCardRow
                ? getPlayerDisplay(currentPlayer, paradeBoard, cardList.get(selectedIndex))
                : getPlayerDisplay(currentPlayer, paradeBoard)
        ).append("\n\n");

        // Format action options (highlight selection if applicable)
        for (int i = 0; i < actionOptions.length; i++) {
            boolean isSelected = (i == selectedIndex);
            String option = actionOptions[i];
            result.append(isSelected && !onCardRow ? String.format("[ %s ]", option) : String.format("  %s  ", option)).append("  ");
        }

        result.append(DisplayEffects.BOLD + "\n\nUse A/D to move, W/S to switch rows, Enter to select." + DisplayEffects.ANSI_RESET);
        return result.toString();
    }

    /** Displays player board and hand with optional selected card. */
    private static String getPlayerDisplay(Player player, ParadeBoard paradeBoard) {
        return getPlayerDisplay(player, paradeBoard, null);
    }

    /** Displays full game state for one player (hand, board, parade). */
    private static String getPlayerDisplay(Player player, ParadeBoard paradeBoard, Card selectedCard) {
        StringBuilder result = new StringBuilder();
        result.append("\u001B[0mParade:").append(paradeBoard.toString()).append("\n\n")
              .append(DisplayEffects.BOLD + DisplayEffects.ANSI_UNDERLINE)
              .append(player.getPlayerName());

        if (player.getPlayerBoard().isEmpty()) {
            result.append("'s board is empty." + DisplayEffects.ANSI_RESET);
        } else {
            result.append("'s board" + DisplayEffects.ANSI_RESET + "\n")
                  .append(player.getPlayerBoard().toString());
        }

        result.append("\n\n")
              .append(DisplayEffects.BOLD + DisplayEffects.ANSI_UNDERLINE)
              .append(player.getPlayerName())
              .append("'s hand" + DisplayEffects.ANSI_RESET + "\n\n")
              .append(getFormattedHandWithIndex(player.getPlayerHand(), selectedCard));

        return result.toString();
    }

    /** Renders the final scoreboard based on sorted players list. */
    public static String getScoreboard(List<Player> winners) {
        StringBuilder scoreboard = new StringBuilder();
        scoreboard.append("\n=========== FINAL SCORES ===========\n\n");

        int maxNameLength = winners.stream()
                                   .mapToInt(p -> p.getPlayerName().length())
                                   .max()
                                   .orElse(10);

        int nameWidth = Math.max(maxNameLength + 2, 12);
        int scoreWidth = 8;
        int positionWidth = 10;

        String divider = "-".repeat(positionWidth) + "+" + "-".repeat(nameWidth) + "+" + "-".repeat(scoreWidth);
        scoreboard.append(String.format("%-" + positionWidth + "s | %" + nameWidth + "s | %" + scoreWidth + "s%n", "POSITION", "PLAYER", "SCORE"))
                  .append(divider).append("\n");

        int displayRank = 1;
        for (int i = 0; i < winners.size(); i++) {
            Player player = winners.get(i);
            String position = (i > 0 && player.getPlayerScore() == winners.get(i - 1).getPlayerScore()) ? "" : switch (displayRank++) {
                case 1 -> "1st";
                case 2 -> "2nd";
                case 3 -> "3rd";
                default -> displayRank + "th";
            };

            scoreboard.append(String.format("%-" + positionWidth + "s | %" + nameWidth + "s | %" + scoreWidth + "d%n", position, player.getPlayerName(), player.getPlayerScore()));
        }

        scoreboard.append("\n").append("=".repeat(divider.length())).append("\n");
        return scoreboard.toString();
    }

    /** Displays a boxed, centered winner announcement. */
    public static void displayWinner(Player winner) {
        String winnerName = winner.getPlayerName();
        int winnerScore = winner.getPlayerScore();
        int boxWidth = 40;

        String topBorder = "╔" + "═".repeat(boxWidth - 2) + "╗";
        String middleBorder = "╠" + "═".repeat(boxWidth - 2) + "╣";
        String bottomBorder = "╚" + "═".repeat(boxWidth - 2) + "╝";

        System.out.println(topBorder);
        System.out.println(formatBoxLine("CHAMPION", boxWidth));
        System.out.println(middleBorder);
        System.out.println(formatBoxLine(winnerName, boxWidth));
        System.out.println(formatBoxLine(winnerScore + " POINTS", boxWidth));
        System.out.println(bottomBorder);
        System.out.println(centerText("CONGRATULATIONS!", boxWidth));
    }

    /** Displays tie results among top players. */
    public static void displayTieResults(List<Player> winners) {
        int topScore = winners.get(0).getPlayerScore();
        int nameWidth = Math.max(
                winners.stream().filter(p -> p.getPlayerScore() == topScore).mapToInt(p -> p.getPlayerName().length()).max().orElse(10) + 2,
                12);

        System.out.println("TIED RESULTS");
        for (Player p : winners) {
            if (p.getPlayerScore() != topScore) break;
            System.out.printf("• %-" + (nameWidth - 2) + "s - %d points%n", p.getPlayerName(), p.getPlayerScore());
        }
    }

    /** Formats a centered line inside a decorated box. */
    private static String formatBoxLine(String text, int width) {
        int contentWidth = width - 4;
        int padding = contentWidth - text.length();
        int leftPad = padding / 2;
        int rightPad = padding - leftPad;
        return "║ " + " ".repeat(leftPad) + text + " ".repeat(rightPad) + " ║";
    }

    /** Centers plain text (used for messages outside boxes). */
    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text;
    }

    /** Displays all player boards and current parade. */
    public static String getDisplayBoardOverview(List<Player> playerlist, ParadeBoard paradeBoard) {
        String result = "";
        for (Player player : playerlist) {
            PlayerBoard board = player.getPlayerBoard();
            result += DisplayEffects.BOLD + DisplayEffects.ANSI_GREEN + player.getPlayerName() + DisplayEffects.ANSI_RESET;
            result += board.isEmpty() ? "'s Player Board is empty." : "'s Player Board:" + board;
            result += "\n";
        }
        result += "Parade:\n" + paradeBoard + "\n";
        return result;
    }

    /** Prints the discard action of a bot. */
    public static void getBotDiscardDisplay(Player bot, Card discardedCard) {
        System.out.println(DisplayEffects.BOLD + DisplayEffects.ANSI_PURPLE + bot.getPlayerName() + " discarded:" + DisplayEffects.ANSI_RESET);
        System.out.println(discardedCard);
        System.out.println("Updated hand:");
        System.out.println(getFormattedHandWithIndex(bot.getPlayerHand(), null));
    }

    /** Displays a detailed breakdown when a card is played. */
    public static void getCardPlayDisplay(Player player, Card chosenCard, ParadeBoard paradeBoard, PlayerBoard playerBoard, ArrayList<Card> removedCards) {
        UIManager.clearScreen();

        System.out.println(player.getPlayerName() + " has played: ");
        System.out.println(chosenCard);
        UIManager.pauseExecution(1000);

        System.out.println("Updated Parade:");
        System.out.println(paradeBoard.toString(removedCards, chosenCard));
        System.out.println();

        if (playerBoard.isEmpty()) {
            System.out.println(player.getPlayerName() + "'s Playerboard is empty.");
        } else {
            System.out.println("Updated playerboard:");
            System.out.println(playerBoard);
        }

        System.out.println("Updated Hand:");
        System.out.println(player.getPlayerHand());

        UIManager.pauseExecution(5000);
        UIManager.clearScreen();
    }
}

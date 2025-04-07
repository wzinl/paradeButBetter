package main.helpers.ui;

import java.util.ArrayList;
import java.util.List;

import main.models.ParadeBoard;
import main.models.cards.Card;
import main.models.player.Player;
import main.models.player.PlayerBoard;
import main.models.player.PlayerHand;

public class DisplayFactory {

    public static void clearScreen() {
        System.out.print("\033c");
    }

    public static void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void showIntroduction() {
        StringBuilder openingMsg = new StringBuilder();
        openingMsg.append("    ██████╗ ██████╗ ██████╗  █████╗ ██████╗ ███████╗\n")
                .append("     ██╔══██╗██╔══██╗██╔══██╗ ██╔══██╗██╔══██╗██╔════╝\n")
                .append("     ██████╔╝███████║██████╔╝ ███████║██║  ██║█████╗ \n")
                .append("     ██╔═══╝ ██╔══██║██╔══██╝ ██╔══██║██║  ██║██╔══╝  \n")
                .append("     ██║     ██║  ██║██║  ██║ ██║  ██║██████╔╝███████╗\n")
                .append("     ╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝ ╚══════╝");

        try {
            System.out.printf("\n\n");
            System.out.println(DisplayEffects.BOLD + "------------------------Welcome To-------------------------" + DisplayEffects.ANSI_RESET);
            System.out.println();
            DisplayEffects.typeWriter(openingMsg.toString(), 6);
            System.out.println(DisplayEffects.BOLD + "-----------------------------------------------------------" + DisplayEffects.ANSI_RESET);
            System.out.println();
            DisplayEffects.blinkingEffect(DisplayEffects.BOLD + DisplayEffects.ANSI_RED + "                       [Enter Game]                         " + DisplayEffects.ANSI_RESET);
            DisplayEffects.blinkingEffect(DisplayEffects.BOLD + DisplayEffects.ANSI_RED + "                    [Game Instructions]                      " + DisplayEffects.ANSI_RESET);
        } catch (InterruptedException e) {
            System.out.println("An error has occurred trying to create introduction display!");
        }
    }

    public static String getCardSelectionPrompt(int handSize) {
        return String.format(DisplayEffects.BOLD + DisplayEffects.ANSI_GREEN + "Which card would you like to play? (%d to %d): " + DisplayEffects.ANSI_RESET, 1, handSize);
    }

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
    
        if (players.get(0).getPlayerScore() == players.get(1).getPlayerScore()) {
            finalScores.append("\nThe game is a tie!");
        } else {
            finalScores.append("\n").append(players.get(0).getPlayerName()).append(" wins!");
        }
    
        return finalScores.toString();
    }

    public static String getBotAction(String botName, int cardIndex) {
        return String.format(DisplayEffects.BOLD + DisplayEffects.ANSI_PURPLE +
                "%s is going to play Card %d"
                + DisplayEffects.ANSI_RESET + "\n",
                botName, cardIndex);
    }

    public static String getFormattedHandWithIndex(PlayerHand hand, Card selectedCard) {
        List<Card> cards = hand.getCardList();
        if (cards.isEmpty())
            return "No cards in hand.\n";

        List<String[]> cardLines = new ArrayList<>();
        List<String> colorCodes = new ArrayList<>();

        for (Card card : cards) {
            colorCodes.add(
                    selectedCard != null && card.equals(selectedCard)
                            ? card.getHighlightedAnsiColorCode()
                            : card.getAnsiColorCode());
            cardLines.add(card.toString().replaceAll("\\u001B\\[[;\\d]*m", "").split("\n"));
        }

        StringBuilder result = new StringBuilder();
        int linesPerCard = cardLines.get(0).length;

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

    private static String getIndexString(int size) {
        StringBuilder index = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int cardLength = 10;
            String cardIndexStr = "{" + (i + 1) + "}";
            int indexLength = cardIndexStr.length();
            int leftPadding = (cardLength - indexLength) / 2;
            int rightPadding = cardLength - indexLength - leftPadding;

            index.append(" ".repeat(leftPadding))
                    .append(cardIndexStr)
                    .append(" ".repeat(rightPadding))
                    .append("  ");
        }
        return index.toString();
    }

    public static String getDiscardPrompt() {
        StringBuilder result = new StringBuilder();
        result.append("We have reached the end of the game!\n")
                .append("Each player will choose two cards to discard. The rest of your hand will go into your player board.\n");
        return result.toString();
    }

    public static String getTurnDisplay(Player currentPlayer, ParadeBoard paradeBoard, String[] actionOptions) {
        StringBuilder result = new StringBuilder();

        result.append(getPlayerDisplay(currentPlayer, paradeBoard))
                .append("\n");

        return result.toString();
    }

    public static String getTurnDisplay(Player currentPlayer, ParadeBoard paradeBoard, int selectedIndex,
                                         String[] actionOptions, Boolean onCardRow) {
        StringBuilder result = new StringBuilder();

        result.append("==========" + DisplayEffects.BOLD + DisplayEffects.ANSI_GREEN)
                .append(currentPlayer.getPlayerName())
                .append("'s turn!" + DisplayEffects.ANSI_RESET + "==========\n\n");

        PlayerHand currHand = currentPlayer.getPlayerHand();
        List<Card> cardList = currHand.getCardList();

        if (onCardRow) {
            Card selectedCard = cardList.get(selectedIndex);
            result.append(getPlayerDisplay(currentPlayer, paradeBoard, selectedCard))
                    .append("\n");
        } else {
            result.append(getPlayerDisplay(currentPlayer, paradeBoard))
                    .append("\n");
        }

        result.append("\n\n");

        String[] formattedOptions = new String[actionOptions.length];
        for (int i = 0; i < actionOptions.length; i++) {
            String option = actionOptions[i];
            boolean isSelected = (i == selectedIndex);

            formattedOptions[i] = isSelected && !onCardRow ? String.format("[ %s ]", option)
                    : String.format("  %s  ", option);
        }

        for (String opt : formattedOptions) {
            result.append(opt)
                    .append("  ");
        }

        result.append(DisplayEffects.BOLD + "\n\nUse A/D to move, W/S to switch rows, Enter to select." + DisplayEffects.ANSI_RESET);
        return result.toString();
    }

    private static String getPlayerDisplay(Player player, ParadeBoard paradeBoard) {
        return getPlayerDisplay(player, paradeBoard, null);
    }

    private static String getPlayerDisplay(Player player, ParadeBoard paradeBoard, Card selectedCard) {
        StringBuilder result = new StringBuilder();
        result.append("\u001B[0mParade:")
              .append(paradeBoard.toString())
              .append("\n\n")
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
    
        scoreboard.append(String.format("%-" + positionWidth + "s | %" + nameWidth + "s | %" + scoreWidth + "s%n",
                "POSITION", "PLAYER", "SCORE"));
        scoreboard.append(divider).append("\n");
    
        int displayRank = 1;
    
        for (int i = 0; i < winners.size(); i++) {
            Player player = winners.get(i);
            String position;
    
            if (i > 0 && player.getPlayerScore() == winners.get(i - 1).getPlayerScore()) {
                position = "";
            } else {
                position = switch (displayRank) {
                    case 1 -> "1st";
                    case 2 -> "2nd";
                    case 3 -> "3rd";
                    default -> displayRank + "th";
                };
                displayRank++;
            }
    
            scoreboard.append(String.format("%-" + positionWidth + "s | %" + nameWidth + "s | %" + scoreWidth + "d%n",
                    position, player.getPlayerName(), player.getPlayerScore()));
        }
    
        scoreboard.append("\n").append("=".repeat(divider.length())).append("\n");
    
        return scoreboard.toString();
    }

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

    public static void displayTieResults(List<Player> winners) {
        int topScore = winners.get(0).getPlayerScore();
        int nameWidth = Math.max(
                winners.stream()
                        .filter(p -> p.getPlayerScore() == topScore)
                        .mapToInt(p -> p.getPlayerName().length())
                        .max()
                        .orElse(10) + 2,
                12);

        System.out.println("TIED RESULTS");

        for (Player p : winners) {
            if (p.getPlayerScore() != topScore)
                break;
            System.out.printf("• %-" + (nameWidth - 2) + "s - %d points%n",
                    p.getPlayerName(), p.getPlayerScore());
        }
    }

    private static String formatBoxLine(String text, int width) {
        int contentWidth = width - 4;
        int padding = contentWidth - text.length();
        int leftPad = padding / 2;
        int rightPad = padding - leftPad;

        return "║ " + " ".repeat(leftPad) + text + " ".repeat(rightPad) + " ║";
    }

    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text;
    }

    public static String getDisplayBoardOverview(List<Player> playerlist, ParadeBoard paradeBoard) {
        String result = "";
        for (Player player : playerlist) {
            PlayerBoard board = player.getPlayerBoard();
            String boardString = "";
            boardString += DisplayEffects.BOLD + DisplayEffects.ANSI_GREEN + player.getPlayerName() + DisplayEffects.ANSI_RESET;
            if (board.isEmpty()) {
                boardString += "'s Player Board is empty.";
            } else {
                boardString += "'s Player Board:";
                boardString += board;
            }
            result += boardString + "\n";
        }
        result += "Parade:\n";
        result += paradeBoard + "\n";
        return result; 
    }

    public static void getBotDiscardDisplay(Player bot, Card discardedCard) {
        System.out.println(DisplayEffects.BOLD + DisplayEffects.ANSI_PURPLE
                + bot.getPlayerName() + " discarded:"
                + DisplayEffects.ANSI_RESET);
        System.out.println(discardedCard);
        System.out.println("Updated hand:");
        System.out.println(getFormattedHandWithIndex(bot.getPlayerHand(), null));
    }

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
            System.out.println();
        }
    
        System.out.println("Updated Hand:");
        System.out.println(player.getPlayerHand());
    
        UIManager.pauseExecution(5000);
        UIManager.clearScreen();
    }
    
    
}
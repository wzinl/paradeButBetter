package parade.helpers.ui;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.jansi.Ansi;

import parade.models.ParadeBoard;
import parade.models.cards.Card;
import parade.models.player.Player;
import parade.models.player.PlayerBoard;
import parade.models.player.PlayerHand;

/**
 * DisplayFactory handles all low-level formatting and UI presentation logic.
 * It supports displaying player hands, game states, scoreboards, and animations
 * using ANSI styling and custom formatting.
 */
public class DisplayFactory {


    private static final String TITLE_CARD = """
         ██████╗ ██████╗ ██████╗  █████╗ ██████╗ ███████╗
         ██╔══██╗██╔══██╗██╔══██╗ ██╔══██╗██╔══██╗██╔════╝
         ██████╔╝███████║██████╔╝ ███████║██║  ██║█████╗ 
         ██╔═══╝ ██╔══██║██╔══██╝ ██╔══██║██║  ██║██╔══╝  
         ██║     ██║  ██║██║  ██║ ██║  ██║██████╔╝███████╗
         ╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝ ╚══════╝
        """;
        private static final String TOP_BORDER ="------------------------Welcome To-------------------------\n\n";
        private static final String BOTTOM_BORDER ="\n-----------------------------------------------------------\n";
        private static final String FULL_TITLE_CARD =  TOP_BORDER + TITLE_CARD +BOTTOM_BORDER;

    public static String getTitleCard() {
        return FULL_TITLE_CARD;
    }

    /** Clears the console screen. */
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
        try {
            // System.out.printf("\n\n");
            System.out.print(Ansi.ansi().bold().a(
                            TOP_BORDER
                            ).reset());
            UIManager.typeWriter(TITLE_CARD, 6);
            System.out.print(Ansi.ansi().bold().a(
                BOTTOM_BORDER
                ).reset());

        } catch (InterruptedException e) {
            System.out.println("An error has occurred trying to create introduction display!");
        }
    }

    /** Generates a card selection prompt based on hand size. */
    public static String getCardSelectionPrompt(int handSize) {
        return String.format(Ansi.ansi().bold().fg(Ansi.Color.GREEN).a(
            "Which card would you like to play? (%d to %d): "
            ).reset().toString(), 1, handSize);
    }

    /** Builds the final scores display with winner or tie result. */
    public static String showFinalScores(List<Player> players, ParadeBoard paradeBoard) {
        StringBuilder finalScores = new StringBuilder();

        finalScores.append(
                    Ansi.ansi().bold().fg(Ansi.Color.MAGENTA).a(
                        "=========== FINAL SCORES ==========="
                        ).reset())
                    .append("\n");

        for (Player player : players) {
            finalScores.append(
                        String.format("%s has scored: %d%n", player.getPlayerName(), player.getPlayerScore()));
        }
    
        return finalScores.toString();
    }

    /** Returns formatted bot action announcement. */
    public static String getBotAction(String botName, int cardIndex) {
        return String.format(Ansi.ansi().bold().fgBright(Ansi.Color.MAGENTA).a("%s is going to play Card %d").reset().toString() + "\n", botName, cardIndex);
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
                      .append(Card.ANSI_RESET + "  ");
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

        result.append("==========")
              .append(Ansi.ansi().bold().fg(Ansi.Color.GREEN)
              .a(currentPlayer.getPlayerName() + "'s turn!").reset())
              .append("==========\n\n");

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

            result.append(isSelected && !onCardRow ?
                          Ansi.ansi().bold().fg(Ansi.Color.RED).a(
                          String.format("[ %s ]", option)).reset() 
                          : String.format("  %s  ", option))
                  .append("  ");
        }

        result.append(Ansi.ansi().bold().a("\n\nUse arrow keys or WASD to navigate, and Enter to select.").reset());
        return result.toString();
    }

    /** Displays player board and hand with optional selected card. */
    private static String getPlayerDisplay(Player player, ParadeBoard paradeBoard) {
        return getPlayerDisplay(player, paradeBoard, null);
    }

    /** Displays full game state for one player (hand, board, parade). */
    private static String getPlayerDisplay(Player player, ParadeBoard paradeBoard, Card selectedCard) {
        StringBuilder result = new StringBuilder();
        result.append(Card.ANSI_RESET);
        result.append("Parade:")
              .append(paradeBoard.toString())
              .append("\n\n")
              .append(Ansi.ansi().bold().a(player.getPlayerName()));

        if (player.getPlayerBoard().isEmpty()) {
            result.append("'s board is empty.").append(Ansi.ansi().reset());
        } else {
            result.append("'s board").append(Ansi.ansi().reset()).append("\n")
                  .append(player.getPlayerBoard().toString());
        }

        result.append("\n\n")
              .append(Ansi.ansi().bold().a(player.getPlayerName() + "'s hand").reset())
              .append("\n\n")
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

        scoreboard.append(String.format("%-" + positionWidth + "s|%-" + nameWidth + "s|%" + scoreWidth + "s%n",
                " POSITION", " PLAYER", "SCORE "));
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

            scoreboard.append(String.format("%-" + positionWidth + "s|%-" + nameWidth + "s|%" + scoreWidth + "d%n",
                    " " + position, " " + player.getPlayerName(), player.getPlayerScore()));
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
            String boardString = "";
            boardString += Ansi.ansi().bold().fg(Ansi.Color.GREEN).a(player.getPlayerName()).reset();
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

    /** Prints the discard action of a bot. */
    public static void getBotDiscardDisplay(Player bot, Card discardedCard) {
        System.out.println(Ansi.ansi().bold().fgBright(Ansi.Color.MAGENTA).a(bot.getPlayerName() + " discarded:").reset());
        System.out.println(discardedCard);
        System.out.println("Updated hand:");
        System.out.println(getFormattedHandWithIndex(bot.getPlayerHand(), null));
    }

    /** Displays a detailed breakdown when a card is played. */
    public static void getCardPlayDisplay(Player player, Card chosenCard, ParadeBoard paradeBoard, PlayerBoard playerBoard, ArrayList<Card> removedCards) {


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

    public static String getNamePrompt(boolean isBot, int index) {
        if(isBot){
            return Ansi.ansi().bold().fg(Ansi.Color.MAGENTA).a("🤖 Enter name of Bot " + index + ": ").reset().toString();
        }
        return Ansi.ansi().bold().fg(Ansi.Color.GREEN).a("🤓 Enter name of Player " + index + ": ").reset().toString();
    }
    

    
}

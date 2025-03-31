package main.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import main.models.ParadeBoard;
import main.models.cards.Card;
import main.models.player.Player;
import main.models.player.PlayerBoard;
import main.models.player.PlayerHand;


public class ScreenUtils {

        private static final Map<String, String> TEXT_TO_BG = Map.ofEntries(
        Map.entry("31", "41"),  // red -> red bg
        Map.entry("32", "42"),  // green -> green bg
        Map.entry("33", "43"),  // yellow -> yellow bg
        Map.entry("34", "44"),  // blue -> blue bg
        Map.entry("35", "45"),  // magenta -> magenta bg
        Map.entry("36", "46"),  // cyan -> cyan bg
        Map.entry("37", "47"),  // white -> white bg
        Map.entry("90", "100"), // bright black -> gray bg
        Map.entry("91", "101"), // bright red -> light red bg
        Map.entry("92", "102"), // bright green -> light green bg
        Map.entry("93", "103"), // bright yellow -> light yellow bg
        Map.entry("94", "104"), // bright blue -> light blue bg
        Map.entry("95", "105"), // bright magenta -> light magenta bg
        Map.entry("96", "106"), // bright cyan -> light cyan bg
        Map.entry("97", "107")  // bright white -> light white bg
    );

    public static void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void clearScreen() {
        System.out.println("\033c");
    }


    public static String getDisplay(Player currentPlayer, ParadeBoard paradeBoard) {
        StringBuilder result = new StringBuilder();
        PlayerHand hand = currentPlayer.getPlayerHand();

        result.append("\u001B[0m"); // triggers ANSI processing
        result.append("Parade Board:\n").append(paradeBoard.toString()).append("\n\n");
        result.append("Here is your board:\n");
        result.append(getPlayerBoardDisplay(currentPlayer.getPlayerBoard()));
        result.append("Here is your hand:\n\n");
        result.append(getHandDisplay(hand));
        result.append(getIndexString(hand));
        return result.toString();
    }

    public static String getDisplay(List<Player> playerList, ParadeBoard paradeBoard) {
        StringBuilder result = new StringBuilder();
        result.append("Parade Board:\n").append(paradeBoard).append("\n\n\n");

        for (Player curr : playerList) {
            result.append(curr.getPlayerName()).append("'s board\n");
            result.append(getPlayerBoardDisplay(curr.getPlayerBoard()));
            if (!curr.getPlayerHand().getCardList().isEmpty()) {
                result.append(curr.getPlayerName()).append("'s hand\n");
                result.append(getHandDisplay(curr.getPlayerHand()));
            }
        }

        result.append("\n");
        return result.toString();
    }

    public static String getHandDisplay(PlayerHand playerHand) {
        List<Card> cards = playerHand.getCardList();
        List<String[]> cardLines = new ArrayList<>();
        List<String> colorCodes = new ArrayList<>();

        for (Card card : cards) {
            colorCodes.add(card.getAnsiColorCode());
            cardLines.add(card.toString().replaceAll("\u001B\\[[;\\d]*m", "").split("\n"));
        }

        StringBuilder result = new StringBuilder();
        int linesPerCard = cardLines.get(0).length;

        for (int line = 0; line < linesPerCard; line++) {
            for (int i = 0; i < cards.size(); i++) {
                result.append(colorCodes.get(i))
                      .append(cardLines.get(i)[line])
                      .append("\u001B[0m ")
                      .append(" ");
            }
            result.append("\n");
        }

        return result.toString();
    }

    public static String getPlayerBoardDisplay(PlayerBoard board) {
        if (board.isEmpty()) {
            return "Playerboard is empty.\n\n";
        } else {
            return board + "\n\n";
        }
    }

    public static String getIndexString(PlayerHand playerHand) {
        ArrayList<Card> cardsList = playerHand.getCardList();
        StringBuilder index = new StringBuilder();

        for (int i = 0; i < cardsList.size(); i++) {
            int cardLength = 10;
            String cardIndexStr = "{" + (i + 1) + "}";
            int indexLength = cardIndexStr.length();
            int leftPadding = (cardLength - indexLength) / 2;
            int rightPadding = cardLength - indexLength - leftPadding;

            index.append(" ".repeat(leftPadding));
            index.append(cardIndexStr);
            index.append(" ".repeat(rightPadding));
            index.append("  ");
        }

        return index.toString();
    }
    

    public static String getDisplay(Player currentPlayer, ParadeBoard paradeBoard, Card selectedCard) {
        StringBuilder result = new StringBuilder();
        PlayerHand hand = currentPlayer.getPlayerHand();

        result.append("\u001B[0m"); // triggers ANSI processing
        result.append("Parade Board:\n").append(paradeBoard.toString()).append("\n\n");
        result.append("Here is your board:\n");
        result.append(getPlayerBoardDisplay(currentPlayer.getPlayerBoard()));
        result.append("Here is your hand:\n\n");
        result.append(getHandDisplay(hand, selectedCard));
        result.append(getIndexString(hand));
        return result.toString();
    }

    public static String getHandDisplay(PlayerHand playerHand, Card selectedCard) {
        List<Card> cards = playerHand.getCardList();
        List<String[]> cardLines = new ArrayList<>();
        List<String> colorCodes = new ArrayList<>();
    
        // Extract color codes and cleaned card strings
        for (Card card : cards) {
            if(card.equals(selectedCard)){
                colorCodes.add(card.getHighlightedAnsiColorCode());
                
            } else{
                colorCodes.add(card.getAnsiColorCode());
            }
            cardLines.add(card.toString().replaceAll("\u001B\\[[;\\d]*m", "").split("\n"));
        }
    
        StringBuilder result = new StringBuilder();
        int linesPerCard = cardLines.get(0).length;
    
        for (int line = 0; line < linesPerCard; line++) {
            for (int i = 0; i < cards.size(); i++) {
                boolean isSelected = cards.get(i).equals(selectedCard);
                String color = colorCodes.get(i);
                String cardLine = cardLines.get(i)[line];
    
                // if (isSelected) {
                //     // Apply a highlight like bold + underline + background
                //     result.append("\u001B[1;47m") // bold + underline + white background`
                //           .append(cardLine)
                //           .append("\u001B[0m");
                // } else {
                    result.append(color)
                          .append(cardLine)
                          .append("\u001B[0m");
                // }
    
                result.append("  "); // spacing between cards
            }
            result.append("\n");
        }
    
        return result.toString();
    }
    

    public static String getHighlightAnsi(String textColorCode) {
        String bgColorCode;
    
        bgColorCode = switch (textColorCode) {
            case "30" -> "40";
            case "31" -> "41";
            case "32" -> "42";
            case "33" -> "43";
            case "34" -> "44";
            case "35" -> "45";
            case "36" -> "46";
            case "37" -> "47";
            case "90" -> "100";
            case "91" -> "101";
            case "92" -> "102";
            case "93" -> "103";
            case "94" -> "104";
            case "95" -> "105";
            case "96" -> "106";
            case "97" -> "107";
            default -> "100";
        }; // black -> dark bg
        // red -> dark red
        // green -> dark green
        // yellow -> dark yellow
        // blue -> dark blue
        // magenta -> dark magenta
        // cyan -> dark cyan
        // white -> light gray
        // bright black -> gray
        // bright red -> light red
        // bright green
        // bright yellow
        // bright blue
        // bright magenta
        // bright cyan
        // bright white
        // fallback: gray
    
        // Return ANSI escape sequence: bold + original text + new bg
        return "\u001B[1;" + textColorCode + ";" + bgColorCode + "m";
    }
     
    
}

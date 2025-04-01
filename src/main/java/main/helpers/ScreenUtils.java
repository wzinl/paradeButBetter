package main.helpers;

import java.util.ArrayList;
import java.util.List;

import main.models.ParadeBoard;
import main.models.cards.Card;
import main.models.player.Player;
import main.models.player.PlayerBoard;
import main.models.player.PlayerHand;



public class ScreenUtils {


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

    public static String getDisplay(Player currentPlayer, ParadeBoard paradeBoard) {
        return getDisplay(currentPlayer, paradeBoard, null);
    }

    public static String getDisplay(Player currentPlayer, ParadeBoard paradeBoard, Card selectedCard) {
        StringBuilder result = new StringBuilder();
        PlayerHand hand = currentPlayer.getPlayerHand();
        String playerName = currentPlayer.getPlayerName();

        result.append("\u001B[0m"); // triggers ANSI processing
        result.append("Parade:");
        result.append(paradeBoard.toString()).append("\n\n");
        result.append(playerName).append("'s board\n");
        result.append(getPlayerBoardDisplay(currentPlayer.getPlayerBoard()));
        result.append(playerName).append("'s hand\n\n");
        result.append(getHandDisplay(hand, selectedCard));
        result.append(getIndexString(hand));
        return result.toString();
    }



    public static String getHandDisplay(PlayerHand playerHand) {
        return getHandDisplay(playerHand, null);
    }
    public static String getHandDisplay(PlayerHand playerHand, Card selectedCard) {
        List<Card> cards = playerHand.getCardList();
        List<String[]> cardLines = new ArrayList<>();
        List<String> colorCodes = new ArrayList<>();
    
        // Extract color codes and cleaned card strings
        for (Card card : cards) {
            if(selectedCard != null && card.equals(selectedCard)){
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
                String color = colorCodes.get(i);
                String cardLine = cardLines.get(i)[line];
                result.append(color)
                        .append(cardLine)
                        .append("\u001B[0m");
    
                result.append("  "); // spacing between cards
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

     
    public static String getTurnDisplay(Player currentPlayer, ParadeBoard paradeBoard, int selectedIndex, String[] actionOptions, Boolean onCardRow) {
        System.out.println(currentPlayer.getPlayerName() + "'s turn.");
        PlayerHand currHand = currentPlayer.getPlayerHand();
        List<Card> cardList = currHand.getCardList();
        int actionOptionsCount = actionOptions.length;

        String result = "";
        
        if(onCardRow){
            Card selectedCard = cardList.get(selectedIndex);
            result += ScreenUtils.getDisplay(currentPlayer, paradeBoard, selectedCard) + "\n";
        }else{
            result += ScreenUtils.getDisplay(currentPlayer, paradeBoard) + "\n";
        }
        System.out.println();
        for (int i = 0; i < actionOptionsCount; i++) {
            String option = actionOptions[i];
            boolean isSelected = (i == selectedIndex);

            String content;
            if (isSelected && !onCardRow) {
                content = "[" + option + "]";
            } else {
                content = " " + option + " ";
            }

            result += String.format("%-10s", content);
        }

        result += "\n\nUse A/D to move, W/S to switch rows, Enter to select.";

        return result;
    }
}

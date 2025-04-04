package main.models.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.exceptions.InvalidCardException;
import main.models.cards.Card;
import main.models.cards.CardCollection;

public class PlayerBoard implements CardCollection, Serializable{
    HashMap<String, ArrayList<Card>> playerBoard;

    public PlayerBoard() {
        this.playerBoard = new HashMap<>();
    }

    // From parade board add to playerboard
    @Override
    public void addCards(ArrayList<Card> cardsToAdd){
        for (int i = 0; i < cardsToAdd.size(); i++) {

            Card curCard = cardsToAdd.get(i);
            String curCardColor = curCard.getColor();

            // if player doesn't currently have this color, create new (key, value) map of
            // said color
            playerBoard.putIfAbsent(curCardColor, new ArrayList<>());

            // puts the new card into the arraylist with specified color
            playerBoard.get(curCardColor).add(curCard);
        }
    }

    @Override
    public void addCard(Card cardToAdd){
        String cardToAddColor = cardToAdd.getColor();

        // if player doesn't currently have this color, create new (key, value) map of
        // said color
        playerBoard.putIfAbsent(cardToAddColor, new ArrayList<>());

        // puts the new card into the arraylist with specified color
        playerBoard.get(cardToAddColor).add(cardToAdd);
    }

    // get how many cards of a certain color a player has. if a player has 3 black
    // cards, return 3 when black is put in
    public int getCardNumberByColor(String color) {
        ArrayList<Card> value = playerBoard.get(color);
        if (value == null) {
            return 0;
        } else {
            return value.size();
        }
        
    }
        
    public HashMap<String, ArrayList<Card>> getPlayerBoardHash() {
        return playerBoard;
    }
    // public void sortByQuantity() {
    // }

    public boolean isEmpty(){
        return playerBoard.isEmpty();
    }


    @Override
    public String toString() {
        List<String> colors = new ArrayList<>(playerBoard.keySet());
        colors.sort(Comparator.naturalOrder());
    
        // Card dimensions
        final int CARD_WIDTH = 10;
        final int CARD_HEIGHT = 8;  // Your card height (8 lines)
        final int OVERLAP = 2;       // Lines to overlap between cards
    
        Map<String, List<String[]>> colorStacks = new HashMap<>();
        int maxStackHeight = 0;
    
        for (String color : colors) {
            List<Card> cards = new ArrayList<>(playerBoard.get(color));
            cards.sort(Comparator.comparingInt(Card::getValue));
    
            List<String[]> stack = new ArrayList<>();
            for (Card card : cards) {
                stack.add(card.toStringArray());
            }
            colorStacks.put(color, stack);
            
            // Calculate stack height: (n-1 cards × overlap) + full card height
            maxStackHeight = Math.max(maxStackHeight, 
                (stack.size() - 1) * OVERLAP + CARD_HEIGHT);
        }
    
        StringBuilder output = new StringBuilder("\n");
    
        // Print centered colored headers
        for (String color : colors) {
            String colorCode = getAnsiColorCode(color);
            int padding = (CARD_WIDTH - color.length()) / 2;
            String centeredHeader = " ".repeat(padding) + colorCode + color + "\u001B[0m" + 
                                " ".repeat(CARD_WIDTH - color.length() - padding);
            output.append(centeredHeader).append("  "); // 2 spaces between columns
        }
        output.append("\n");

    
        // Print stacks
        for (int line = 0; line < maxStackHeight; line++) {
            for (String color : colors) {
                List<String[]> stack = colorStacks.get(color);
                String colorCode = getAnsiColorCode(color);
                boolean linePrinted = false;
    
                for (int cardIdx = 0; cardIdx < stack.size(); cardIdx++) {
                    int cardLine = line - (cardIdx * OVERLAP);
                    if (cardLine >= 0 && cardLine < CARD_HEIGHT) {
                        String cardRow = stack.get(cardIdx)[cardLine];
                        
                        // Apply color to the entire card
                        output.append(colorCode).append(cardRow).append("\u001B[0m");
                        linePrinted = true;
                        break; // Only show top most visible card for this line
                    }
                }
    
                if (!linePrinted) {
                    output.append(" ".repeat(CARD_WIDTH));
                }
                output.append("  "); // Spacing between color sets
            }
            output.append("\n");
        }
    
        return output.toString();
    }

    public String getAnsiColorCode(String color) {
        return switch (color) {
            case "Green" -> "\u001B[38;5;46m";
            case "Purple" -> "\u001B[38;5;129m";
            case "Red" -> "\u001B[38;5;196m";
            case "Blue" -> "\u001B[38;5;39m";
            case "Orange" -> "\u001B[38;5;208m";
            case "Grey" -> "\u001B[38;5;245m";
            default -> "\u001B[0m";
        };
    }

    @Override
    public void removeCard(Card card) throws InvalidCardException {
        throw new UnsupportedOperationException("Unimplemented method 'removeCard'");
    }
}
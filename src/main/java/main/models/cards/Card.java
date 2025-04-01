package main.models.cards;

import java.io.Serializable;

public class Card implements Serializable{
    private final int value;
    private final String color;
    private boolean isFaceUp;
    
    public Card(int value, String color, boolean isFaceUp) {
        this.value = value;
        this.color = color;
        this.isFaceUp = isFaceUp;
    }

    public String getColor() {
        return color;
    }

    public int getValue() {
        return value;
    }

    public boolean getIsFaceUp() {
        return isFaceUp;
    }

    public void setIsFaceUp(boolean isFaceUp) {
        this.isFaceUp = isFaceUp;
    }

    public String getAnsiColorCode() {
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
    public String getHighlightedAnsiColorCode() {
        return switch (color) {
            case "Green" -> "\u001B[1m\u001B[30m\u001B[48;5;46m";  // Bold + Black text + Green background
            case "Purple" -> "\u001B[1m\u001B[30m\u001B[48;5;129m";  // Bold + Black text + Purple background
            case "Red" -> "\u001B[1m\u001B[30m\u001B[48;5;196m";  // Bold + Black text + Red background
            case "Blue" -> "\u001B[1m\u001B[30m\u001B[48;5;39m";  // Bold + Black text + Blue background
            case "Orange" -> "\u001B[1m\u001B[30m\u001B[48;5;208m";  // Bold + Black text + Orange background
            case "Grey" -> "\u001B[1m\u001B[30m\u001B[48;5;245m";  // Bold + Black text + Grey background
            default -> "\u001B[1m\u001B[0m";  // Bold (default reset to normal)
        };
    }

    @Override
    public String toString() {
        String reset = "\u001B[0m";
        int CARD_WIDTH = 10;  // Fixed width 
        int TEXT_WIDTH = CARD_WIDTH - 4;  // Space between borders (after padding)
    
        StringBuilder result = new StringBuilder();
    
        // Top border
        result.append("\n┌").append("─".repeat(CARD_WIDTH - 2)).append("┐\n");
    
        // Top value (left-aligned)
        result.append("│ ")
              .append(String.format("%-" + TEXT_WIDTH + "s", value))  // Left-align
              .append(" │\n");
    
        // Empty line
        result.append("│").append(" ".repeat(CARD_WIDTH - 2)).append("│\n");
    
        // Color (centered)
        result.append("│ ")
              .append(centerString(color, TEXT_WIDTH))  // Helper method below
              .append(" │\n");
    
        // Empty line
        result.append("│").append(" ".repeat(CARD_WIDTH - 2)).append("│\n");
    
        // Bottom value (right-aligned)
        result.append("│ ")
              .append(String.format("%" + TEXT_WIDTH + "s", value))  // Right-align
              .append(" │\n");
    
        // Bottom border
        result.append("└").append("─".repeat(CARD_WIDTH - 2)).append("┘\n");
    
        return getAnsiColorCode() + result.toString() + reset;
    }
    
    // Helper method to center text (add this to your class)
    private String centerString(String text, int width) {
        if (text.length() >= width) {
            return text.substring(0, width);  // Truncate if too long
        }
        int padding = width - text.length();
        int leftPad = padding / 2;
        return " ".repeat(leftPad) + text + " ".repeat(padding - leftPad);
    }

    public int length() {
        return ("[" + color + ": " + value + "]").length();
    }
}

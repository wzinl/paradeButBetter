package parade.models.cards;

public class Card{
    private final int value;
    private final String color;
    private boolean isFaceUp;
    public static final String ANSI_RESET = "\u001B[0m";
    
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
        return CardColors.getAnsiColorCode(color);
    }

    public String getHighlightedAnsiColorCode() {
        return CardColors.getHighlightedAnsiColorCode(color);
    }

    @Override
public String toString() {
    final int CARD_WIDTH = 10;  // Fixed width 
    final int TEXT_WIDTH = CARD_WIDTH - 4;  // Space between borders (after padding)

    StringBuilder result = new StringBuilder();

    // Top border
    result.append("\n┌").append("─".repeat(CARD_WIDTH - 2)).append("┐\n");

    // Top value (left-aligned or blank if not face-up)
    if (isFaceUp) {
        result.append("│ ")
              .append(String.format("%-" + TEXT_WIDTH + "s", value))  // Left-align
              .append(" │\n");
    } else {
        result.append("│ ")
              .append(" ".repeat(TEXT_WIDTH))  // Blank space
              .append(" │\n");
    }

    // Empty line
    result.append("│").append(" ".repeat(CARD_WIDTH - 2)).append("│\n");

    // Color (centered)
    result.append("│ ")
          .append(centerString(color, TEXT_WIDTH))  // Helper method below
          .append(" │\n");

    // Empty line
    result.append("│").append(" ".repeat(CARD_WIDTH - 2)).append("│\n");

    // Bottom value (right-aligned or blank if not face-up)
    if (isFaceUp) {
        result.append("│ ")
              .append(String.format("%" + TEXT_WIDTH + "s", value))  // Right-align
              .append(" │\n");
    } else {
        result.append("│ ")
              .append(" ".repeat(TEXT_WIDTH))  // Blank space
              .append(" │\n");
    }

    // Bottom border
    result.append("└").append("─".repeat(CARD_WIDTH - 2)).append("┘");
    return getAnsiColorCode() + result.toString() + ANSI_RESET;
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

    public String[] toStringArray() {
        return toString().split("\n");
    }
    
    public int length() {
        return ("[" + color + ": " + value + "]").length();
    }
}

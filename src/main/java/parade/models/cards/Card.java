package parade.models.cards;

/**
 * Represents a single card in the game, characterized by its color, value, and visibility (face-up or face-down).
 * Provides methods to generate styled string representations for rendering in the terminal.
 */
public class Card {

    /** The value of the card (e.g., 0–10). */
    private final int value;

    /** The color of the card (e.g., "Red", "Blue"). */
    private final String color;

    /** Indicates whether the card is currently face up (visible). */
    private boolean isFaceUp;

    /** ANSI reset code to clear color formatting. */
    public static final String ANSI_RESET = "\u001B[0m";

    /**
     * Constructs a Card object with the specified value, color, and face-up status.
     *
     * @param value     the numeric value of the card
     * @param color     the color of the card
     * @param isFaceUp  whether the card is face up
     */
    public Card(int value, String color, boolean isFaceUp) {
        this.value = value;
        this.color = color;
        this.isFaceUp = isFaceUp;
    }

    /**
     * Returns the color of the card.
     *
     * @return the card's color
     */
    public String getColor() {
        return color;
    }

    /**
     * Returns the numeric value of the card.
     *
     * @return the card's value
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns whether the card is face up (visible).
     *
     * @return true if the card is face up, false otherwise
     */
    public boolean getIsFaceUp() {
        return isFaceUp;
    }

    /**
     * Sets whether the card is face up.
     *
     * @param isFaceUp true to show the card, false to hide it
     */
    public void setIsFaceUp(boolean isFaceUp) {
        this.isFaceUp = isFaceUp;
    }

    /**
     * Gets the ANSI color code for rendering this card's color.
     *
     * @return ANSI escape sequence representing the card's color
     */
    public String getAnsiColorCode() {
        return CardColors.getAnsiColorCode(color);
    }

    /**
     * Gets the highlighted ANSI color code for hover or selection effects.
     *
     * @return ANSI escape sequence for highlighted color
     */
    public String getHighlightedAnsiColorCode() {
        return CardColors.getHighlightedAnsiColorCode(color);
    }

    /**
     * Returns the default string representation of the card, using the full-sized display.
     *
     * @return the full string representation of the card
     */
    @Override
    public String toString() {
        return toString(false);
    }

    /**
     * Returns a formatted string representation of the card in either full or compact layout.
     *
     * @param compact true for a compact version, false for full size
     * @return the formatted string
     */
    public String toString(boolean compact) {
        String reset = ANSI_RESET;
        final int CARD_WIDTH = compact ? 8 : 10;
        final int TEXT_WIDTH = CARD_WIDTH - (compact ? 2 : 4);

        StringBuilder result = new StringBuilder();

        // Top border
        result.append("\n┌").append("─".repeat(CARD_WIDTH - 2)).append("┐\n");

        if (compact) {
            // Compact display
            if (isFaceUp) {
                result.append("│")
                    .append(centerString(Integer.toString(value), TEXT_WIDTH))
                    .append("│\n");
            } else {
                result.append("│")
                    .append(" ".repeat(TEXT_WIDTH))
                    .append("│\n");
            }

            result.append("│")
                .append(centerString(isFaceUp ? color : "", TEXT_WIDTH))
                .append("│\n");

        } else {
            // Top value (left-aligned)
            if (isFaceUp) {
                result.append("│ ")
                    .append(String.format("%-" + TEXT_WIDTH + "s", value))
                    .append(" │\n");
            } else {
                result.append("│ ")
                    .append(" ".repeat(TEXT_WIDTH))
                    .append(" │\n");
            }

            // Blank middle
            result.append("│").append(" ".repeat(CARD_WIDTH - 2)).append("│\n");

            // Centered color
            result.append("│ ")
                .append(centerString(color, TEXT_WIDTH))
                .append(" │\n");

            result.append("│").append(" ".repeat(CARD_WIDTH - 2)).append("│\n");

            // Bottom value (right-aligned)
            if (isFaceUp) {
                result.append("│ ")
                    .append(String.format("%" + TEXT_WIDTH + "s", value))
                    .append(" │\n");
            } else {
                result.append("│ ")
                    .append(" ".repeat(TEXT_WIDTH))
                    .append(" │\n");
            }
        }

        // Bottom border
        result.append("└").append("─".repeat(CARD_WIDTH - 2)).append("┘");

        return getAnsiColorCode() + result.toString() + reset;
    }

    /**
     * Converts the card's string representation into a string array (line-by-line).
     *
     * @return array of strings representing the card
     */
    public String[] toStringArray() {
        return toString().split("\n");
    }

    /**
     * Helper method to center a string within a given width.
     * Pads with spaces on both sides.
     *
     * @param text  the text to center
     * @param width the total width of the area
     * @return centered string with padding
     */
    private String centerString(String text, int width) {
        if (text.length() >= width) {
            return text.substring(0, width);
        }
        int padding = width - text.length();
        int leftPad = padding / 2;
        return " ".repeat(leftPad) + text + " ".repeat(padding - leftPad);
    }
}

package parade.models.cards;

public class CardColors {
    public static String getAnsiColorCode(String color) {
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
    public static String getHighlightedAnsiColorCode(String color) {
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
}

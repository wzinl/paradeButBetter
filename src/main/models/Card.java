package main.models;


public class Card {
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

    private String getAnsiColorCode() {
        switch (color) {
            case "Green": return "\u001B[38;5;46m";  
            case "Purple": return "\u001B[38;5;129m";  
            case "Red": return "\u001B[38;5;196m";  
            case "Blue": return "\u001B[38;5;39m";  
            case "Orange": return "\u001B[38;5;208m";  
            case "Grey": return "\u001B[38;5;245m";  
            default: return "\u001B[0m";        
        }
    }


    @Override
    public String toString() {

        String reset = "\u001B[0m"; // store the default terminal color code in a string to use 

        return getAnsiColorCode() + "[" + color + ": " + value + "]" + reset;
    }
}

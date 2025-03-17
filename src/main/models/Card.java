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


    @Override
    public String toString() {
        return "(" + this.color + ": " + this.value + ")";
    }
}

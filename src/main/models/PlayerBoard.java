package main.models;

import java.util.HashMap;
import java.util.*;

public class PlayerBoard {
    HashMap<String, ArrayList<Card>> playerBoard;

    public PlayerBoard() {
        this.playerBoard = new HashMap<>();

    }

    // From parade board add to playerboard
    public void addToBoard(ArrayList<Card> cardsToAdd) {
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

    // get how many cards of a certain color a player has. if a player has 3 black
    // cards, return 3 when black is put in
    public int getCardNumberByColor(String color) {
        return playerBoard.get(color).size();
    }

    // public void sortByQuantity() {
    // }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, ArrayList<Card>> entry : playerBoard.entrySet()) {
            String key = entry.getKey(); // colour of the cards
            ArrayList<Card> cards = entry.getValue(); // List of cards of that colour 
            sb.append("Color:").append(key).append(", Cards: ").append("\n");
            
        }

        return sb.toString();
    }

    
    public HashMap<String, ArrayList<Card>> getPlayerBoard() {
        return playerBoard;
    }
}
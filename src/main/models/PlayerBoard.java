package main.models;

import java.util.*;
import main.error.InvalidCardException;

public class PlayerBoard implements CardCollection{
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

    @Override
    public void removeCard(Card card) throws InvalidCardException{
        //TODO
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
        
        Map<String, List<Integer>> colorValues = new HashMap<>();
        int maxRows = 0;
        for (String color : colors) {
            List<Integer> values = playerBoard.get(color).stream()
                    .map(c -> c.getValue())
                    .sorted()
                    .toList();
            colorValues.put(color, values);
            maxRows = Math.max(maxRows, values.size());
        }

        List<Integer> colWidths = colors.stream().map(String::length).toList();
        StringBuilder output = new StringBuilder();

        // Header
        for (int i = 0; i < colors.size(); i++) {
            String colorName = colors.get(i);
            String code = getAnsiColorCode(colorName);
            String coloredHeader = code + colorName + "\u001B[0m"; //default col code for terminal to reset the colours used
        
            output.append(String.format("%-" + colWidths.get(i) + "s  ", coloredHeader));
        }
        output.append("\n");
    
        // Values
        for (int row = 0; row < maxRows; row++) {
            for (int i = 0; i < colors.size(); i++) {
                List<Integer> values = colorValues.get(colors.get(i));
                String num = row < values.size() ? values.get(row) + "" : "";
                output.append(String.format("%-" + colWidths.get(i) + "s  ", num));
            }
            output.append("\n");
        }
        return output.toString();
    }

    private String getAnsiColorCode(String color) {
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
}
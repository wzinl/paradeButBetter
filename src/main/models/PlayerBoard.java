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
        for (int i = 0; i < colors.size(); i++) 
                output.append(String.format("%-" + colWidths.get(i) + "s  ", colors.get(i)));
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

    
    public HashMap<String, ArrayList<Card>> getPlayerBoardHash() {
        return playerBoard;
    }
}
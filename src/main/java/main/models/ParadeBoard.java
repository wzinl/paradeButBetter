package main.models;

import java.io.Serializable;
import java.util.*;

import main.models.cards.Card;
import main.models.cards.Deck;

public class ParadeBoard implements Serializable{
    private ArrayList<Card> cardList;

    // makes the board on its own using the deck and drawing the top 5 cards
    public ParadeBoard(Deck deck) {
        this.cardList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            cardList.add(deck.drawCard());
        }
    }

    public int getNumberOfCards() {
        return cardList.size();
    }

    public void addToBoard(Card card) {
        cardList.add(card);
    }

    public void remove(Card card) {
        cardList.remove(card);
    }

    // to check
    public ArrayList<Card> playCard(Card card) {
        ArrayList<Card> selectedcards = new ArrayList<>();
        // check if cards value is greater than board size
        if (card.getValue() > cardList.size()) {
            return selectedcards;
        }
        // Cards beyond playedCard's value index enter "removal mode"
        int removalStartIndex = cardList.size() - card.getValue() - 1;

        // "removal mode" cards
        ArrayList<Card> remainingParade = new ArrayList<>();

        // if not, the removal pile is no.of cards in board - card val
        for (int i = cardList.size() - 1; i >= 0; i--) {
            Card currentCard = cardList.get(i);

            if (i >= removalStartIndex) {
                // Remove if same colour or value less than or equal to the value of played card
                if (currentCard.getColor().equals(card.getColor()) || currentCard.getValue() <= card.getValue()) {
                    selectedcards.add(currentCard);
                } else {
                    remainingParade.add(currentCard);
                }
            }
            // even if not in "removal mode", add to remaining parade
            else {
                remainingParade.add(currentCard);
            }
        }

        // update the parade after removal
        this.cardList = remainingParade;
        return selectedcards;
    }

    public ArrayList<Card> getCardList() {
        return cardList;
    }

    @Override
    public String toString() {

        // Prepare all card lines and color codes
        List<String[]> cardLines = new ArrayList<>();
        List<String> colorCodes = new ArrayList<>();
        
        for (Card card : cardList) {
            colorCodes.add(card.getAnsiColorCode());
            cardLines.add(card.toString()
                        .replaceAll("\u001B\\[[;\\d]*m", "") // Remove existing ANSI codes
                        .split("\n"));
        }

        // Build horizontal display
        StringBuilder result = new StringBuilder();
        int linesPerCard = cardLines.get(0).length;
        
        for (int line = 0; line < linesPerCard; line++) {
            for (int i = 0; i < cardList.size(); i++) {
                // Apply card's color, add line, then reset
                result.append(colorCodes.get(i))
                    .append(cardLines.get(i)[line])
                    .append("\u001B[0m  "); // Reset + double space between cards
            }
            result.append("\n");
        }

        return result.toString();
    }

    // used in player turn when you want to display the cards that will be removed
    // from the parade board
    public String toString(ArrayList<Card> toRemove, Card chosenCard) {
        int safeCardCount = Math.min(chosenCard.getValue(), cardList.size());
    
        // Prepare data for display
        List<String[]> firstLine = new ArrayList<>(); // Top row (cards to remove)
        List<String[]> secondLine = new ArrayList<>(); // Bottom row (kept + safe)
        List<String> colorCodes = new ArrayList<>();
    
        for (Card card : cardList) {
            colorCodes.add(card.getAnsiColorCode());
            String[] cardLines = card.toString()
                                     .replaceAll("\u001B\\[[;\\d]*m", "")
                                     .split("\n");
            if (toRemove.contains(card)) {
                firstLine.add(cardLines); // Card appears in first row
                secondLine.add(null); // Leave blank in second row
            } else {
                firstLine.add(null); // Leave blank in first row
                secondLine.add(cardLines); // Card appears in second row
            }
        }
    
        // Prepare chosen card display
        String[] chosenCardLines = chosenCard.toString()
                                             .replaceAll("\u001B\\[[;\\d]*m", "")
                                             .split("\n");
        String chosenColor = chosenCard.getAnsiColorCode();
    
        // Format first and second row
        StringBuilder result = new StringBuilder();
        int linesPerCard = 8;
    
        // First row (Cards to remove)
        for (int line = 0; line < linesPerCard; line++) {
            for (int i = 0; i < cardList.size() - safeCardCount; i++) {
                if (firstLine.get(i) != null) {
                    result.append(colorCodes.get(i))
                          .append(firstLine.get(i)[line])
                          .append("\u001B[0m  ");
                } else {
                    result.append(" ".repeat(secondLine.get(i)[line].length()))
                          .append("  ");
                }
            }
            result.append("\n");
        }
        result.append("\n"); // Space between rows
    
        // Second row (Kept cards + safe cards + chosen card)
        for (int line = 0; line < linesPerCard; line++) {
            // Kept cards
            for (int i = 0; i < cardList.size() - safeCardCount; i++) {
                if (secondLine.get(i) != null) {
                    result.append(colorCodes.get(i))
                          .append(secondLine.get(i)[line])
                          .append("\u001B[0m  ");
                } else {
                    result.append(" ".repeat(firstLine.get(i)[line].length()))
                          .append("  ");
                }
            }
    
            // Safe cards separator
            if (safeCardCount > 0) {
                if (line == linesPerCard / 2) {
                    result.append(":  ");
                } else {
                    result.append("   ");
                }
            }
    
            // Safe cards (leftovers)
            for (int i = cardList.size() - safeCardCount; i < cardList.size(); i++) {
                result.append(colorCodes.get(i))
                      .append(secondLine.get(i)[line])
                      .append("\u001B[0m  ");
            }
    
            // Chosen card separator and card
            if (line == linesPerCard / 2) {
                result.append("|  ");
            } else {
                result.append("   ");
            }
            result.append(chosenColor)
                  .append(chosenCardLines[line])
                  .append("\u001B[0m\n");
        }
    
        return result.toString();
    }
    
    
}
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
        for (int i = 0; i < 5; i++) {
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
        List<Card> firstLine = new ArrayList<>();
        List<Card> secondLine = new ArrayList<>();
        int i;
        int safeCardCount = chosenCard.getValue();
        if (chosenCard.getValue() >= cardList.size()) {
            return formatHorizontalLine(cardList) + "|  " + chosenCard;
        }
        
        for (i = 0; i < cardList.size() - safeCardCount; i++) {
            Card card = cardList.get(i);

            if (toRemove.contains(card)) {
                firstLine.add(card);
                secondLine.add(null);
            } else {
                firstLine.add(null);
                secondLine.add(card);
            }

        }
        if (i < cardList.size()){
            secondLine.add(null);
        }

        while (i < cardList.size()) {
            Card card = cardList.get(i);
            secondLine.add(card);
            i++;
        }
        
        // Format the horizontal display
        StringBuilder result = new StringBuilder();
        
        // First line (cards to remove)
        result.append(formatHorizontalLine(firstLine));
        result.append("\n\n"); // Space between lines
        
        // Second line (kept cards + safe cards + chosen card)
        result.append(formatHorizontalLine(secondLine));
        result.append("|  ").append(chosenCard);
    
        return result.toString();
    }
    
    // Helper method to format a list of cards horizontally
    private String formatHorizontalLine(List<Card> cards) {
        StringBuilder line = new StringBuilder();
        int cardWidth = 10; // Adjust based on the expected length of cards

        for (Card card : cards) {
            if (card == null) {
                line.append("     "); // Use consistent width for null cards
            } else {
                line.append(String.format("%-" + cardWidth + "s", card)); // Format each card to be aligned
            }
            line.append("  "); // Extra spacing between cards
        }

        return line.toString().trim();
    }
}
package main.models;

import java.util.*;

import main.models.cards.Card;
import main.models.cards.Deck;

public class ParadeBoard {
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

        StringBuilder result = new StringBuilder();

        for (Card card : cardList) {
            result.append(card).append("  ");
        }

        return result.toString();
    }

    // used in player turn when you want to display the cards that will be removed
    // from the parade board
    public String toString(ArrayList<Card> toRemove, Card chosenCard) {
        String firstLine = "";
        String secondLine = "";
        int i;
        int safeCardCount = chosenCard.getValue();
        if (chosenCard.getValue() >= cardList.size()) {
            return this.toString() + "|  " + chosenCard;
        }
        for (i = 0; i < cardList.size() - safeCardCount; i++) {
            Card card = cardList.get(i);
            String blank = " ".repeat(card.length());

            if (toRemove.contains(card)) {
                firstLine += card + "  ";
                secondLine += blank + "  ";
            } else {
                firstLine += blank + "  ";
                secondLine += card + "  ";
            }

        }
        if (i < cardList.size()){
            secondLine += ":  ";
        }

        while (i < cardList.size()) {
            Card card = cardList.get(i);
            secondLine += card + "  ";
            i++;
        }
        secondLine += "|  ";
        secondLine += chosenCard;

        firstLine = firstLine.replaceAll("\\s+$", "");
        secondLine = secondLine.replaceAll("\\s+$", "");

        // right trim both lines and combine
        return firstLine + "\n" + secondLine;
    }

}

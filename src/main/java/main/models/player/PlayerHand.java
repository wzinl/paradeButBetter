package main.models.player;

import java.util.ArrayList;
import java.util.List;

import main.exceptions.InvalidCardException;
import main.models.cards.Card;
import main.models.cards.Deck;

public class PlayerHand{
    private final ArrayList<Card> cardList;
    // to double check
    public static final int MAXHANDCOUNT = 5;

    public PlayerHand() {
        this.cardList = new ArrayList<>(); // in GameManager class need to draw card from the deck and add cards to
                                           // empty hand
    }

    // this method not needed right...
    public void addCard(Card card) {
        if (cardList.size() < MAXHANDCOUNT) {
            cardList.add(card);
        } else {
            throw new IllegalStateException("Cannot add more cards. Hand is full.");
        }
    }

    // Implements removeCard from CardCollection interface

    public void removeCard(Card card) throws InvalidCardException {
        // If hand does not have that card
        if (!cardList.contains(card)) {
            throw new InvalidCardException("Card not in hand!");

        }
        // else remove card from hand
        cardList.remove(card);

    }
    // from the board

    // can do custom error to make it final round if deck is empty?
    public void drawCard(Deck deck) {
        // first, check if drawing a card is possible
        if (deck.isEmpty()) {
            System.out.println("Cannot draw, deck empty");

        } else {
            // if not, draw from the top of the deck
            Card drawnCard = deck.getDeck().remove(deck.getDeckSize() - 1);

            // Add the drawn card to the player's hand
            addCard(drawnCard);
        }
    }

    public void initHand(Deck deck) {
        for (int i = 0; i < 5; i++) {
            drawCard(deck);
        }
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

    public ArrayList<Card> getCardList() {
        return cardList;
    }

    public int getHandSize() { 
        return this.getCardList().size();
    }

}
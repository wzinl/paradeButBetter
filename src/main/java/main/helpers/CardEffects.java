package main.helpers;

import java.util.ArrayList;

import main.helpers.ui.UIManager;
import main.models.ParadeBoard;
import main.models.cards.Card;
import main.models.player.PlayerBoard;

public class CardEffects {

    public static void apply(String playerName,Card chosenCard, ParadeBoard paradeBoard, PlayerBoard playerBoard) {
        int chosenValue = chosenCard.getValue();
        ArrayList<Card> removedCards = new ArrayList<>();

        for (int i = 0; i < paradeBoard.getCardList().size() - chosenValue; i++) {
            Card current = paradeBoard.getCardList().get(i);
            if (current.getValue() <= chosenValue || current.getColor().equals(chosenCard.getColor())) {
                removedCards.add(current);
            }
        }
        UIManager.clearScreen();

        System.out.println(playerName + " has played: ");
        System.out.println(chosenCard);
        // UIManager.pauseExecution(1000);

        System.out.println("Updated Parade:");
        System.out.println(paradeBoard.toString(removedCards, chosenCard));
        System.out.println();

        if(playerBoard.isEmpty()){
            System.out.println(playerName +"'s' Playerboard is empty.");
        } else{
            System.out.println("Updated playerboard:");
            System.out.println(playerBoard.toString());
            System.out.println();
        }   

        // UIManager.pauseExecution(5000);
        UIManager.clearScreen();

        for (Card card : removedCards) {
            paradeBoard.remove(card);
            playerBoard.addCard(card);
        }

        paradeBoard.addToBoard(chosenCard);
    }

    public static int simulate(Card chosenCard, ParadeBoard paradeBoard) {
        int count = 0;
        int chosenValue = chosenCard.getValue();

        for (int i = 0; i < paradeBoard.getCardList().size() - chosenValue; i++) {
            Card current = paradeBoard.getCardList().get(i);
            if (current.getValue() <= chosenValue || current.getColor().equals(chosenCard.getColor())) {
                count++;
            }
        }
        return count;
    }

    public static int[] smarterSimulate(Card chosenCard, ParadeBoard paradeBoard) {
        int count = 0;
        int score = 0;
        int chosenValue = chosenCard.getValue();

        for (int i = 0; i < paradeBoard.getCardList().size() - chosenValue; i++) {
            Card current = paradeBoard.getCardList().get(i);
            if (current.getValue() <= chosenValue || current.getColor().equals(chosenCard.getColor())) {
                count++;
                score += current.getValue();
            }
        }

        int[] ret = {count, score};
        return ret;
    }
}

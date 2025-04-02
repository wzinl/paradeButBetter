package main.gameStates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.context.GameContext;
import main.exceptions.InvalidCardException;
import main.helpers.CardEffects;
import main.helpers.InputValidator;
import main.helpers.MenuSelector;
import main.helpers.ScreenUtils;
import main.models.cards.Card;
import main.models.player.Player;
import main.models.player.PlayerBoard;
import main.models.player.PlayerHand;
import main.models.player.bots.RandomBot;
import main.models.player.bots.SmartBot;

public class TurnState extends GameState {

    private boolean isInFinalRound;
    private static final String[] ACTION_OPTIONS = {
        "Save Game",
        "Quit Game"
    };

    public TurnState(GameStateManager gsm, GameContext context) {
        super(gsm, context);
        this.isInFinalRound = context.getIsInFinalRound();
    }

    @Override
    public void enter() {
        ScreenUtils.clearScreen();

        while (!isInFinalRound) {
            Player currentPlayer = playerList.get(currentPlayerIndex);
            playTurn(currentPlayer, false);

            if (currentPlayer.hasCollectedAllColours() || deck.isEmpty()) {
                context.setInFinalRound(true);
                isInFinalRound = true;
                System.out.println("You have collected all 6 colors! Moving on to the final round!");
                context.setFinalRoundStarterIndex(currentPlayerIndex);
                this.finalPlayerIndex = currentPlayerIndex;
            }

            this.currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
        }
        finalRound();
    }

    public void playTurn(Player current, Boolean isFinalTurn) {
        PlayerHand hand = current.getPlayerHand();
        PlayerBoard board = current.getPlayerBoard();
        List <Card> cards = hand.getCardList();


        // System.out.println(ScreenUtils.getDisplay(current, paradeBoard, hand.getCardList().get(1)) + "\n");
        while (true) {
            try {
                int index = getTurnSelection(current);

                Card chosenCard = cards.get(index);

                if (current instanceof RandomBot || current instanceof SmartBot) {
                    System.out.printf("Bot is going to play card #%d...\n", index);
                    ScreenUtils.pause(3000);
                }

                CardEffects.apply(chosenCard, paradeBoard, board);
                hand.removeCard(chosenCard);
                if (!isFinalTurn) {
                    hand.drawCard(deck);
                }
                ScreenUtils.clearScreen();

            } catch (InvalidCardException e) {
                System.out.println("Invalid card. Please enter a valid card.");
            }
        }
    }

    private int getTurnSelection(Player current) {
        ArrayList <Card> cardList = current.getPlayerHand().getCardList();
        if (current instanceof RandomBot randomBot) {
            return randomBot.getNextCardIndex(cardList);
        } else if (current instanceof SmartBot smartBot) {
            return smartBot.getNextCardIndex(cardList, paradeBoard);
        } else {
            try {
                String chosen = MenuSelector.turnSelect(paradeBoard, current, ACTION_OPTIONS);
                if (chosen.matches("\\d+")) {
                    return Integer.parseInt(chosen);
                }
                if (chosen.startsWith("action: ")) {
                    return chosen.split("action: ")[1].charAt(0);
                }
            } catch (IOException e) {
                System.out.println("An error has occured displaying the menu! Defaulting to manual input.");
            }
            System.out.println(ScreenUtils.getDisplay(current, paradeBoard));
            return InputValidator.getIntInRange(
                String.format("Which card would you like to play? (%d to %d): ", 1, cardList.size()),
                1, cardList.size());
        }
    }

    public void finalRound() {
        System.out.println("Each player gets one final turn! No more cards will be drawn!");

        while (currentPlayerIndex != finalPlayerIndex) {
            System.out.println("finalPlayerIndex: " + finalPlayerIndex);
            System.out.println("currentPlayerIndex: " + currentPlayerIndex);

            Player currentPlayer = playerList.get(currentPlayerIndex);
            playTurn(currentPlayer, true);
            this.currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
        }

        playTurn(playerList.get(finalPlayerIndex), true);
        this.currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
    }

    @Override
    public void exit() {
        System.out.println("Exiting TurnState.");
    }
}
package main.gameStates;

import java.util.List;

import main.context.GameContext;
import main.exceptions.InvalidCardException;
import main.helpers.InputValidator;
import main.helpers.ScreenUtils;
import main.helpers.CardEffects;
import main.models.cards.Card;
import main.models.player.*;
import main.models.player.bots.RandomBot;
import main.models.player.bots.SmartBot;

public class TurnState extends GameState {

    private boolean isInFinalRound;

    public TurnState(GameStateManager gsm, GameContext context) {
        super(gsm, context);
        this.isInFinalRound = context.getIsInFinalRound();
    }

    @Override
    public void enter() {
        System.out.println("Entering TurnState");
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
        System.out.println(current.getPlayerName() + "'s turn.");
        PlayerHand hand = current.getPlayerHand();
        PlayerBoard board = current.getPlayerBoard();
        List<Card> cards = hand.getCardList();

        System.out.println(getDisplay(current) + "\n");

        while (true) {
            try {
                int index = getCardIndex(current, cards, board);
                Card chosenCard = cards.get(index);

                if (current instanceof RandomBot || current instanceof SmartBot) {
                    System.out.printf("Bot is going to play card #%d...\n", index + 1);
                    ScreenUtils.pause(3000);
                }

                CardEffects.apply(chosenCard, paradeBoard, board);
                hand.removeCard(chosenCard);
                hand.drawCard(deck);
                ScreenUtils.clearScreen();
                break;

            } catch (InvalidCardException e) {
                System.out.println("Invalid card. Please enter a valid card.");
            }
        }
    }

    private int getCardIndex(Player current, List<Card> hand, PlayerBoard board) {
        if (current instanceof RandomBot) {
            return ((RandomBot) current).getNextCardIndex(hand);
        } else if (current instanceof SmartBot) {
            return ((SmartBot) current).getNextCardIndex(hand, paradeBoard);
        } else {
            return InputValidator.getIntInRange(
                    String.format("Which card would you like to play? (%d to %d): ", 1, hand.size()),
                    1, hand.size()
            );
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

package main.gameStates;

import java.util.ArrayList;
import java.util.Random;

import main.context.GameContext;
import main.exceptions.InvalidCardException;
import main.helpers.InputValidator;
import main.models.bots.RandomBot;
import main.models.bots.SmartBot;
import main.models.cards.Card;
import main.models.player.Player;
import main.models.player.PlayerBoard;
import main.models.player.PlayerHand;

public class TurnState extends GameState {

    private boolean isInFinalRound;

    public TurnState(GameStateManager gsm, GameContext context) {
        super(gsm, context);
        this.isInFinalRound = context.getIsInFinalRound();
    }

    @Override
    public void enter() {
        System.out.println("Entering TurnState");
        System.out.println("\033c");

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
        PlayerHand currentHand = current.getPlayerHand();
        PlayerBoard currentplayerBoard = current.getPlayerBoard();

        System.out.println(getDisplay(current) + "\n");

        if (current instanceof RandomBot) {
            while (true) {
                try {
                    Random random = new Random();
                    int playIndex = random.nextInt(currentHand.getCardList().size());
                    System.out.printf("The bot is going to play: %d\n", playIndex + 1);
                    Card chosenCard = currentHand.getCardList().get(playIndex);

                    Thread.sleep(5000);

                    System.out.println("Bot played: ");
                    System.out.println(chosenCard);
                    System.out.println();

                    playCard(chosenCard, currentplayerBoard);
                    currentHand.removeCard(chosenCard);
                    currentHand.drawCard(deck);
                    System.out.println("\033c");

                    break;
                } catch (InvalidCardException e) {
                    System.out.println("Invalid card. Please enter a valid card.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } else if (current instanceof SmartBot) {
            while (true) {
                try {
                    int bestIndex = 0;
                    int leastNumCardsKicked = Integer.MAX_VALUE;

                    for (int i = 0; i < currentHand.getCardList().size(); i++) {
                        Card chosenCard = currentHand.getCardList().get(i);
                        int numCardsKicked = simulatePlayCard(chosenCard, currentplayerBoard);
                        if (numCardsKicked < leastNumCardsKicked) {
                            leastNumCardsKicked = numCardsKicked;
                            bestIndex = i;
                        }
                    }

                    System.out.printf("The bot is going to play but smartly: %d\n", bestIndex + 1);
                    System.out.println();

                    Thread.sleep(5000);

                    Card chosenCard = currentHand.getCardList().get(bestIndex);
                    System.out.println("Bot played: ");
                    System.out.println(chosenCard);
                    System.out.println();

                    playCard(chosenCard, currentplayerBoard);
                    currentHand.removeCard(chosenCard);
                    currentHand.drawCard(deck);
                    System.out.println("\033c");

                    break;
                } catch (InvalidCardException e) {
                    System.out.println("Invalid card. Please enter a valid card.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } else {
            while (true) {
                try {
                    int playIndex = InputValidator.getIntInRange(
                            String.format("Which card would you like to play?(%d to %d): ",
                                    1, currentHand.getCardList().size()),
                            1, currentHand.getCardList().size()) - 1;

                    Card chosenCard = currentHand.getCardList().get(playIndex);
                    System.out.println();

                    System.out.println("You have played: ");
                    System.out.println(chosenCard);
                    System.out.println();

                    playCard(chosenCard, currentplayerBoard);
                    currentHand.removeCard(chosenCard);
                    currentHand.drawCard(deck);
                    System.out.println("\033c");

                    break;
                } catch (InvalidCardException e) {
                    System.out.println("Invalid card. Please enter a valid card.");
                }
            }
        }
    }

    public void playCard(Card chosenCard, PlayerBoard currentPlayerBoard) {
        int chosenValue = chosenCard.getValue();
        ArrayList<Card> removedCards = new ArrayList<>();

        for (int i = 0; i < paradeBoard.getCardList().size() - chosenValue; i++) {
            Card currentParadeCard = paradeBoard.getCardList().get(i);
            if (currentParadeCard.getValue() <= chosenValue
                    || currentParadeCard.getColor().equals(chosenCard.getColor())) {
                removedCards.add(currentParadeCard);
            }
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Turn Summary:");
        System.out.println(paradeBoard.toString(removedCards, chosenCard));
        System.out.println();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\033c");

        for (Card currentParadeCard : removedCards) {
            paradeBoard.remove(currentParadeCard);
            currentPlayerBoard.addCard(currentParadeCard);
        }

        paradeBoard.addToBoard(chosenCard);
        System.out.println();
    }

    public int simulatePlayCard(Card chosenCard, PlayerBoard currentPlayerBoard) {
        int counter = 0;
        int chosenValue = chosenCard.getValue();

        for (int i = 0; i < paradeBoard.getCardList().size() - chosenValue; i++) {
            Card currentParadeCard = paradeBoard.getCardList().get(i);
            if (currentParadeCard.getValue() <= chosenValue
                    || currentParadeCard.getColor().equals(chosenCard.getColor())) {
                counter++;
            }
        }

        return counter;
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

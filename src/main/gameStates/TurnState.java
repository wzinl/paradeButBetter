package main.gameStates;

import java.util.ArrayList;
import java.util.Random;

import main.context.GameContext;
import main.error.InvalidCardException;
import main.helpers.InputValidator;
import main.models.*;

public class TurnState extends GameState {

    private boolean isInFinalRound;

    public TurnState(GameStateManager gsm, GameContext context) {
        /*
         * grabbing all the necessary information from Game Context
         */
        super(gsm, context);
        this.isInFinalRound = context.getIsInFinalRound();
    }

    // Method to check if the game reached its final round conditions
    // 1. When a player has collected all 6 colors
    // 2. The draw pile is exhausted
    // Final Round: Play one more turn (without drawing a card from draw pile) -->
    // everyone should 4 cards left in hand
    @Override
    public void enter() {
        System.out.println("Entering TurnState");
        System.out.println("\033c");

        while (!isInFinalRound) {
            // get current player
            Player currentPlayer = playerList.get(currentPlayerIndex);
            // call playturn(currentplayer)
            playTurn(currentPlayer, false);

            /*
             * after player has played their turn,
             * check if deck size is 0, or if playerboard has all 6 colors. if so, set
             * isInFinalRound to true and update
             * setFinalRoundTriggerPlayerIndex to the currentPlayerIndex so we
             * can go one final round and stop after that exact player has ended their turn
             */
            if (currentPlayer.hasCollectedAllColours() || deck.isEmpty()) {
                context.setInFinalRound(true);
                isInFinalRound = true;
                System.out.println("You have collected all 6 colors! Moving on to the final round!");
                context.setFinalRoundTriggerPlayerIndex(currentPlayerIndex);
                this.finalPlayerIndex = currentPlayerIndex;

            }

            // move to the next player if the final round is not triggered
            this.currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
        }
        /*
         * Once we reach here, we know that the conditions for the final round has
         * been triggered, so we move into the final round.
         */
        finalRound();
    }

    public void playTurn(Player current, Boolean isFinalTurn) {
        // Get current players hand and their board
        System.out.println(current.getPlayerName() + "'s turn.");
        PlayerHand currentHand = current.getPlayerHand(); // cards in the hand of current player
        PlayerBoard currentplayerBoard = current.getPlayerBoard(); // card in the board of the current player

        System.out.println(getDisplay(current) + "\n");

        // Keep in loop and only break once the valid card is specified

        if (current instanceof RandomBot) {
            while (true) {
                try {
                    // Obtain the index of the card the user wants to play
                    Random random = new Random();
                    int playIndex = random.nextInt(5) + 1;
                    System.out.printf("The bot is going to play: %d\n", playIndex);
                    Card chosenCard = currentHand.getCardList().get(playIndex);
                    System.out.println();

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Display to user which card they played
                    System.out.println("Bot played: ");
                    System.out.println(chosenCard);
                    System.out.println();
                    /*
                     * play the chosen Card
                     */
                    playCard(chosenCard, currentplayerBoard);
                    currentHand.removeCard(chosenCard);
                    currentHand.drawCard(deck);

                    System.out.println("\033c");

                    break;
                } catch (InvalidCardException e) {
                    System.out.println("Invalid card. Please enter a valid card.");
                }
            }
        } else if (current instanceof SmartBot) {
            while (true) {
                try {
                    int numCardsKicked = 0;
                    int bestIndex = 1;
                    int leastNumCardsKicked = 0;
                    for(int i = 1; i <= 5; i++){
                        Card chosenCard = currentHand.getCardList().get(i-1);
                        numCardsKicked = simulatePlayCard(chosenCard, currentplayerBoard);
                        // System.out.printf("current card: %d\n current card kicks: %d\n\n\n", i, numCardsKicked);
                        if(i == 1){
                            leastNumCardsKicked = numCardsKicked;
                        } else {
                            if(numCardsKicked < leastNumCardsKicked){
                                bestIndex = i;
                            }
                        }
                    }


                    int playIndex = bestIndex;
                    System.out.printf("The bot is going to play but smartly: %d\n", playIndex);
                    System.out.println();

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Display to user which card they played
                    System.out.println("Bot played: ");
                    System.out.println(currentHand.getCardList().get(bestIndex-1));
                    System.out.println();
                    /*
                     * play the chosen Card
                     */
                    playCard(currentHand.getCardList().get(bestIndex-1), currentplayerBoard);
                    currentHand.removeCard(currentHand.getCardList().get(bestIndex-1));
                    currentHand.drawCard(deck);

                    System.out.println("\033c");

                    break;
                } catch (InvalidCardException e) {
                    System.out.println("Invalid card. Please enter a valid card.");
                }
            }
        } else {
            while (true) {
                try {
                    // Obtain the index of the card the user wants to play
                    int playIndex = InputValidator.getIntInRange(
                            String.format("Which card would you like to play?(%d to %d): ",
                                    1, currentHand.getCardList().size()),
                            1, currentHand.getCardList().size()) - 1;
                    Card chosenCard = currentHand.getCardList().get(playIndex);
                    System.out.println();

                    // Display to user which card they played
                    System.out.println("You have played: ");
                    System.out.println(chosenCard);
                    System.out.println();
                    /*
                     * play the chosen Card
                     */
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
        // Get the numerical value of the chosen card
        int chosenValue = chosenCard.getValue();

        // Create a list to keep track of cards that will be removed from the parade
        ArrayList<Card> removedCards = new ArrayList<>();

        /*
         * Iterate through the parade board, excluding the last 'chosenValue' cards.
         * These excluded cards are 'safe' and not eligible for removal this turn.
         */
        for (int i = 0; i < paradeBoard.getCardList().size() - chosenValue; i++) {
            Card currentParadeCard = paradeBoard.getCardList().get(i);

            // Condition for removal:
            // Remove the card if it has equal or lower value than the chosen card,
            // OR if it shares the same color
            if (currentParadeCard.getValue() <= chosenValue
                    || currentParadeCard.getColor().equals(chosenCard.getColor())) {
                removedCards.add(currentParadeCard);
            }
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Display summary of turn: show the chosen card, removed cards, and remaining
        // parade
        System.out.println("Turn Summary:");
        System.out.println(paradeBoard.toString(removedCards, chosenCard));
        System.out.println();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\033c");

        // Move removed cards from ParadeBoard to PlayerBoard
        for (int i = 0; i < removedCards.size(); i++) {
            Card currentParadeCard = removedCards.get(i);
            paradeBoard.remove(currentParadeCard);
            currentPlayerBoard.addCard(currentParadeCard);
        }

        // Add the chosen card to the end of the parade board
        paradeBoard.addToBoard(chosenCard);

        System.out.println();
    }

    public int simulatePlayCard(Card chosenCard, PlayerBoard currentPlayerBoard) {
        int counter = 0;
        // Get the numerical value of the chosen card
        int chosenValue = chosenCard.getValue();

        // Create a list to keep track of cards that will be removed from the parade
        ArrayList<Card> removedCards = new ArrayList<>();

        /*
         * Iterate through the parade board, excluding the last 'chosenValue' cards.
         * These excluded cards are 'safe' and not eligible for removal this turn.
         */
        for (int i = 0; i < paradeBoard.getCardList().size() - chosenValue; i++) {
            Card currentParadeCard = paradeBoard.getCardList().get(i);

            // Condition for removal:
            // Remove the card if it has equal or lower value than the chosen card,
            // OR if it shares the same color
            if (currentParadeCard.getValue() <= chosenValue
                    || currentParadeCard.getColor().equals(chosenCard.getColor())) {
                removedCards.add(currentParadeCard);
                counter++;
            }
        }

        return counter;
    }


    public void finalRound() {
        System.out.println("Each player gets one final turn! No more cards will be drawn!");

        // so we know who to stop at
        // Loop until it reaches the trigger player, everyone including the trigger
        // player should play the last round
        while (currentPlayerIndex != finalPlayerIndex) {
            System.out.println("finalPlayerIndex" + finalPlayerIndex);

            System.out.println("currentPlayerIndex" + currentPlayerIndex);
            Player currentPlayer = playerList.get(currentPlayerIndex);
            playTurn(currentPlayer, true);
            this.currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
        }
        // after loop ends, its the trigger player's turn to play their final turn
        playTurn(playerList.get(finalPlayerIndex), true);

        this.currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
        // enter the endgame
    }

    // huh. this is not implemented properly
    @Override
    public void exit() {
        System.out.println("Exiting TurnState.");
    }

}

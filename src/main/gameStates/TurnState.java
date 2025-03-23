package main.gameStates;

import java.util.ArrayList;
import main.context.GameContext;
import main.error.InvalidCardException;
import main.helpers.InputValidator;
import main.models.*;

public class TurnState implements GameState {

    private final GameContext context;
    private final ArrayList<Player> playerList;
    private int currentPlayerIndex;
    private ParadeBoard paradeBoard;
    private Deck deck;
    private boolean isInFinalRound;

    public TurnState(GameStateManager gsm, GameContext context) {

        /*
         * grabbing all the necessary information from Game Context
         */
        this.context = context;
        this.playerList = context.getPlayerList();
        this.currentPlayerIndex = context.getCurrentPlayerIndex();
        this.paradeBoard = context.getParadeBoard();
        this.deck = context.getDeck();
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
        while (!isInFinalRound) {
            // get current player
            Player currentPlayer = playerList.get(currentPlayerIndex);
            // call playturn(currentplayer)
            playTurn(currentPlayer, false);

            /*
               after player has played their turn,
               check if deck size is 0, or if playerboard has all 6 colors. if so, set
               isInFinalRound to true and update 
               setFinalRoundTriggerPlayerIndex to the currentPlayerIndex so we
               can go one final round and stop after that exact player has ended their turn
             */
            if (currentPlayer.hasCollectedAllColours()) {
                context.setInFinalRound(true);
                isInFinalRound = true;
                System.out.println("You have collected all 6 colors! Moving on to the final round!");
                context.setFinalRoundTriggerPlayerIndex(currentPlayerIndex);
            } else {
                if (deck.isEmpty()) {
                    context.setInFinalRound(true);
                    isInFinalRound = true;
                    System.out.println("The deck is empty! Moving on to the final round!");
                    context.setFinalRoundTriggerPlayerIndex(currentPlayerIndex);
                }
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
        //Get current players hand and their board
        System.out.println(current.getPlayerName() + "'s turn.");
        PlayerHand currentHand = current.getPlayerHand();
        PlayerBoard currentplayerBoard = current.getPlayerBoard();

        System.out.println(getDisplay(current));

        // Keep in loop and only break once the valid card is specified
        while (true) {
            try {

                //Obtain the index of the card the user wants to play
                int playIndex = InputValidator.getIntInRange(
                        String.format("Which card would you like to play?(%d to %d): ",
                                1, currentHand.getCardList().size()),
                        1, currentHand.getCardList().size()) - 1;
                Card chosenCard = currentHand.getCardList().get(playIndex);

                //Display to user which card they played
                System.out.println("You have played: ");
                System.out.println(chosenCard);
                System.out.println();

                /*
                 * play the chosen Card
                 */
                playCard(chosenCard, currentplayerBoard);
                currentHand.removeCard(chosenCard);

                //huh isnt this redundant since u are always passing in false
                if (!isFinalTurn) {
                    currentHand.drawCard(deck);
                }
                
                break;
            } catch (InvalidCardException e) {
                System.out.println("Invalid card. Please enter a valid card.");
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

        // Display summary of turn: show the chosen card, removed cards, and remaining parade
        System.out.println("Turn Summary:");
        System.out.println(paradeBoard.toString(removedCards, chosenValue, chosenCard));
        System.out.println();

        //Move removed cards from ParadeBoard to PlayerBoard
        for (int i = 0; i < removedCards.size(); i++) {
            Card currentParadeCard = removedCards.get(i);
            paradeBoard.remove(currentParadeCard);
            currentPlayerBoard.addCard(currentParadeCard);
        }

        //Add the chosen card to the end of the parade board
        paradeBoard.addToBoard(chosenCard);
        System.out.println();
    }

    public void finalRound() {
        System.out.println("Each player gets one final turn! No more cards will be drawn!");

        // so we know who to stop at
        int finalPlayerIndex = context.getFinalRoundTriggerPlayerIndex();
        // Loop until it reaches the trigger player, everyone including the trigger
        // player should play the last round
        while (currentPlayerIndex != finalPlayerIndex) {
            Player currentPlayer = playerList.get(currentPlayerIndex);
            playTurn(currentPlayer, true);
            this.currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
        }
        // after loop ends, its the trigger player's turn to play their final turn
        playTurn(playerList.get(finalPlayerIndex), true);

        this.currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
        // enter the endgame
    }

    //huh. this is not implemented properly
    @Override
    public void exit() {
        System.out.println("Exiting TurnState.");
    }


    //Below are all the relevant display functions
    public String getDisplay(Player currentPlayer) {
        StringBuilder result = new StringBuilder();
        result.append("\u001B[0m"); // triggers ANSI processing
    
        result.append("Parade Board:\n");
        result.append(paradeBoard.toString()).append("\n\n");
    
        result.append("Here is your board:\n");
        result.append(getPlayerBoardDisplay(currentPlayer.getPlayerBoard()));
    
        result.append("Here is your hand:\n");
        result.append("\n"); // Insert extra newline to ensure the hand starts on a fresh line
        result.append(getHandDisplay(currentPlayer.getPlayerHand()));
    
        return result.toString();
    }
    
        public String getDisplay() {
            String result = "";
    
            // Display the parade board
            result += "Parade Board:\n";
            result += paradeBoard + "\n".repeat(3);
    
            for (Player curr : playerList) {
                result += curr.getPlayerName() + "'s board\n";
                result += getPlayerBoardDisplay(curr.getPlayerBoard());
                if (!curr.getPlayerHand().getCardList().isEmpty()) {
                    result += curr.getPlayerName() + "'s hand\n";
                    result += getHandDisplay(curr.getPlayerHand());
                }
            }
    
            result += "\n";
            return result;
        }
    
        public String getHandDisplay(PlayerHand playerHand) {
            String result = "";
    
            result += playerHand + "\n";
            return result;
        }
    
        public String getPlayerBoardDisplay(PlayerBoard currentplayerBoard) {
            String result = "";
            if (currentplayerBoard.isEmpty()) {
                System.out.println();
                result += "Playerboard is empty.\n\n";
            } else {
                result += currentplayerBoard + "\n\n";
            }
            return result;
        }

    
}
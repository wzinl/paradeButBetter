package main.gameStates;

import java.util.ArrayList;
import main.context.GameContext;
import main.error.InvalidCardException;
import main.helpers.InputValidator;
import main.models.*;

public class TurnState implements GameState {

    private final GameStateManager gsm;
    private final GameContext context;
    private final ArrayList<Player> playerList;
    private int currentPlayerIndex;
    private ParadeBoard paradeBoard;
    private Deck deck;
    private boolean isInFinalRound;

    public TurnState(GameStateManager gsm, GameContext context) {
        this.gsm = gsm;
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

            // draw card from deck
            PlayerHand playerHand = currentPlayer.getPlayerHand();

            // check if deck size is 0, or if playerboard has all 6 colors. if so, set
            // isInFinalRound to true
            // update setFinalRoundTriggerPlayerIndex to the currentPlayerIndex

            if (currentPlayer.hasCollectedAllColours()) {
                context.setInFinalRound(true);
                isInFinalRound = true;
                System.out.println("You have collected all 6 colors! Moving on to the final round!");
                // this setter is to know where to stop at when this player has played his last
                // card
                context.setFinalRoundTriggerPlayerIndex(currentPlayerIndex);
            } else {
                if (deck.isEmpty()) {
                    context.setInFinalRound(true);
                    isInFinalRound = true;
                    System.out.println("The deck is empty! Moving on to the final round!");
                    // this setter is to know where to stop at when this player has played his last
                    // card
                    context.setFinalRoundTriggerPlayerIndex(currentPlayerIndex);
                }
            }
        //   move to the next player if the final round is not triggered
        this.currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
        }
        finalRound();
    }

    @Override
    public void exit() {
        System.out.println("Exiting TurnState.");
    }

    public void playTurn(Player current,Boolean isFinalTurn) {
        System.out.println(current.getPlayerName() + "'s turn.");
        PlayerHand currentHand = current.getPlayerHand();
        PlayerBoard currentplayerBoard = current.getPlayerBoard();

        System.out.println(getDisplay(current));

        // Keep in loop and only break once valid card is specified
        while (true) {
            try {
                int playIndex = InputValidator.getIntInRange(String.format("Which card would you like to play?(%d to %d): ",
                                 1, currentHand.getCardList().size()),
                                1, currentHand.getCardList().size()) - 1;
                Card chosenCard = currentHand.getCardList().get(playIndex) ;

                System.out.println("You have played: ");
                System.out.println(chosenCard);
                System.out.println();

                playCard(chosenCard, currentplayerBoard);
                currentHand.removeCard(chosenCard);

                if(!isFinalTurn){
                    currentHand.drawCard(deck);
                }
                break;
            } catch (InvalidCardException e) {
                System.out.println("Invalid card. Please enter a valid card.");
            }
        }
        // look at face value of card placed onto the end of the board. count that many
        // cards from the end.

        // from the remaining cards, select any cards equal to or less than the card
        // that was played, as well as any cards of the same color
        // these cards are removed from paradeBoard and added to player's playerboard
        // remove cards from paradeBoard; playCard();

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
        System.out.println("Turn Summary:");
        System.out.println(paradeBoard.toString(removedCards, chosenValue, chosenCard));
        System.out.println();

        for (int i = 0; i < removedCards.size(); i++) {
            Card currentParadeCard = removedCards.get(i);
            paradeBoard.remove(currentParadeCard);
            currentPlayerBoard.addCard(currentParadeCard);
        }
        
        paradeBoard.addToBoard(chosenCard);
        System.out.println();
    }

    public String getDisplay(Player currentPlayer) {
        String result = "";

        // Display the parade board
        result += "Parade Board:\n";
        result += paradeBoard + "\n\n";

        result += "Here is your board:\n";
        result += getPlayerBoardDisplay(currentPlayer.getPlayerBoard());
        result += "Here is your hand:\n";
        result += getHandDisplay(currentPlayer.getPlayerHand());
        return result;
    }

    public String getDisplay() {
        String result = "";

        // Display the parade board
        result += "Parade Board:\n";
        result += paradeBoard + "\n".repeat(3);

        for(Player curr : playerList){
            result += curr.getPlayerName() + "'s board\n";
            result += getPlayerBoardDisplay(curr.getPlayerBoard());
            if(!curr.getPlayerHand().getCardList().isEmpty()){
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
        if(currentplayerBoard.isEmpty()){
            System.out.println();
            result += "Playerboard is empty.\n\n";
        }else{
            result += currentplayerBoard + "\n\n";
        }
        return result;
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
}
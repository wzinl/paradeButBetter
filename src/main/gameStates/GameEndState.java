package main.gameStates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import main.context.GameContext;
import main.error.InvalidCardException;
import main.helpers.InputValidator;
import main.models.*;

public class GameEndState implements GameState {
    private GameStateManager gsm;
    private GameContext context;
    private final ArrayList<Player> playerList;
    private int currentPlayerIndex;
    private ParadeBoard paradeBoard;
    private Deck deck;

    public GameEndState(GameStateManager gsm, GameContext context) {
        this.gsm = gsm;
        this.context = context;
        this.playerList = context.getPlayerList();
        this.currentPlayerIndex = context.getCurrentPlayerIndex();
        this.paradeBoard = context.getParadeBoard();
        this.deck = context.getDeck();

    }

    // take in arrayist of players first
    // for each player get playeyrBoard
    // find who has the most cards per color
    // change value of isFlipped to true for each one
    // calculate score afterwards, flipped cards = 1 point each.

    @Override
    public void enter() {
        try {
            System.out.println("Game End State entering");

            // display the 4 cards in the players hand
            // then we go auto adding the

            // Player turn, add/discard 2 cards
            for (Player player : playerList) {


                PlayerHand currentHand = player.getPlayerHand();
                PlayerBoard currentPlayerBoard = player.getPlayerBoard();
                System.out.println(player.getPlayerName() + "'s turn");
                System.out.println("Current Game State");
                getDisplay(player);
                System.out.println("Discard 2 cards from hand.");
                System.out.println();

                System.out.println(currentHand); // display the current hand
                System.out.println();

                int discardCardIndex1 = InputValidator.getIntInRange(String.format("Select first card to discard: "),
                                1, currentHand.getCardList().size()) - 1;
                
                Card removeCard1 = currentHand.getCardList().get(discardCardIndex1);
                currentHand.removeCard(removeCard1);

                System.out.println(currentHand);
                System.out.println();
                int discardCardIndex2 = InputValidator.getIntInRange(String.format("Select second card to discard: "),
                                1, currentHand.getCardList().size()) -1;
                Card removeCard2 = currentHand.getCardList().get(discardCardIndex2);
                currentHand.removeCard(removeCard2);


                for (Card card : currentHand.getCardList()) {
                    player.getPlayerBoard().addCard(card);

                }
                currentHand.getCardList().clear();
            
            }
            
            // if there are more than 2 players
            for (String color : Deck.colours) { // looping through each color
                int highestColor = -1;
    
            if(playerList.size() > 2){
                    // get players with the highest cards per color
                    for (Player player : playerList) {
                        int playerColor = player.getPlayerBoard().getCardNumberByColor(color);
                        highestColor = Math.max(highestColor, playerColor);
                    }
                    // flip cards of players with highest cards per color
                    for (Player player : playerList) {
                            if (player.getPlayerBoard().getCardNumberByColor(color) == highestColor) {
                                for (Card card : player.getPlayerBoard().getPlayerBoardHash().get(color)) {
                                    card.setIsFaceUp(false);
                                }
                            }
                    }
                } else{
                    Player player1 = playerList.get(0);
                    Player player2 = playerList.get(1);
                    int player1Count = player1.getPlayerBoard().getCardNumberByColor(color);
                    int player2Count = player2.getPlayerBoard().getCardNumberByColor(color);

                    if (Math.abs(player1Count - player2Count) >= 2) {
                        PlayerBoard currentBoard = player1Count > player2Count ? player1.getPlayerBoard() : player2.getPlayerBoard();
                        for (Card card : currentBoard.getPlayerBoardHash().get(color)) {
                            card.setIsFaceUp(false);
                        }
                    }

                }
            } 
            
            ArrayList<Player> winners = new ArrayList<>(playerList);
                // sort by descending order so that the winner with the highest score comes
                // first
                Collections.sort(winners, Comparator.comparing(Player::calculateScore)); //lowest score is in first place
                // undecided what to do with winners yet


            System.out.println(getDisplay());
            for (Player player : winners) {
                    System.out.println(player.getPlayerName() + " has scored: " + player.getPlayerScore());
                }
        } catch (InvalidCardException e) {
            System.out.println(e.getMessage());
        }

    }

    //Add in separate calculation scoring for 2 player game mode and >2 Players

    @Override
    public void exit() {

    }

    public String getDisplay(Player currentPlayer) {
        String result = "";

        // Display the parade board
        result += "Parade Board:\n";
        result += paradeBoard + "\n".repeat(3);

        result += "Here is your board:\n";
        result += getPlayerBoardDisplay(currentPlayer.getPlayerBoard());
        result += "Here is your hand:\n";
        result += getHandDisplay(currentPlayer.getPlayerHand());
        result += "\n";
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
        
        result += playerHand + "\n".repeat(3);
        return result;
    }

    public String getPlayerBoardDisplay(PlayerBoard currentplayerBoard) {
        String result = "";
        if(currentplayerBoard.isEmpty()){
            System.out.println();
            result += "Your playerboard is empty.\n";
        }else{
            result += currentplayerBoard + "\n";
        }
        return result;
    }
}


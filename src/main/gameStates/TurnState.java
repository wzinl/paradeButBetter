package main.gameStates;

import java.util.ArrayList;
import java.util.Scanner;

import main.context.GameContext;
import main.error.InvalidCardException;
import main.models.*;


public class TurnState implements GameState{

    private GameStateManager gsm;
    private GameContext context;
    
    public TurnState(GameStateManager gsm, GameContext context){
        this.gsm = gsm;
        this.context = context;
    }

    // Method to check if the game reached its final round conditions
    // 1. When a player has collected all 6 colors
    // 2. The draw pile is exhausted
    // Final Round: Play one more turn (without drawing a card from draw pile) --> everyone should 4 cards left in hand
    @Override
    public void enter(){
        GameContext context = GameContext.getInstance();
        Player currentplayer = context.getCurrentPlayer();
        Deck deck = context.getDeck(); 
        ParadeBoard paradeBoard = context.getParadeBoard();

        // Player plays their normal turn
        System.out.println(currentplayer.getPlayerName() + "'s turn!");

        // Display player's hand
        System.out.println("Your current hand: ");
        System.out.println(currentplayer.getPlayerHand().displayHand());

        // Choose a card from hand to add to board
        // Prompt the player to choose a card o display on board
        boolean validInput = false;
        Scanner scanner = new Scanner(System.in);

        while (!validInput){
            System.out.println("Choose a card to display on parade board (pick 0-4): "); // total 5 cards in hand
            int chosenIndex = scanner.nextInt();
            
            try {
                Card chosenCard = currentplayer.getPlayerHand().displayHand().get(chosenIndex);
                currentplayer.getPlayerHand().playCardFromHand(chosenCard, paradeBoard);
                System.out.println(currentplayer.getPlayerName() + "played" + chosenCard);
                validInput = true; // if no exception occur

                // player select the removal cards from parade board
                ArrayList<Card> cardsToAdd = paradeBoard.removefromBoard(chosenCard);

                // add the list of "removal" cards to player board
                currentplayer.getPlayerBoard().addCards(cardsToAdd);

                // player draw card
                currentplayer.getPlayerHand().drawCard(deck);

            } catch (IndexOutOfBoundsException e){
                System.out.println("Invalid card index! Try again.");
            } catch (InvalidCardException e){
                System.out.println("Error playing the card"); // if card not in hand, clarify!!!
            }
        }

        // check if the player has collected all 6 colors or if the draw pie is exhausted
        if (currentplayer.hasCollectedAllColours() || deck.isDeckEmpty() ){
            // set the "trigger last round player index"
            context.setFinalRoundTriggerPlayerIndex(context.getCurrentPlayerIndex());

            // Start last round 
            startFinalRound();

        } 
        // else continue with normal turn
        // Move to next player for the next turn
        context.nextTurn();
    }


    // Method for starting the last round
    private void startFinalRound(){
        System.out.println("Each player gets one final turn! No more cards will be drawn.");

        // Mark the game as in final round mode
        context.setInFinalRound(true);

        // Get the player who triggered the last round
        int finalRoundTriggerPlayerIndex = context.getFinalRoundTriggerPlayerIndex();
        int numberOfPlayers = context.getPlayerList().size();
        
        // Start the loop from the next player after the trigger player
        int currentIndex = (finalRoundTriggerPlayerIndex + 1)% numberOfPlayers;

        // Loop until it reaches the triger player, including the trigger player should play last round
        do {
            Player currentPlayer = context.getPlayerList().get(currentIndex);
            playFinalTurn(currentPlayer);
        } while (currentIndex != finalRoundTriggerPlayerIndex); // stop when we reach the trigger player
        
        // after loop ends, its the trigger player's turn to play their final turn
        playFinalTurn(context.getPlayerList().get(finalRoundTriggerPlayerIndex));

        endGame(); // end the game after all the players have has their final round
    }

    // Method for players final turn without drawing a card
    private void playFinalTurn(Player player){
        System.out.println(player.getPlayerName() + "'s final turn!");

        // Play a card from player hand to board
        boolean validInput = false;
        Scanner scanner = new Scanner(System.in);

        while (!validInput){
            System.out.println("Choose a card to display on parade board (pick 0-4): "); // total 5 cards in hand
            int chosenIndex = scanner.nextInt();
            
            try {
                Card chosenCard = player.getPlayerHand().displayHand().get(chosenIndex);
                player.getPlayerHand().playCardFromHand(chosenCard, context.getParadeBoard());
                System.out.println(player.getPlayerName() + "played" + chosenCard);
                validInput = true; // if no exception occur

                // player select the removal cards from parade board
                ArrayList<Card> cardsToAdd = context.getParadeBoard().removefromBoard(chosenCard);

                // add the list of "removal" cards to player board
                player.getPlayerBoard().addCards(cardsToAdd);

            } catch (IndexOutOfBoundsException e){
                System.out.println("Invalid card index! Try again.");
            } catch (InvalidCardException e){
                System.out.println("Error playing the card"); // if card not in hand, clarify!!!
            }
        }

    }

// End the game after the last round
    private void endGame() {
        System.out.println("Game over! The last round has concluded.");

        // Logic to determine the winner can go here (e.g., based on scores or number of cards remaining)

        // Transition the game state to a "GameOverState" or similar
        gsm.setState(new GameEndState());
    }


    @Override
    public void exit() {
        System.out.println("Exiting TurnState.");
    }
}
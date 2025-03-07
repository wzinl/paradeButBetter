package main.gameStates;

import main.context.GameContext;
import main.models.Deck;
import main.models.ParadeBoard;
import main.models.Player;
import main.models.PlayerBoard;

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
        System.out.println(currentplayer.getPlayerName() + "'s turn!");

        // Handle Player's turn logic
        // draw card

        // check if the player has collected all 6 colors or if the draw pie is exhausted
        if (currentplayer.hasCollectedAllColours() || deck.isDeckEmpty() ){

            // Start last round 
            startLastRound();

        } else {
            // Normal turn logic where the player draw and plays
            drawCardAndPlay(currentplayer, deck, paradeBoard);
        }

        // Move to next player for the next turn
        context.nextTurn();
    }
        // Normal turn (drawing and playing cards)
        private void drawCardAndPlay(Player currentplayer, Deck deck, ParadeBoard paradeBoard){
            // to fill up...
            
        }

        // Actions after playing card on the parade
        private void PlayCardOnParade(Player currentplayer, ParadeBoard paradeBoard){
            
        }

        // Method for starting the last round
        private void startLastRound(){
            System.out.println("The last round has started.");
        }

        // Method for players final turn without drawing a card
        private void playFinalTurn(Player player){

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
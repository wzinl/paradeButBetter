package main;
import main.gameStates.GameStateManager;
import main.helpers.InputHandler;
import main.helpers.ScreenUtils;

public class Game {
    private GameStateManager gsm;

    public Game() {
        /*
         * Creating a new Game state manager object, the purpose of
         * game state:
         * 1. scalability
         * 2. modularity 
         * 3. easier to debug
         * there are 3 game states:
         * 1. InitState(GameStateManager gsm) - initialises all the necessary classes
         * 2. TurnState(GameStateManager gsm) - where the main logic of Parade occurs
         * 3. GameEndState(GameStateManager gsm) - the final actions and the calculation of player scores
         */

        gsm = new GameStateManager(new InputHandler()); 
        ScreenUtils.clearScreen();
        
    }

    public void run() {
        //
        // InitState init = new InitState(gsm);

        /*
         * Set current state to InitState, where the Game is initialised
         */

         
        gsm.init(); 

        //Once InitState is done, we move to Turn State.
        gsm.nextState();
    }

    //kickstarting Parade!
    public static void main(String[] args) {
        
        new Game().run();
    }
}

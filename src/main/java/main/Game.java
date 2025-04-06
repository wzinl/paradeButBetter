package main;

import main.gameStates.GameStateManager;
import main.helpers.inputHandlers.InputManager;
import main.helpers.ui.UIManager;

public class Game {
    private final GameStateManager gsm;
    private boolean isRunning; // Flag to control the game loop

    public Game() {        
        gsm = new GameStateManager(new InputManager(), this);
        isRunning = true; // Initialize the game as running
    }

    public void run() {
        InputManager inputManager = gsm.getInputManager();
        // UIManager.displayIntroduction();
        UIManager.displayInstructions();

        inputManager.getEnter();

        
        gsm.init();

        // Main game loop
        while (isRunning) {
            gsm.nextState(); // Transition to the next state
        }
        exit();
        
    }

    public void exit() {
        System.out.println("Game has stopped. Thank you for playing!");
        isRunning = false; // Set the flag to false to stop the game
        System.exit(0);
    }

    // Kickstarting Parade!
    public static void main(String[] args) {
        Game game = new Game();
        game.run();
    }
}
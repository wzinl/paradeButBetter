package main;

import java.io.IOException;

import org.fusesource.jansi.AnsiConsole;

import main.gameStates.GameStateManager;
import main.helpers.inputHandlers.InputManager;
import main.helpers.ui.UIManager;

public class Game {
    private final GameStateManager gsm;
    private final static String[] introActions = {"Start Game", "Game Rules", "Quit Game"};
    private boolean isRunning; // Flag to control the game loop
    InputManager inputManager;

    public Game() {        
        gsm = new GameStateManager(new InputManager(), this);
        isRunning = true; // Initialize the game as running
        inputManager = gsm.getInputManager();

    }

    public void intro() {
        UIManager.displayIntroduction();
        boolean validInput = false;

        while (!validInput) {
            try {
                char introInput = inputManager.getIntroInput(introActions).getActionChar();

                // Map the input to corresponding methods
                switch (Character.toLowerCase(introInput)) {
                    case 's' -> {
                        validInput = true;
                    }
                    case 'g' -> {
                        gameRules(); // Call gameRules for 'g'
                    }
                    case 'q' -> {
                        validInput = true;
                        exit(); // Call exit for 'q'
                    }
                    default -> UIManager.displayMessage("Invalid input. Please try again.");
                }
            } catch (IOException e) {
                UIManager.displayMessage("There has been an error with the input selection setup!");
                UIManager.displayMessage("Exiting the game...");
                exit();
            }
        }
    }

    public void gameRules() {
        UIManager.displayInstructions();
        inputManager.getEnter();
    }

    public void run() {
        AnsiConsole.systemInstall();
        intro();
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
        AnsiConsole.systemUninstall();
        System.exit(0);
    }

    // Kickstarting Parade!
    public static void main(String[] args) {
        Game game = new Game();
        game.run();
    }
}
package parade;

import java.io.IOException;

import org.fusesource.jansi.AnsiConsole;

import parade.gameStates.GameStateManager;
import parade.helpers.inputHandlers.InputManager;
import parade.helpers.ui.UIManager;

/**
 * Main class for the Parade game. Handles game initialization, intro menu,
 * and managing the main game loop and exit process.
 */
public class Game {

    /** Manages the state transitions between different game states. */
    private final GameStateManager gsm;

    /** Intro menu options displayed to the player. */
    private final static String[] introActions = {"Start Game", "Game Rules", "Exit Game"};

    /** Flag used to control the main game loop. */
    private boolean isRunning;

    /** Handles user input throughout the game. */
    InputManager inputManager;

    /**
     * Constructs a new Game instance, initializing the game state manager and input manager.
     */
    public Game() {
        gsm = new GameStateManager(new InputManager(), this);
        isRunning = true;
        inputManager = gsm.getInputManager();
    }

    /**
     * Displays the introduction screen and handles the user's menu selection:
     * start the game, view the game rules, or exit.
     *
     * @throws IOException if there is an error during input selection
     */
    public void intro() {
        UIManager.displayIntroduction();
        boolean validInput = false;

        while (!validInput) {
            try {
                char introInput = inputManager.getIntroInput(introActions).getActionChar();

                switch (Character.toLowerCase(introInput)) {
                    case 's' -> validInput = true;
                    case 'g' -> gameRules();
                    case 'q' -> {
                        validInput = true;
                        exit();
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

    /**
     * Displays the game rules and waits for the player to press Enter to return to the previous screen.
     */
    public void gameRules() {
        UIManager.displayInstructions();
        inputManager.getEnter();
    }

    /**
     * Starts the game, shows the intro screen, and begins the main game loop.
     * This method installs the Jansi console handler and ensures the game transitions
     * between states using the GameStateManager.
     */
    public void run() {
        AnsiConsole.systemInstall();
        intro();
        gsm.init();

        while (isRunning) {
            gsm.nextState();
        }

        exit();
    }

    /**
     * Terminates the game cleanly, displaying an exit message,
     * uninstalling the Jansi console handler, and exiting the JVM.
     */
    public void exit() {
        UIManager.clearScreen();
        System.out.println("Game has stopped. Thank you for playing!");
        isRunning = false;
        AnsiConsole.systemUninstall();
        System.exit(0);
    }

    /**
     * Main entry point for launching the Parade game.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.run();
    }
}

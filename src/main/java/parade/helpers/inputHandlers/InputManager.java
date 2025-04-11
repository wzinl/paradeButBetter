package parade.helpers.inputHandlers;

import java.io.IOException;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import parade.helpers.inputTypes.ActionInput;
import parade.helpers.inputTypes.SelectionInput;
import parade.models.ParadeBoard;
import parade.models.player.Player;

/**
 * Manages user input mode switching and delegates input to the appropriate handler.
 * Supports both line-based and menu-based input through {@link LineInputHandler} and {@link MenuInputHandler}.
 */
public class InputManager {

    /** The JLine terminal instance used for reading input. */
    protected Terminal terminal;

    /** Handler for line-based input (text entry). */
    protected LineInputHandler lineHandler;

    /** Handler for menu-based input (interactive selection). */
    protected MenuInputHandler menuHandler;

    /** The currently active input handler. */
    protected InputHandler currentHandler;

    /** Global actions always available during turn input. */
    private static final String[] ACTIONS = {
        "Change Input Type",
        "Display Everybody's Boards",
        "Exit Game"
    };

    /**
     * Constructs an InputManager and initializes both input handlers with a shared terminal.
     * Sets line input as the default mode.
     */
    public InputManager() {
        try {
            this.terminal = TerminalBuilder.builder().system(true).build();
            this.lineHandler = new LineInputHandler(terminal);
            this.menuHandler = new MenuInputHandler(terminal);
            currentHandler = lineHandler;
            lineHandler.startInput();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize terminal input", e);
        }
    }

    /**
     * Requests turn input from the player using their preferred input method.
     *
     * @param paradeBoard     the current parade board state
     * @param currentPlayer   the player making a decision
     * @return the selection made by the player
     */
    public SelectionInput turnSelect(ParadeBoard paradeBoard, Player currentPlayer) {
        try {
            ensureCorrectInputHandler(currentPlayer.getPreferMenu());
            return currentHandler.turnSelect(paradeBoard, currentPlayer, ACTIONS);
        } catch (IOException e) {
            throw new RuntimeException("Error during turn selection", e);
        }
    }

    /**
     * Ensures the correct input handler is active based on the player's preference.
     *
     * @param preferMenu whether the player prefers menu-based input
     * @throws IOException if the input switch fails
     */
    private void ensureCorrectInputHandler(boolean preferMenu) throws IOException {
        if (preferMenu) {
            ensureMenuInput();
        } else {
            ensureLineInput();
        }
    }

    /**
     * Switches to the line input handler if it is not already active.
     */
    public void ensureLineInput() {
        try {
            switchInputHandler(lineHandler);
        } catch (IOException e) {
            throw new RuntimeException("Failed to switch to LineInputHandler", e);
        }
    }

    /**
     * Switches to the menu input handler if it is not already active.
     */
    public void ensureMenuInput() {
        try {
            switchInputHandler(menuHandler);
        } catch (IOException e) {
            throw new RuntimeException("Failed to switch to MenuInputHandler", e);
        }
    }

    /**
     * Switches from the current input handler to the given handler.
     *
     * @param newHandler the input handler to switch to
     * @throws IOException if input shutdown/start fails
     */
    private void switchInputHandler(InputHandler newHandler) throws IOException {
        if (currentHandler != newHandler) {
            currentHandler.stopInput();
            newHandler.startInput();
            currentHandler = newHandler;
        }
    }

    /**
     * Displays the intro menu and waits for the player to select an action.
     *
     * @param introActions the list of intro menu options
     * @return the action input selected by the player
     * @throws IOException if input fails
     */
    public ActionInput getIntroInput(String[] introActions) throws IOException {
        ensureMenuInput();
        return menuHandler.introSelect(introActions);
    }

    /**
     * Prompts the player for an integer within a specific range.
     *
     * @param prompt the prompt to display
     * @param min    the minimum allowed value (inclusive)
     * @param max    the maximum allowed value (inclusive)
     * @return the validated integer entered by the player
     */
    public int getIntInRange(String prompt, int min, int max) {
        ensureLineInput();
        return lineHandler.getIntInRange(prompt, min, max);
    }

    /**
     * Prompts the player for a string input.
     *
     * @param prompt the prompt to display
     * @return the string entered by the player
     */
    public String getString(String prompt) {
        ensureLineInput();
        return lineHandler.getString(prompt);
    }
    /**
     * Waits for the player to press Enter before continuing.
     */
    public void getEnter() {
        ensureLineInput();
        lineHandler.getEnter();
    }
}

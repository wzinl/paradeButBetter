package parade.helpers.inputHandlers;

import java.io.IOException;

import org.jline.terminal.Terminal;

import parade.helpers.inputTypes.SelectionInput;
import parade.models.ParadeBoard;
import parade.models.player.Player;

/**
 * Abstract class representing an input handler for managing player input during the game.
 * Subclasses can implement different styles of interaction, such as line input or menu-driven input.
 */
public abstract class InputHandler {

    /** The terminal interface used to handle raw input and rendering. */
    protected final Terminal terminal;

    /**
     * Constructs an InputHandler with the provided terminal instance.
     *
     * @param terminal the terminal used for reading input and rendering output
     */
    public InputHandler(Terminal terminal) {
        this.terminal = terminal;
    }

    /**
     * Flushes any buffered output or input state.
     * Called when a refresh or cleanup of the input handler is required.
     */
    protected abstract void flush();

    /**
     * Handles the player's selection during their turn.
     *
     * @param paradeBoard     the current state of the parade board
     * @param currentPlayer   the player making a selection
     * @param actionStrings   the list of allowed action commands (e.g., "Change Input Type", "Exit Game")
     * @return a {@link SelectionInput} representing the player's chosen input
     * @throws IOException if an error occurs while reading input
     */
    public abstract SelectionInput turnSelect(ParadeBoard paradeBoard, Player currentPlayer, String[] actionStrings) throws IOException;

    /**
     * Starts the input handling session. Can be used to prepare the terminal or resources.
     *
     * @throws IOException if an error occurs while starting input
     */
    public abstract void startInput() throws IOException;

    /**
     * Stops the input handling session and cleans up any resources.
     *
     * @throws IOException if an error occurs while stopping input
     */
    public abstract void stopInput() throws IOException;
}

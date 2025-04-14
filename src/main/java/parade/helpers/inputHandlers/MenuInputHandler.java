package parade.helpers.inputHandlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.fusesource.jansi.Ansi;
import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Terminal;

import parade.helpers.inputTypes.ActionInput;
import parade.helpers.inputTypes.CardInput;
import parade.helpers.inputTypes.SelectionInput;
import parade.helpers.ui.DisplayFactory;
import parade.helpers.ui.UIManager;
import parade.models.ParadeBoard;
import parade.models.cards.Card;
import parade.models.player.Player;
import parade.models.player.PlayerHand;

/**
 * Handles menu-based input using arrow key navigation and ENTER key selection.
 * Supports two menu modes:
 * - In-turn card/action selection
 * - Intro menu action selection
 */
public class MenuInputHandler extends InputHandler {

    /** Key bindings for arrow and enter keys. */
    private static final KeyMap<String> keyMap = new KeyMap<>();

    static {
        keyMap.bind("UP", "w", "W", "\u001B[A");
        keyMap.bind("DOWN", "s", "S", "\u001B[B");
        keyMap.bind("LEFT", "a", "A", "\u001B[D");
        keyMap.bind("RIGHT", "d", "D", "\u001B[C");
        keyMap.bind("ENTER", "\r", "\n", "\r\n");
    }

    /**
     * Constructs a MenuInputHandler using the given terminal.
     *
     * @param terminal the terminal used to read raw key bindings
     * @throws IOException if terminal setup fails
     */
    public MenuInputHandler(Terminal terminal) throws IOException {
        super(terminal);
    }

    /**
     * Flushes any buffered characters from stdin before reading new input.
     * Prevents unintentional keystroke carryover.
     */
    @Override
    public void flush() {
        try {
            InputStream in = System.in;
            while (in.available() > 0) {
                in.read(); // discard input
            }
        } catch (IOException e) {
            System.out.println("Error flushing stdin: " + e.getMessage());
        }
    }

    /**
     * Enables raw input mode on the terminal to allow real-time key reading.
     *
     * @throws IOException if the terminal cannot be set to raw mode
     */
    @Override
    public void startInput() throws IOException {
        flush();
        terminal.enterRawMode();
    }

    /**
     * Stops the menu input session by flushing any remaining input.
     */
    @Override
    public void stopInput() {
        flush();
    }

    /**
     * Displays an interactive menu where the player can use arrow keys to
     * navigate between cards and actions, and press Enter to select.
     *
     * @param paradeBoard     the current state of the parade board
     * @param currentPlayer   the player taking their turn
     * @param actionStrings   a list of available action commands (e.g., "End Turn", "Exit")
     * @return a {@link SelectionInput} representing either a card or an action
     * @throws IOException if reading terminal input fails
     */
    @Override
    public SelectionInput turnSelect(ParadeBoard paradeBoard, Player currentPlayer, String[] actionStrings) throws IOException {
        flush();
        BindingReader bindingReader = new BindingReader(terminal.reader());
        int selectedIndex = 0;
        PlayerHand currHand = currentPlayer.getPlayerHand();
        List<Card> cardList = currHand.getCardList();
        int handSize = cardList.size();
        int actionStringsCount = actionStrings.length;

        boolean onCardRow = true;
        UIManager.printFormattedTurnDisplay(currentPlayer, paradeBoard, selectedIndex, actionStrings, onCardRow);

        while (true) {
            String key = bindingReader.readBinding(keyMap);

            if (key == null) {
                continue;
            }

            switch (key) {
                case "UP" -> {
                    if (!onCardRow) {
                        onCardRow = true;
                        selectedIndex = 0;
                    }
                }
                case "DOWN" -> {
                    if (onCardRow) {
                        onCardRow = false;
                        selectedIndex = 0;
                    }
                }
                case "RIGHT" -> {
                    selectedIndex++;
                    int max = onCardRow ? handSize : actionStringsCount;
                    if (selectedIndex >= max) {
                        selectedIndex = 0;
                    }
                }
                case "LEFT" -> {
                    selectedIndex--;
                    int max = onCardRow ? handSize : actionStringsCount;
                    if (selectedIndex < 0) {
                        selectedIndex = max - 1;
                    }
                }
                case "ENTER" -> {
                    if (onCardRow) {
                        return new CardInput(selectedIndex);
                    } else {
                        return new ActionInput(actionStrings[selectedIndex].toUpperCase().charAt(0));
                    }
                }
            }

            UIManager.clearScreen();
            UIManager.printFormattedTurnDisplay(currentPlayer, paradeBoard, selectedIndex, actionStrings, onCardRow);
        }
    }

    /**
     * Displays an interactive intro menu for the game, where the user selects from options like
     * "Start Game", "Game Rules", or "Exit Game".
     *
     * @param actions the list of menu options to display
     * @return the {@link ActionInput} corresponding to the selected menu option
     * @throws IOException if reading key input fails
     */
    public ActionInput introSelect(String[] actions) throws IOException {
        flush();
        String titleCard = DisplayFactory.getTitleCard();
        BindingReader bindingReader = new BindingReader(terminal.reader());
        int selectedIndex = 0;
        int actionCount = actions.length;

        UIManager.clearScreen();

        while (true) {
            UIManager.displayMessage(titleCard); // Show title

            // Render action menu with current selection highlighted
            for (int i = 0; i < actionCount; i++) {
                if (i == selectedIndex) {
                    System.out.println(Ansi.ansi().bold().fg(Ansi.Color.RED).a("> " + actions[i]).reset());
                } else {
                    System.out.println("  " + actions[i]);
                }
            }

            String key = bindingReader.readBinding(keyMap);

            if (key == null) continue;

            switch (key) {
                case "UP" -> {
                    selectedIndex--;
                    if (selectedIndex < 0) selectedIndex = actionCount - 1;
                }
                case "DOWN" -> {
                    selectedIndex++;
                    if (selectedIndex >= actionCount) selectedIndex = 0;
                }
                case "ENTER" -> {
                    return new ActionInput(actions[selectedIndex].toUpperCase().charAt(0));
                }
            }

            UIManager.clearScreen();
        }
    }
}

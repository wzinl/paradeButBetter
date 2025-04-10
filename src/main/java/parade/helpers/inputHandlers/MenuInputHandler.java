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

public class MenuInputHandler extends InputHandler {

    private static final KeyMap<String> keyMap = new KeyMap<>();
    static {
        keyMap.bind("UP", "w", "W", "\u001B[A");
        keyMap.bind("DOWN", "s", "S", "\u001B[B");
        keyMap.bind("LEFT", "a", "A", "\u001B[D");
        keyMap.bind("RIGHT", "d", "D", "\u001B[C");
        keyMap.bind("ENTER", "\r", "\n", "\r\n");
    }

    @Override
    public String toString() {
        return super.toString();
    }


    public MenuInputHandler(Terminal terminal) throws IOException{
        super(terminal);
    }
    @Override
    public void flush() {
        try {
            InputStream in = System.in;
            while (in.available() > 0) {
                in.read(); // discard input
            }

        } catch (IOException e) {
            System.out.println("Error flushing stdin: " + e.getMessage());
            // Ignore or log
        }
    }
    @Override
    public void startInput() throws IOException {
        flush();
        terminal.enterRawMode();
    }

    @Override
    public void stopInput() {
        flush();
    }


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

    public ActionInput introSelect(String[] actions) throws IOException {
        flush();
        String titleCard = DisplayFactory.getTitleCard();
        BindingReader bindingReader = new BindingReader(terminal.reader());
        int selectedIndex = 0;
        int actionCount = actions.length;
    
        UIManager.clearScreen();

        while (true) {
            UIManager.displayMessage(titleCard); // Print the title card
            // Display the menu with the current selection highlighted
            for (int i = 0; i < actionCount; i++) {
                if (i == selectedIndex) {
                    System.out.println(Ansi.ansi().bold().fg(Ansi.Color.RED).a("> " + actions[i]).reset());
                } else {
                    System.out.println("  " + actions[i]);
                }
            }
    
            // Read user input
            String key = bindingReader.readBinding(keyMap);
    
            if (key == null) {
                continue;
            }

            switch (key) {
                case "UP" -> {
                    selectedIndex--;
                    if (selectedIndex < 0) {
                        selectedIndex = actionCount - 1; // Wrap around to the last option
                    }
                }
                case "DOWN" -> {
                    selectedIndex++;
                    if (selectedIndex >= actionCount) {
                        selectedIndex = 0; // Wrap around to the first option
                    }
                }
                case "ENTER" -> {
                    return new ActionInput(actions[selectedIndex].toUpperCase().charAt(0)); // Return the selected action
                }
            }
            UIManager.clearScreen();

        }
    }
}

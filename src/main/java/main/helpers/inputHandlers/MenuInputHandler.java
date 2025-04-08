package main.helpers.inputHandlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.fusesource.jansi.Ansi;
import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Terminal;

import main.helpers.inputTypes.ActionInput;
import main.helpers.inputTypes.CardInput;
import main.helpers.inputTypes.SelectionInput;
import main.helpers.ui.DisplayFactory;
import main.helpers.ui.UIManager;
import main.models.ParadeBoard;
import main.models.cards.Card;
import main.models.player.Player;
import main.models.player.PlayerHand;

public class MenuInputHandler {
    private final Terminal terminal;
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
        this.terminal = terminal;
    }
    public void flushStdin() {
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

    public SelectionInput turnSelect(ParadeBoard paradeBoard, Player currentPlayer, Map<String, Character> actionMap) throws IOException {
        flushStdin();
        BindingReader bindingReader = new BindingReader(terminal.reader());
        int selectedIndex = 0;
        PlayerHand currHand = currentPlayer.getPlayerHand();
        List<Card> cardList = currHand.getCardList();
        int handSize = cardList.size();

        String[] actionKeys = actionMap.keySet().toArray(String[]::new);
        int actionKeyCount = actionKeys.length;

        boolean onCardRow = true;
        
        UIManager.printFormattedTurnDisplay(currentPlayer, paradeBoard, selectedIndex, actionKeys, onCardRow);
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
                    int max = onCardRow ? handSize : actionKeyCount;
                    if (selectedIndex >= max) {
                        selectedIndex = 0;
                    }
                }
                case "LEFT" -> {
                    selectedIndex--;
                    int max = onCardRow ? handSize : actionKeyCount;
                    if (selectedIndex < 0) {
                        selectedIndex = max - 1;
                    }
                }
                case "ENTER" -> {
                    if (onCardRow) {
                        return new CardInput(selectedIndex);
                    } else {
                        return new ActionInput(actionMap.get(actionKeys[selectedIndex]));
                    }
                }
            }

            UIManager.clearScreen();
            UIManager.printFormattedTurnDisplay(currentPlayer, paradeBoard, selectedIndex, actionKeys, onCardRow);
        }
    }

    public ActionInput introSelect(String[] actions) throws IOException {
        flushStdin();
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

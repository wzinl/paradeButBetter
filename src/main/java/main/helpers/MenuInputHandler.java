package main.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Terminal;

import main.helpers.ui.UIManager;
import main.models.ParadeBoard;
import main.models.cards.Card;
import main.models.input.ActionInput;
import main.models.input.CardInput;
import main.models.input.SelectionInput;
import main.models.player.Player;
import main.models.player.PlayerHand;

public class MenuInputHandler {
    private static final KeyMap<String> keyMap = new KeyMap<>();
    static {
        keyMap.bind("UP", "w", "W", "\u001B[A");
        keyMap.bind("DOWN", "s", "S", "\u001B[B");
        keyMap.bind("LEFT", "a", "A", "\u001B[D");
        keyMap.bind("RIGHT", "d", "D", "\u001B[C");
        keyMap.bind("ENTER", "\r", "\n", "\r\n");
    }

    public MenuInputHandler(Terminal terminal) throws IOException{
        // this.terminal = terminal;
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

    public SelectionInput turnSelect(Terminal terminal,ParadeBoard paradeBoard, Player currentPlayer, Map<String, Character> actionMap) throws IOException {
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
}

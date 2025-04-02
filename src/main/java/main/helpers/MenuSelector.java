package main.helpers;

import java.io.IOException;
import java.util.List;

import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import main.models.ParadeBoard;
import main.models.cards.Card;
import main.models.player.Player;
import main.models.player.PlayerHand;

public class MenuSelector {

    public static String turnSelect(ParadeBoard paradeBoard, Player currentPlayer, String[] actionOptions) throws IOException{
        int selectedIndex = 0;
        PlayerHand currHand = currentPlayer.getPlayerHand();
        List < Card > cardList = currHand.getCardList();
        int handSize = cardList.size();
        int actionOptionsCount = actionOptions.length;
        boolean onCardRow = true;

        Terminal terminal = TerminalBuilder.builder()
                .dumb(true)
                .build();
        terminal.enterRawMode(); // Raw mode = immediate input
        Attributes attr = terminal.getAttributes();

        attr.setLocalFlag(Attributes.LocalFlag.ECHO, false);
        terminal.setAttributes(attr);
        System.out.println(ScreenUtils.getTurnDisplay(currentPlayer, paradeBoard, selectedIndex, actionOptions, onCardRow));
        while (true) {
            // System.out.println("iterartion!");
            int ch = terminal.reader().read();
            if (ch == -1) {
                continue; // No input yet
            }
            switch (ch) {
                case 'W', 'w' -> { // UP
                    if (!onCardRow) {
                        onCardRow = true;
                        selectedIndex = 0;
                    }
                }
                case 'S', 's' -> { // DOWN
                    if (onCardRow) {
                        onCardRow = false;
                        selectedIndex = 0;
                    }
                }
                case 'D', 'd' -> { // RIGHT
                    selectedIndex++;
                    int max = onCardRow ? handSize : actionOptionsCount;
                    if (selectedIndex >= max) {
                        selectedIndex = 0;
                    }
                }
                case 'A', 'a' -> { // LEFT
                    selectedIndex--;
                    int max = onCardRow ? handSize : actionOptionsCount;
                    if (selectedIndex < 0) {
                        selectedIndex = max - 1;
                    }
                }
                case 10, 13 -> { // ENTER
                    if (onCardRow) {
                        return String.valueOf(selectedIndex);
                    } else {
                        return "action: " + actionOptions[selectedIndex];
                    }
                }
            }
            ScreenUtils.clearScreen();
            System.out.println();
            System.out.println(ScreenUtils.getTurnDisplay(currentPlayer, paradeBoard, selectedIndex, actionOptions, onCardRow));
    }
}
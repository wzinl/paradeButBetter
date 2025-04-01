    package main.helpers;

    // import java.io.Console;
    // import java.io.IOException;
    // import java.io.Reader;
    // import java.util.List;

    // import javax.naming.Binding;

    import main.models.ParadeBoard;
    import main.models.cards.Card;
    import main.models.player.Player;
    import main.models.player.PlayerHand;

    import org.jline.terminal.Terminal;
    import org.jline.terminal.TerminalBuilder;
    import org.jline.terminal.Terminal;
    import java.io.IOException;
    import java.util.List;
    import org.jline.terminal.Attributes;


    public class MenuSelector {

        public static String turnSelect(ParadeBoard paradeBoard, Player currentPlayer, List<String> actionOptions) {
            int selectedIndex = 0;
            PlayerHand currHand = currentPlayer.getPlayerHand();
            List<Card> cardList = currHand.getCardList();
            int handSize = cardList.size();
            int actionOptionsCount = actionOptions.size();
            boolean onCardRow = true;
        
            try {
                Terminal terminal = TerminalBuilder.builder()
                        .system(true)
                        .jna(true)
                        .build();
                terminal.enterRawMode(); // Raw mode = immediate input
                Attributes attr = terminal.getAttributes();
        
                attr.setLocalFlag(Attributes.LocalFlag.ECHO, false);
                terminal.setAttributes(attr);
                System.out.println("Terminal class: " + terminal.getClass().getName());

                    // ScreenUtils.clearScreen();
                    System.out.println(ScreenUtils.getTurnDisplay(currentPlayer, paradeBoard, selectedIndex, actionOptions, onCardRow));
                    while (true) {
                        // System.out.println("iterartion!");
                        int ch = terminal.reader().read();                    
                        if (ch == -1) {
                            continue; // No input yet
                        }
                    
                        System.out.println("Ch: " + (int)ch);

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
                            case 'A','a' -> { // LEFT
                                selectedIndex--;
                                int max = onCardRow ? handSize : actionOptionsCount;
                                if (selectedIndex < 0) {
                                    selectedIndex = max - 1;
                                }
                            }
                        }
                        if (ch == 10 || ch == 13) { // ENTER
                            if (onCardRow) {
                                return String.valueOf(selectedIndex);
                            } else {
                                return "action:" + actionOptions.get(selectedIndex);
                            }
                        }

                        // ScreenUtils.clearScreen();

                        System.out.println();
                        System.out.println(ScreenUtils.getTurnDisplay(currentPlayer, paradeBoard, selectedIndex, actionOptions, onCardRow));
                    }
                    

            } catch (IOException e) {
                throw new RuntimeException("Error reading input", e);
            }
        }
    }   

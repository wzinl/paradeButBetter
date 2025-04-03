package main.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import main.models.ParadeBoard;
import main.models.cards.Card;
import main.models.input.ActionInput;
import main.models.input.CardInput;
import main.models.input.SelectionInput;
import main.models.player.Player;
import main.models.player.PlayerHand;


public class InputHandler {

    protected final Terminal terminal;
    protected LineReader reader;
    private final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    private final AtomicBoolean running = new AtomicBoolean(true);
    private Thread inputThread;


    private static final int MAX_ATTEMPTS = 5;
    private static final int COOLDOWN_THRESHOLD = 3;
    private static final int COOLDOWN_MS = 2000;


    KeyMap<String> keyMap;

    public InputHandler() {
        try {
            this.terminal = TerminalBuilder.builder().system(true).build();
            startInputThread();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize terminal input", e);
        }
        keyMap = new KeyMap<>();
        keyMap.bind("UP", "w", "W", "\u001B[A");
        keyMap.bind("DOWN", "s", "S", "\u001B[B");
        keyMap.bind("LEFT", "a", "A", "\u001B[D");
        keyMap.bind("RIGHT", "d", "D", "\u001B[C");
        keyMap.bind("ENTER", "\r", "\n", "\r\n");
    }

    private void startInputThread() {
        reader = LineReaderBuilder.builder().terminal(terminal).build();
        inputThread = new Thread(() -> {
            while (running.get()) {
                try {
                    String line = reader.readLine().trim();
                    inputQueue.offer(line);
                } catch (UserInterruptException | EndOfFileException e) {
                    running.set(false); // exit gracefully on known exit events
                } catch (Exception e) {
                    running.set(false); // catch all
                }
            }
        });
    
        inputThread.setDaemon(true);
        inputThread.start();
    }

    public void stopInputThread() {
        running.set(false);   
        if (inputThread != null && inputThread.isAlive()) {
            inputThread.interrupt(); 
            try {
                inputThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        flushQueue(); 
    }
    

    private void flushQueue() {
        inputQueue.clear();
    }

    private String waitForInput(String prompt) {
        flushQueue();
        System.out.println(prompt);
        try {
            return inputQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Input interrupted");
        }
    }

    private void handleSpamDelay(int attempts) {
        if (attempts >= COOLDOWN_THRESHOLD) {
            System.out.println("Please wait a moment before trying again...\n");
            ScreenUtils.pause(COOLDOWN_MS);
        }
    }

    public int getInt(String prompt) {
        int attempts = 0;
        while (attempts < MAX_ATTEMPTS) {
            String input = waitForInput(prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer.\n");
                attempts++;
                handleSpamDelay(attempts);
            }
        }
        throw new RuntimeException("Too many invalid integer inputs.");
    }

    public int getIntInRange(String prompt, int min, int max) {
        int attempts = 0;
        while (attempts < MAX_ATTEMPTS) {
            int val = getInt(prompt);
            if (val >= min && val <= max) {
                return val;
            } else {
                System.out.printf("Value out of range. Please enter a number between %d and %d.\n\n", min, max);
                attempts++;
                handleSpamDelay(attempts);
            }
        }
        throw new RuntimeException("Too many out-of-range inputs.");
    }

    public SelectionInput getIntInRangeWithExceptions(String prompt, int min, int max, Map<String, Character> map) {
        int attempts = 0;
        Collection<Character> actionOptions = map.values();
        prompt = prompt.replaceAll(":$", ".");

        while (attempts < MAX_ATTEMPTS) {
            System.out.println(prompt);
            String input = waitForInput("> ").trim();

            if (input.length() == 1 && actionOptions.contains(input.charAt(0))) {
                return new ActionInput(input.charAt(0));
            }

            try {
                int val = Integer.parseInt(input);
                if (val >= min && val <= max) {
                    return new CardInput(val);
                } else {
                    System.out.printf("Value out of range. Please enter a number between %d and %d.\n\n", min, max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number or one of the allowed characters.\n");
            }

            attempts++;
            handleSpamDelay(attempts);
        }

        throw new RuntimeException("Too many invalid inputs.");
    }

    public String getString(String prompt) {
        int attempts = 0;
        while (attempts < MAX_ATTEMPTS) {
            String input = waitForInput(prompt);
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("Input cannot be empty.\n");
                attempts++;
                handleSpamDelay(attempts);
            }
        }
        throw new RuntimeException("Too many invalid string inputs.");
    }

    public boolean getYesNo(String prompt) {
        int attempts = 0;
        while (attempts < MAX_ATTEMPTS) {
            String input = waitForInput(prompt + " (Y/N):").toLowerCase();
            if (input.equals("y")) return true;
            if (input.equals("n")) return false;

            System.out.println("Invalid input. Please type 'Y' or 'N'.\n");
            attempts++;
            handleSpamDelay(attempts);
        }
        throw new RuntimeException("Too many invalid yes/no inputs.");
    }

    public String getDifficulty(String prompt) {
        while (true) {
            String input = waitForInput(prompt);
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Please enter a valid string.");
        }
    }


    public void shutdown() {
        running.set(false);
        try {
            terminal.close();
        } catch (IOException ignored) {}
    }

    private void flushStdin() {
        try {
            InputStream in = System.in;
            while (in.available() > 0) {
                in.read(); // discard input
            }

        } catch (IOException e) {
            // Ignore or log
        }
    }

    public SelectionInput turnSelect(ParadeBoard paradeBoard, Player currentPlayer, Map<String, Character> actionMap) throws IOException {
        int selectedIndex = 0;
        PlayerHand currHand = currentPlayer.getPlayerHand();
        List<Card> cardList = currHand.getCardList();
        int handSize = cardList.size();

        String[] actionOptions = actionMap.keySet().toArray(new String[0]);
        int actionOptionsCount = actionOptions.length;

        boolean onCardRow = true;
        stopInputThread();
        Attributes originalAttributes = terminal.getAttributes();
        terminal.enterRawMode();
        BindingReader bindingReader = new BindingReader(terminal.reader());
        flushStdin();

        System.out.println(ScreenUtils.getTurnDisplay(currentPlayer, paradeBoard, selectedIndex, actionOptions, onCardRow));
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
                    int max = onCardRow ? handSize : actionOptionsCount;
                    if (selectedIndex >= max) {
                        selectedIndex = 0;
                    }
                }
                case "LEFT" -> {
                    selectedIndex--;
                    int max = onCardRow ? handSize : actionOptionsCount;
                    if (selectedIndex < 0) {
                        selectedIndex = max - 1;
                    }
                }
                case "ENTER" -> {
                    terminal.setAttributes(originalAttributes);
                    startInputThread();
                    flushStdin();

                    if (onCardRow) {
                        return new CardInput(selectedIndex);
                    } else {
                        return new ActionInput(actionMap.get(actionOptions[selectedIndex]));
                    }
                }
            }

            ScreenUtils.clearScreen();
            System.out.println();
            System.out.println(ScreenUtils.getTurnDisplay(currentPlayer, paradeBoard, selectedIndex, actionOptions, onCardRow));
        }
    }



}

package main.helpers;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import main.models.selections.input.ActionInput;
import main.models.selections.input.CardInput;
import main.models.selections.input.SelectionInput;

public class InputValidator {

    private static Terminal terminal;
    private static LineReader reader;

    private static final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    private static final AtomicBoolean running = new AtomicBoolean(true);

    private static final int MAX_ATTEMPTS = 5;
    private static final int COOLDOWN_THRESHOLD = 3;
    private static final int COOLDOWN_MS = 2000;

    // Start background thread to read input
    static {
        try {
            terminal = TerminalBuilder.builder()
                    .system(true)
                    .build();

            reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build();

            Thread inputThread = new Thread(() -> {
                while (running.get()) {
                    try {
                        String line = reader.readLine().trim();
                        inputQueue.offer(line);
                    } catch (Exception e) {
                        // Likely terminal closed
                        running.set(false);
                        break;
                    }
                }
            });

            inputThread.setDaemon(true);
            inputThread.start();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize terminal input", e);
        }
    }

    private static void flushQueue() {
        inputQueue.clear();
    }

    private static String waitForInput(String prompt) {
        flushQueue();
        System.out.println(prompt);
        try {
            return inputQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Input interrupted");
        }
    }

    private static void handleSpamDelay(int attempts) {
        if (attempts >= COOLDOWN_THRESHOLD) {
            System.out.println("Please wait a moment before trying again...\n");
            ScreenUtils.pause(COOLDOWN_MS);
        }
    }

    public static int getInt(String prompt) {
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

    public static int getIntInRange(String prompt, int min, int max) {
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

    public static SelectionInput getIntInRangeWithExceptions(String prompt, int min, int max, Map<String, Character> map) {
        int attempts = 0;
       Collection<Character> actionOptions = map.values();

        prompt = prompt.substring(0, prompt.length() - 2) + ".";
        while (attempts < MAX_ATTEMPTS) {
            // Print prompt
            System.out.println(prompt);
            
            String input = waitForInput("> ").trim();
            
            // Check for exception character
            if (input.length() == 1) {
                char ch = input.charAt(0);
                for (char ex : actionOptions) {
                    if (ch == ex) {
                        return new ActionInput(ch); // Return the exception character
                    }
                }
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
    
    

    public static String getString(String prompt) {
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

    public static boolean getYesNo(String prompt) {
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

    public static String getDifficulty(String prompt) {
        while (true) {
            String input = waitForInput(prompt);
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Please enter a valid string.");
        }
    }

    public static void shutdown() {
        running.set(false);
        try {
            terminal.close();
        } catch (IOException ignored) {}
    }
}

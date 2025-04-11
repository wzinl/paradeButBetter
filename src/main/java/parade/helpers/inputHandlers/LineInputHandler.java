package parade.helpers.inputHandlers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.fusesource.jansi.Ansi;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;

import parade.helpers.inputTypes.ActionInput;
import parade.helpers.inputTypes.CardInput;
import parade.helpers.inputTypes.SelectionInput;
import parade.helpers.ui.UIManager;
import parade.models.ParadeBoard;
import parade.models.player.Player;

/**
 * Handles line-based input from the terminal using JLine.
 * Allows players to type input directly via keyboard during their turn.
 */
public class LineInputHandler extends InputHandler {

    /** JLine reader for handling line-based input. */
    protected LineReader reader;

    /** Queue to store incoming user input lines. */
    private final BlockingQueue<String> inputQueue;

    /** Flag indicating if the input thread should keep running. */
    private final AtomicBoolean running;

    /** The background thread used to capture input lines. */
    private Thread inputThread;

    /** Original terminal attributes to restore after input ends. */
    private final Attributes lineReaderAttributes;

    /**
     * Constructs a LineInputHandler using the provided terminal.
     *
     * @param terminal the terminal to read from
     * @throws IOException if reader or terminal setup fails
     */
    public LineInputHandler(Terminal terminal) throws IOException {
        super(terminal);
        this.inputQueue = new LinkedBlockingQueue<>();
        this.running = new AtomicBoolean(true);
        this.lineReaderAttributes = terminal.getAttributes();
    }

    /**
     * Starts the input capture process, spawning a background thread
     * that listens for keyboard input using JLine.
     */
    @Override
    public void startInput() {
        flush();
        terminal.setAttributes(lineReaderAttributes);
        running.set(true);
        this.reader = LineReaderBuilder.builder().terminal(terminal).build();

        inputThread = new Thread(() -> {
            while (running.get()) {
                try {
                    String line = this.reader.readLine().trim();
                    inputQueue.offer(line);
                } catch (UserInterruptException | EndOfFileException e) {
                    running.set(false);
                } catch (IllegalArgumentException | IllegalStateException e) {
                    System.err.println("Unexpected error in input thread: " + e.getMessage());
                    running.set(false);
                }
            }
        });

        inputThread.setDaemon(true);
        inputThread.start();
    }

    /**
     * Stops the background input thread and flushes the input buffer.
     */
    @Override
    public void stopInput() {
        running.set(false);
        if (inputThread != null && inputThread.isAlive()) {
            inputThread.interrupt();
            try {
                inputThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        flush();
    }

    /**
     * Displays turn UI and collects either a card selection or action input from the player.
     *
     * @param paradeBoard   the current board state
     * @param currentPlayer the player taking the turn
     * @param actionStrings the list of available action commands
     * @return a {@link SelectionInput} representing the player's choice
     */
    @Override
    public SelectionInput turnSelect(ParadeBoard paradeBoard, Player currentPlayer, String[] actionStrings) {
        int max = currentPlayer.getPlayerHand().getCardList().size();
        int min = 1;
        String prompt = "";

        Collection<Character> actionChars = Arrays.stream(actionStrings)
            .map(action -> action.charAt(0))
            .collect(Collectors.toSet());

        UIManager.printFormattedTurnDisplay(currentPlayer, paradeBoard, actionStrings);

        for (String actionString : actionStrings) {
            prompt += "Enter " +
                Ansi.ansi().bold().fg(Ansi.Color.GREEN).a(actionString.toUpperCase().charAt(0)).reset() +
                " to " + actionString + "\n";
        }

        prompt += Ansi.ansi().bold().fg(Ansi.Color.CYAN).a("Select a card (1-" + max + "): ").reset();
        prompt = prompt.replaceAll(":$", ".");
        System.out.println(prompt);

        String repeatPrompt = "Value out of range. Please enter a number between " + min + " and " + max + ".\n";

        while (true) {
            String input = waitForInput("").trim();

            if (input.length() == 1 && actionChars.contains(input.toUpperCase().charAt(0))) {
                return new ActionInput(input.toUpperCase().charAt(0));
            }

            try {
                int val = Integer.parseInt(input);
                if (val >= min && val <= max) {
                    return new CardInput(val - 1);
                } else {
                    System.out.println(repeatPrompt);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number or one of the allowed characters.\n");
            }
        }
    }

    /**
     * Clears the current input buffer.
     */
    @Override
    protected void flush() {
        inputQueue.clear();
    }

    /**
     * Blocks until input is received, then returns it.
     *
     * @param prompt optional prompt message to display
     * @return the user input string
     */
    private String waitForInput(String prompt) {
        flush();
        try {
            if (prompt != null && !prompt.equals("")) {
                System.out.println(prompt);
            }
            return inputQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Input interrupted");
        }
    }

    /**
     * Prompts the user for an integer until valid input is provided.
     *
     * @param prompt the prompt message
     * @return the integer entered
     */
    public int getInt(String prompt) {
        while (true) {
            String input = waitForInput(prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer.\n");
            }
        }
    }

    /**
     * Prompts the user for an integer within a specified range.
     *
     * @param prompt the prompt message
     * @param min    minimum value (inclusive)
     * @param max    maximum value (inclusive)
     * @return the validated integer
     */
    public int getIntInRange(String prompt, int min, int max) {
        String repeatPrompt = "Value out of range. Please enter a number between " + min + " and " + max + ".\n";
        while (true) {
            int val = getInt(prompt);
            if (val >= min && val <= max) {
                return val;
            } else {
                System.out.println(repeatPrompt);
            }
        }
    }

    /**
     * Prompts the user for a non-empty string input.
     *
     * @param prompt the prompt message
     * @return the input string
     */
    public String getString(String prompt) {
        while (true) {
            String input = waitForInput(prompt);
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("Input cannot be empty.\n");
            }
        }
    }
    /**
     * Waits for the user to press Enter, showing a blinking message in the meantime.
     * Stops the blinking once Enter is detected.
     */
    public void getEnter() {
        String prompt = "Press Enter to continue...";
        flush();

        Thread blinkingThread = new Thread(() -> {
            try {
                UIManager.blinkingEffect(
                    Ansi.ansi().bold().fg(Ansi.Color.RED).a(prompt).reset().toString());
            } catch (InterruptedException e) {
                System.out.println(
                    Ansi.ansi().bold().fg(Ansi.Color.RED).a(prompt).reset().toString());
            }
        });

        blinkingThread.start();

        try {
            inputQueue.take(); // Wait for Enter
            blinkingThread.interrupt(); // Stop blinking
            blinkingThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Input interrupted");
        }
    }
}

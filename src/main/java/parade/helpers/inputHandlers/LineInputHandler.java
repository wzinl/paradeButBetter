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
import org.jline.terminal.Terminal;

import parade.helpers.inputTypes.ActionInput;
import parade.helpers.inputTypes.CardInput;
import parade.helpers.inputTypes.SelectionInput;
import parade.helpers.ui.UIManager;
import parade.models.ParadeBoard;
import parade.models.player.Player;


public class LineInputHandler {


    protected final Terminal terminal;
    protected LineReader reader;
    private final BlockingQueue<String> inputQueue;
    private final AtomicBoolean running;
    private Thread inputThread;

    public LineInputHandler(Terminal terminal) throws IOException{
        this.terminal = terminal;
        this.inputQueue = new LinkedBlockingQueue<>();
        this.running = new AtomicBoolean(true);
    }
    protected void resume() {
        running.set(true);
    }
    protected  void startInputThread() {
        this.reader = LineReaderBuilder.builder().terminal(terminal).build();
        flushQueue();
        inputThread = new Thread(() -> {
            while (running.get()) {
                try {
                    String line = this.reader.readLine().trim();
                    inputQueue.offer(line);
                } catch (UserInterruptException | EndOfFileException e) {
                    running.set(false); // exit gracefully on known exit events
                } catch (Exception e) {
                    System.err.println("Unexpected error in input thread: " + e.getMessage());
                    running.set(false); // catch all
                }
            }
        });
        inputThread.setDaemon(true);
        inputThread.start();
    }

    protected void stopInputThread() {
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
        try {
            System.out.println(prompt);
            System.out.print("\n>");
            System.out.print(" ");
            return inputQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Input interrupted");
        }
    }
    private void waitforEnter() {
        String prompt = "Press Enter to continue...";
        flushQueue();
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
            inputQueue.take(); // Wait for Enter key press
            blinkingThread.interrupt(); // Stop the blinking effect
            blinkingThread.join(); // Ensure blinking thread finishes
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupt status
            throw new RuntimeException("Input interrupted");
        }
    }

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
    
    public SelectionInput turnSelect(ParadeBoard paradeBoard, Player currentPlayer, String[] actionStrings) {
        int max = currentPlayer.getPlayerHand().getCardList().size();
        int min = 1;
        String prompt = "";
        Collection<Character> actionChars = Arrays.stream(actionStrings)
                .map(action -> action.charAt(0))
                .collect(Collectors.toSet());
        UIManager.printFormattedTurnDisplay(currentPlayer, paradeBoard, actionStrings);

        for (String actionString : actionStrings) {
            prompt += "Enter " 
                    + Ansi.ansi().bold().fg(Ansi.Color.GREEN)
                      .a(actionString.toUpperCase().charAt(0)).reset()
                    + " to " + actionString + "\n";        
        }
        prompt += Ansi.ansi().bold().fg(Ansi.Color.CYAN).a("Select a card (1-" + max + "): ").reset();

        
        prompt = prompt.replaceAll(":$", ".");
        System.out.println(prompt);
        String repeatPrompt = "Value out of range. Please enter a number between " + min + " and " + max + ".\n";
        while (true) {
            String input = waitForInput(repeatPrompt).trim();
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

    public void getEnter() {
        waitforEnter();
    }

    
    public boolean getYesNo(String prompt) {
        while (true) {
            String input = waitForInput(prompt + " (Y/N):").toUpperCase();
            if (input.equals("Y")) return true;
            if (input.equals("N")) return false;
    
            System.out.println("Invalid input. Please type 'Y' or 'N'.\n");
        }
    }

}

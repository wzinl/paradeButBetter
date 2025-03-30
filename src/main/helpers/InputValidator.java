package main.helpers;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class InputValidator {

    private static final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final AtomicBoolean running = new AtomicBoolean(true);

    private static final int MAX_ATTEMPTS = 5;
    private static final int COOLDOWN_THRESHOLD = 3;
    private static final long COOLDOWN_MS = 2000;

    // Start background thread to read input
    static {
        Thread inputThread = new Thread(() -> {
            while (running.get()) {
                String line = scanner.nextLine().trim();
                inputQueue.offer(line);
            }
        });
        inputThread.setDaemon(true); // Automatically dies with main program
        inputThread.start();
    }

    // Flushes all input lines that were spammed or queued before prompt.
    private static void flushQueue() {
        // confirmBeforeFlush();
        inputQueue.clear();
    }

    private static void confirmBeforeFlush() {
        System.out.println("Press Enter to continue...");
        try {
            System.in.read(); // waits for user to press Enter key
        } catch (IOException e) {
            System.out.println("Something went wrong while waiting for input.");
        }
    }

    // Waits for next clean line of input from user.
    private static String waitForInput(String prompt) {
        flushQueue();
        System.out.println(prompt);
        try {
            return inputQueue.take(); // waits until input is available
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Input interrupted");
        }
    }

    private static void handleSpamDelay(int attempts) {
        if (attempts >= COOLDOWN_THRESHOLD) {
            System.out.println("Please wait a moment before trying again...\n");
            try {
                Thread.sleep(COOLDOWN_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
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
                System.out.println("Value out of range. Please enter a number between " + min + " and " + max + ".\n");
                attempts++;
                handleSpamDelay(attempts);
            }
        }
        throw new RuntimeException("Too many out-of-range inputs.");
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
            String input = waitForInput(prompt + " (y/n):").toLowerCase();
            if (input.equals("y"))
                return true;
            if (input.equals("n"))
                return false;

            System.out.println("Invalid input. Please type 'y' or 'n'.\n");
            attempts++;
            handleSpamDelay(attempts);
        }
        throw new RuntimeException("Too many invalid yes/no inputs.");
    }

    public static String getDifficulty(String prompt) {
        while (true) {
            System.out.println(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Please enter a valid string.");
        }
    }

    /**
     * Closes the scanner instance.
     * Should only be called once at the very end of the program.
     */
    public static void closeScanner() {
        scanner.close();
    }
}

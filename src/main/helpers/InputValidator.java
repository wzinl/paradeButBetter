package main.helpers;

import java.util.Scanner;

public class InputValidator {

    // Create a single Scanner instance to be used throughout the class
    private static final Scanner scanner = new Scanner(System.in);

    /*
     * Prompts the user for an integer and validates the input.
     * Keeps prompting until a valid integer is entered.
     */
    public static int getInt(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                System.out.println();
            } 
        }
    }

    /**
     * Prompts the user for an integer within a specific range.
     * Keeps prompting until a valid integer within the range is entered.
     */
    public static int getIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = getInt(prompt); 
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Input out of range. Please enter a number between " + min + " and " + max + ".");
            System.out.println();
        }
    }

    /**
     * Prompts the user for a non-empty string.
     * Keeps prompting until a non-empty input is entered.
     */
    public static String getString(String prompt) {
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
     * Prompts the user to enter a yes or no response (y/n).
     * Keeps prompting until the user enters 'y' or 'n'.
     */
    public static boolean getYesNo(String prompt) {
        while (true) {
            System.out.println(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y")) {
                return true;
            } 
            if (input.equals("n")) {
                return false;
            }             
            System.out.println("Invalid input. Please enter 'y' for Yes or 'n' for No.");
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

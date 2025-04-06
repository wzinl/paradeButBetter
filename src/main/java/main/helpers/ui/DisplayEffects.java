package main.helpers.ui;


public class DisplayEffects {

// Text Colouring
public static final String ANSI_RESET = "\u001B[0m";
public static final String ANSI_RED = "\u001B[31m";
public static final String ANSI_GREEN = "\u001B[32m";
public static final String ANSI_YELLOW = "\u001B[33m";
public static final String ANSI_BLUE = "\u001B[34m";
public static final String ANSI_MAGENTA = "\u001B[35m"; // Magenta
public static final String ANSI_CYAN = "\u001B[36m";
public static final String ANSI_PURPLE = "\u001B[95m"; // Distinct purple
public static final String ANSI_BRIGHT_WHITE = "\u001B[97m";

// Background Colouring
public static final String MAGENTA_BG = "\u001B[45m";
public static final String RED_BG = "\u001B[41m";
public static final String BLUE_BG = "\u001B[44m";
public static final String LBLUE_BG = "\u001B[104m"; 
public static final String YELLOW_BG = "\u001B[43m";
public static final String GREEN_BG = "\u001B[42m";
public static final String PURPLE_BG = "\u001B[105m";

// Text Effects
public static final String BOLD = "\u001B[1m";
public static final String ANSI_UNDERLINE = "\u001B[4m";
public static final int BLINK_COUNT = 20;


    public static void typeWriter(String text, int delay) throws InterruptedException {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            Thread.sleep(delay);
        }
        System.out.println();
    }

    public static void blinkingEffect(String text) throws InterruptedException {
        for (int i = 0; i < BLINK_COUNT; i++) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException("Blinking effect interrupted");
            }
            System.out.print("\r" + text);
            Thread.sleep(300);
            System.out.print("\r" + " ".repeat(text.length()));
            Thread.sleep(300);
        }
        System.out.println("\r" + text);
    }

}

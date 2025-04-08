package main.helpers.ui;
import org.fusesource.jansi.Ansi;


public class DisplayEffects {


// Text Colouring
public static final String ANSI_RESET = Ansi.ansi().reset().toString();
public static final String ANSI_RED = Ansi.ansi().fg(Ansi.Color.RED).toString();
public static final String ANSI_GREEN = Ansi.ansi().fg(Ansi.Color.GREEN).toString();
public static final String ANSI_YELLOW = Ansi.ansi().fg(Ansi.Color.YELLOW).toString();
public static final String ANSI_BLUE = Ansi.ansi().fg(Ansi.Color.BLUE).toString();
public static final String ANSI_MAGENTA = Ansi.ansi().fg(Ansi.Color.MAGENTA).toString();
public static final String ANSI_CYAN = Ansi.ansi().fg(Ansi.Color.CYAN).toString();
public static final String ANSI_PURPLE = Ansi.ansi().fgBright(Ansi.Color.MAGENTA).toString();
public static final String ANSI_BRIGHT_WHITE = Ansi.ansi().fgBright(Ansi.Color.WHITE).toString();

// Background Colouring
public static final String MAGENTA_BG = Ansi.ansi().bg(Ansi.Color.MAGENTA).toString();
public static final String RED_BG = Ansi.ansi().bg(Ansi.Color.RED).toString();
public static final String BLUE_BG = Ansi.ansi().bg(Ansi.Color.BLUE).toString();
public static final String LBLUE_BG = Ansi.ansi().bgBright(Ansi.Color.BLUE).toString();
public static final String YELLOW_BG = Ansi.ansi().bg(Ansi.Color.YELLOW).toString();
public static final String GREEN_BG = Ansi.ansi().bg(Ansi.Color.GREEN).toString();
public static final String PURPLE_BG = Ansi.ansi().bgBright(Ansi.Color.MAGENTA).toString();

// Text Effects
public static final String BOLD = Ansi.ansi().bold().toString();
public static final String ANSI_UNDERLINE = "\u001B[4m"; // ANSI escape code for underline

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

package main;
import main.gameStates.*;

public class Game {
    private GameStateManager gsm;

    public Game() {
        System.out.println("Welcome to Parade.");
        gsm = new GameStateManager();
        gsm.setState(new InitState(gsm)); // Start with Main Game State
        
    }

    public void run() {
        
    }

    public static void main(String[] args) {
        new Game().run();
    }
}

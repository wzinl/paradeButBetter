package main;
import main.gameStates.*;

public class Game {
    private GameStateManager gsm;

    public Game() {
        gsm = new GameStateManager();
        gsm.changeState(new MainGameState(gsm)); // Start with Main Game State
    }

    public void run() {
        
    }

    public static void main(String[] args) {
        new Game().run();
    }
}

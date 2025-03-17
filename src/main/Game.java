package main;
import main.gameStates.*;

public class Game {
    private GameStateManager gsm;

    public Game() {
        System.out.println("Welcome to Parade.");

        //creating new game state manager, 
        gsm = new GameStateManager(); 
        
        
    }

    public void run() {
        //
        // InitState init = new InitState(gsm);
        gsm.setState(new InitState(gsm)); 
        gsm.nextState();
    }

    public static void main(String[] args) {
        new Game().run();
    }
}

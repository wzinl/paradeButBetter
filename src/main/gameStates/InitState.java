package main.gameStates;

import java.util.ArrayList;
import main.context.GameContext;
import main.helpers.InputValidator;

public class InitState implements GameState{
    private GameStateManager gsm;
    private GameContext context;

    public InitState(GameStateManager gsm) {
        System.out.println("Game initialized");
        this.gsm = gsm;
    }

    @Override
    public void enter(){
        System.out.println("Game setup will now take place.");
    
        // Get valid number of players
        int numPlayers = InputValidator.getIntInRange("Enter number of players: ", 1, 6);

        ArrayList<String> nameList = new ArrayList<>();
        for (int i = 1; i <= numPlayers; i++) {
            String playerName = InputValidator.getString("Enter name of Player " + i + ": ");
            nameList.add(playerName);
        }
        createGameContext(nameList);
    }

    public void createGameContext(ArrayList<String> nameList) {
        this.context = new GameContext(nameList, this);
    }

    @Override
    public void exit() {
        System.out.println("Exiting " + this.getClass().getSimpleName());
    }

}
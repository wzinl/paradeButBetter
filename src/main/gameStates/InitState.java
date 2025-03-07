package main.gameStates;

import java.util.ArrayList;
import java.util.Scanner;
import java.error.*;
import java.time.chrono.ThaiBuddhistChronology;
import java.time.chrono.ThaiBuddhistEra;
import main.context.GameContext;

public class InitState implements GameState{
    private GameStateManager gsm;
    private GameContext context;

    public InitState(GameStateManager gsm, GameContext context) {
        System.out.println("Game initialized");
        this.gsm = gsm;
        this.context = context;
    }



    public void enter() throws BadInput{
        try(Scanner scannerObj = new Scanner(System.in)){
            System.out.println("Welcome to Parade.");
            System.out.print("Enter number of players: ");

            String numPlayersStr = scannerObj.nextLine(); 
            ArrayList<String> nameList = new ArrayList<>();
            int numPlayers = Integer.parseInt(numPlayersStr);
            for (int i = 1; i <= numPlayers; i++){
                System.out.printf("Enter name of Player %d:", i);
                String playerName = scannerObj.nextLine();
                nameList.add(playerName);
                
                // havent create player objects
            }
        }
    }
    public void exit() {
        System.out.println("Exiting " + this.getClass().getSimpleName());
    }

}
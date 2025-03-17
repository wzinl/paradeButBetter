package main.gameStates;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import main.context.GameContext;
import main.helpers.InputValidator;
import main.models.*;

public class InitState implements GameState{
    private GameStateManager gsm;

    private int startingIndex;
    private ArrayList<Player> playerList;
    private Deck deck;
    private ParadeBoard paradeBoard;

    public InitState(GameStateManager gsm) {
        System.out.println("Game initialized");
        this.gsm = gsm;
        this.deck = new Deck();
        this.paradeBoard = new ParadeBoard(deck);
    }

    @Override
    public void enter(){
        System.out.println("Game setup will now take place.");
    
        // Get valid number of players
        int numPlayers = InputValidator.getIntInRange("Enter number of players: ", 1, 6);

        ArrayList<Player> playerList = new ArrayList<>();
        for (int i = 1; i <= numPlayers; i++) {
            String playerName = InputValidator.getString("Enter name of Player " + i + ": ");
            Player thisPlayer = new Player(playerName);
            thisPlayer.getPlayerHand().initHand(deck);
            playerList.add(thisPlayer);
            //TODO let the players draw the cards..
            // STORE IN GAME CONTEXT
        }
    
        this.startingIndex = ThreadLocalRandom.current().nextInt(0, playerList.size());
        this.playerList = playerList;


        //call exit and enter turn state
        // exit();
    }

    public GameContext createGameContext() {
        return new GameContext(playerList, this, startingIndex, deck, paradeBoard);
    }

    @Override
    public void exit() {
        //exiting InitState
        System.out.println("Exiting " + this.getClass().getSimpleName());
    }

}
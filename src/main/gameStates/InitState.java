package main.gameStates;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import main.context.GameContext;
import main.helpers.InputValidator;
import main.models.*;
import main.models.bots.RandomBot;
import main.models.bots.SmartBot;
import main.models.cards.Deck;
import main.models.player.Player;

public class InitState extends GameState {
    private int startingIndex;

    public InitState(GameStateManager gsm) {
        super(gsm);
        System.out.println("Game initialized");
        // Initialising Deck and Parade Board
        this.deck = new Deck();
        this.paradeBoard = new ParadeBoard(deck);
    }

    @Override
    public void enter() {
        System.out.println("Game setup will now take place.");
        System.out.println();

        System.out.println("\033c");
        // Get valid number of players
        int numPlayers = InputValidator.getIntInRange("Enter number of players: ", 1, 6);
        int numBots = 0;
        int difficulty = 0;
        if (numPlayers != 6 && numPlayers!= 1) {
            numBots = InputValidator.getIntInRange("Enter number of bots: ", 0, 6 - numPlayers);
            difficulty = InputValidator.getIntInRange("Choose bot level (1 or 2)", 1, 2);
        } else if(numPlayers == 1){
            numBots = InputValidator.getIntInRange("Enter number of bots: ", 1, 5);
            difficulty = InputValidator.getIntInRange("Choose bot level (1 or 2)", 1, 2);
        }
        
        /*
         * Prompting user for players names, and adding them into an ArrayList
         * - Additional logic for bots will probably be here
         */
        ArrayList<Player> playerList = new ArrayList<>();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\033c");

        for (int i = 1; i <= numPlayers; i++) {
            // String playerName = InputValidator.getString("Enter name of Player " + i + ": ");
            String playerName = InputValidator.getString("Enter name of Player " + i + ": ");
            Player thisPlayer = new Player(playerName);
            thisPlayer.getPlayerHand().initHand(deck);
            playerList.add(thisPlayer);
            System.out.println("\033c");

        }

        if (numBots != 0) {
            if(difficulty == 1){
                for (int i = 1; i <= numBots; i++) {
                    String BotName = InputValidator.getString("Enter name of Bot " + i + ": ");
                    RandomBot thisPlayer = new RandomBot(BotName);
                    thisPlayer.getPlayerHand().initHand(deck);
                    playerList.add(thisPlayer);
                    System.out.println("\033c");
                }
            } else{
                for (int i = 1; i <= numBots; i++) {
                    String BotName = InputValidator.getString("Enter name of Bot " + i + ": ");
                    SmartBot thisPlayer = new SmartBot(BotName);
                    thisPlayer.getPlayerHand().initHand(deck);
                    playerList.add(thisPlayer);
                    System.out.println("\033c");
                }  
            }
        }

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\033c");

        // Completely randomise the order of the players
        this.startingIndex = ThreadLocalRandom.current().nextInt(0, playerList.size());

        // Update playerList variable

        GameContext context = createGameContext();
        setContext(context);

        // Set the context in the current state
        this.context = context;
        this.playerList = playerList;
    }

    public GameContext createGameContext() {
        /*
         * called from setState in GameStateManager, populates the GameContext
         * Game context's purpose is to store all the necessary info
         * relevant to the game for easy retrieval and access from different
         * players during their turns.
         */
        return new GameContext(playerList, this, startingIndex, deck, paradeBoard);
    }

    @Override
    public void exit() {
        // exiting InitState
        System.out.println("Exiting " + this.getClass().getSimpleName());
    }

}
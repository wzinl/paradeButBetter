package main.gameStates;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import main.context.GameContext;
import main.helpers.GameDisplay;
import main.helpers.InputHandler;
import main.models.ParadeBoard;
import main.models.cards.Deck;
import main.models.player.Player;                       
import main.models.player.bots.RandomBot;
import main.models.player.bots.SmartBot;
import main.models.player.bots.SmarterBot;
import main.helpers.GameDisplay;

public class InitState extends GameState {
    private int startingIndex;

    public InitState(GameStateManager gsm, InputHandler InputHandler) {
        super(gsm, InputHandler);
        System.out.println("Game initialized");
        // Initialising Deck and Parade Board
        this.deck = new Deck();
        this.paradeBoard = new ParadeBoard(deck);
    }

    @Override
    public void enter() {
        // System.out.println("Game setup will now take place.");
        // System.out.println();
        GameDisplay.showIntroduction();


        System.out.println("\033c");
        // Get valid number of players
        int numPlayers = inputHandler.getIntInRange("Enter number of players: ", 1, 6);
        int numBots = 0;
        int difficulty = 0;
        if (numPlayers != 6 && numPlayers!= 1) {
            numBots = inputHandler.getIntInRange("Enter number of bots: ", 0, 6 - numPlayers);
            if(numBots!=0){
                difficulty = inputHandler.getIntInRange("Choose bot level (1-3)", 1, 3);
            }
        } else if(numPlayers == 1){
            numBots = inputHandler.getIntInRange("Enter number of bots: ", 1, 5);
            difficulty = inputHandler.getIntInRange("Choose bot level (1-3)", 1, 3);
        }
        
        /*
         * Prompting user for players names, and adding them into an ArrayList
         */
        ArrayList<Player> playerList = new ArrayList<>();

        System.out.println("\033c");

        for (int i = 1; i <= numPlayers; i++) {
            // String playerName = InputValidator.getString("Enter name of Player " + i + ": ");
            String playerName = inputHandler.getString("Enter name of Player " + i + ": ");
            Player thisPlayer = new Player(playerName);
            thisPlayer.getPlayerHand().initHand(deck);
            playerList.add(thisPlayer);
            System.out.println("\033c");

        }

        if (numBots != 0) {
            if(difficulty == 1){
                for (int i = 1; i <= numBots; i++) {
                    String BotName = inputHandler.getString("Enter name of Bot " + i + ": ");
                    RandomBot thisPlayer = new RandomBot(BotName);
                    thisPlayer.getPlayerHand().initHand(deck);
                    playerList.add(thisPlayer);
                    System.out.println("\033c");
                }
            } else if (difficulty == 2){
                for (int i = 1; i <= numBots; i++) {
                    String BotName = inputHandler.getString("Enter name of Bot " + i + ": ");
                    SmartBot thisPlayer = new SmartBot(BotName);
                    thisPlayer.getPlayerHand().initHand(deck);
                    playerList.add(thisPlayer);
                    System.out.println("\033c");
                }  
            } else{
                for (int i = 1; i <= numBots; i++) {
                    String BotName = inputHandler.getString("Enter name of Bot " + i + ": ");
                    SmarterBot thisPlayer = new SmarterBot(BotName);
                    thisPlayer.getPlayerHand().initHand(deck);
                    playerList.add(thisPlayer);
                    System.out.println("\033c");
                }  
            }
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
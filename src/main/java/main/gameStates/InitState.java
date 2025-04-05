package main.gameStates;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import main.context.GameContext;
import main.helpers.InputHandler;
import main.java.main.helpers.ui.UIManager;
import main.models.ParadeBoard;
import main.models.cards.Deck;
import main.models.player.Player;
import main.models.player.bots.RandomBot;
import main.models.player.bots.SmartBot;
import main.models.player.bots.SmarterBot;

public class InitState extends GameState {
    private int startingIndex;

    public InitState(GameStateManager gsm, InputHandler inputHandler) {
        super(gsm, inputHandler);
        UIManager.displayGameInitMessage();
        this.deck = new Deck();
        this.paradeBoard = new ParadeBoard(deck);
    }

    @Override
    public void enter() {
        UIManager.displayIntroduction(); 

        UIManager.clearScreen();
        int numPlayers = inputHandler.getIntInRange("Enter number of players: ", 1, 6);
        int numBots = 0;
        int difficulty = 0;

        if (numPlayers != 6 && numPlayers != 1) {
            numBots = inputHandler.getIntInRange("Enter number of bots: ", 0, 6 - numPlayers);
            if (numBots != 0) {
                difficulty = inputHandler.getIntInRange("Choose bot level (1-3): ", 1, 3);
            }
        } else if (numPlayers == 1) {
            numBots = inputHandler.getIntInRange("Enter number of bots: ", 1, 5);
            difficulty = inputHandler.getIntInRange("Choose bot level (1-3): ", 1, 3);
        }

        ArrayList<Player> playerList = new ArrayList<>();
        UIManager.clearScreen();

        for (int i = 1; i <= numPlayers; i++) {
            String playerName = inputHandler.getString("Enter name of Player " + i + ": ");
            Player player = new Player(playerName);
            player.getPlayerHand().initHand(deck);
            playerList.add(player);
            UIManager.clearScreen();
        }

        if (numBots != 0) {
            for (int i = 1; i <= numBots; i++) {
                String botName = inputHandler.getString("Enter name of Bot " + i + ": ");
                Player bot;

                switch (difficulty) {
                    case 1:
                        bot = new RandomBot(botName);
                        break;
                    case 2:
                        bot = new SmartBot(botName);
                        break;
                    case 3:
                    default:
                        bot = new SmarterBot(botName);
                        break;
                }

                bot.getPlayerHand().initHand(deck);
                playerList.add(bot);
                UIManager.clearScreen();
            }
        }

        UIManager.clearScreen();

        this.startingIndex = ThreadLocalRandom.current().nextInt(0, playerList.size());

        GameContext context = createGameContext();
        setContext(context);
        this.context = context;
        this.playerList = playerList;
    }

    public GameContext createGameContext() {
        return new GameContext(playerList, this, startingIndex, deck, paradeBoard);
    }

    @Override
    public void exit() {
        UIManager.displayStateExitMessage(this.getClass().getSimpleName());
    }
}

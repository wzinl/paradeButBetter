package main.gameStates;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

import main.context.GameContext;
import main.helpers.InputHandler;
import main.helpers.ui.UIManager;
import main.helpers.ui.DisplayEffects;
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

        UIManager.clearScreen();
        int numPlayers = inputHandler.getIntInRange(DisplayEffects.BOLD+DisplayEffects.ANSI_CYAN+"🎮 Enter number of players: "+DisplayEffects.ANSI_RESET, 1, 6);
        System.out.println();
        int numBots = 0;
        int difficulty = 0;

        if (numPlayers != 6 && numPlayers != 1) {
            numBots = inputHandler.getIntInRange(DisplayEffects.BOLD+DisplayEffects.ANSI_PURPLE+"🤖 Enter number of bots: "+DisplayEffects.ANSI_RESET, 0, 6 - numPlayers);
            System.out.println();
            if (numBots != 0) {
                difficulty = inputHandler.getIntInRange(DisplayEffects.BOLD+DisplayEffects.ANSI_RED+"Choose bot level (1-3): "+DisplayEffects.ANSI_RESET, 1, 3);
                System.out.println();
            }
        } else if (numPlayers == 1) {
            numBots = inputHandler.getIntInRange(DisplayEffects.BOLD+DisplayEffects.ANSI_PURPLE+"🤖 Enter number of bots: "+DisplayEffects.ANSI_RESET, 1, 5);
            System.out.println();
            difficulty = inputHandler.getIntInRange(DisplayEffects.BOLD+DisplayEffects.ANSI_RED+"Choose bot level (1-3): "+DisplayEffects.ANSI_RESET, 1, 3);
            System.out.println();
        }

        ArrayList<Player> playerList = new ArrayList<>();
        HashSet<String> playerNames = new HashSet<>();
        UIManager.clearScreen();

        for (int i = 1; i <= numPlayers; i++) {
            String playerName = inputHandler.getString(DisplayEffects.BOLD+DisplayEffects.ANSI_GREEN+"🤓 Enter name of Player " + i + ": "+DisplayEffects.ANSI_RESET);
            while (playerNames.contains(playerName)) {
                System.out.println();
                System.out.println(DisplayEffects.BOLD+"Player name taken. Please choose another name."+DisplayEffects.ANSI_RESET);
                playerName = inputHandler.getString(DisplayEffects.BOLD+DisplayEffects.ANSI_GREEN+"🤓 Enter name of Player " + i + ": "+DisplayEffects.ANSI_RESET);
            }
            playerNames.add(playerName);
            Player player = new Player(playerName);
            player.getPlayerHand().initHand(deck);
            playerList.add(player);
            UIManager.clearScreen();
        }

        if (numBots != 0) {
            for (int i = 1; i <= numBots; i++) {
                String botName = inputHandler.getString(DisplayEffects.BOLD+DisplayEffects.ANSI_GREEN+"👾 Enter name of Bot " + i + ": "+DisplayEffects.ANSI_RESET);
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
                while (playerNames.contains(botName)) {
                    System.out.println();
                    System.out.println(DisplayEffects.BOLD+"Bot name taken. Please choose another name."+DisplayEffects.ANSI_RESET);
                    botName = inputHandler.getString(DisplayEffects.BOLD+DisplayEffects.ANSI_GREEN+"👾 Enter name of Bot " + i + ": "+DisplayEffects.ANSI_RESET);
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

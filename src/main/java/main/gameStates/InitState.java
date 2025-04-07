package main.gameStates;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

import main.context.GameContext;
import main.helpers.inputHandlers.InputManager;
import main.helpers.ui.DisplayEffects;
import main.helpers.ui.UIManager;
import main.models.ParadeBoard;
import main.models.cards.Deck;
import main.models.player.Player;
import main.models.player.bots.RandomBot;
import main.models.player.bots.SmartBot;
import main.models.player.bots.SmarterBot;

public class InitState extends GameState {
    private int startingIndex;

    public InitState(GameStateManager gsm, InputManager inputManager) {
        super(gsm, inputManager);
        this.deck = new Deck();
        this.paradeBoard = new ParadeBoard(deck);
    }

    @Override
    public void enter() {

        UIManager.clearScreen();
        int numPlayers = inputManager.getIntInRange(DisplayEffects.BOLD+DisplayEffects.ANSI_CYAN+"🎮 Enter number of players: "+DisplayEffects.ANSI_RESET, 1, 6);
        System.out.println();
        int numBots = 0;
        int difficulty = 0;

        if (numPlayers != 6 && numPlayers != 1) {
            numBots = inputManager.getIntInRange(DisplayEffects.BOLD+DisplayEffects.ANSI_PURPLE+"🤖 Enter number of bots: "+DisplayEffects.ANSI_RESET, 0, 6 - numPlayers);
            System.out.println();
            if (numBots != 0) {
                difficulty = inputManager.getIntInRange(DisplayEffects.BOLD+DisplayEffects.ANSI_RED+"Choose bot level (1-3): "+DisplayEffects.ANSI_RESET, 1, 3);
                System.out.println();
            }
        } else if (numPlayers == 1) {
            numBots = inputManager.getIntInRange(DisplayEffects.BOLD+DisplayEffects.ANSI_PURPLE+"🤖 Enter number of bots: "+DisplayEffects.ANSI_RESET, 1, 5);
            System.out.println();
            difficulty = inputManager.getIntInRange(DisplayEffects.BOLD+DisplayEffects.ANSI_RED+"Choose bot level (1-3): "+DisplayEffects.ANSI_RESET, 1, 3);
            System.out.println();
        }

        ArrayList<Player> createdPlayerList = new ArrayList<>();
        HashSet<String> playerNames = new HashSet<>();
        UIManager.clearScreen();

        for (int i = 1; i <= numPlayers; i++) {
            String playerName = inputManager.getString(DisplayEffects.BOLD+DisplayEffects.ANSI_GREEN+"🤓 Enter name of Player " + i + ": "+DisplayEffects.ANSI_RESET);
            while (playerNames.contains(playerName)) {
                System.out.println();
                System.out.println(DisplayEffects.BOLD+"Name taken. Please choose another name."+DisplayEffects.ANSI_RESET);
                playerName = inputManager.getString(DisplayEffects.BOLD+DisplayEffects.ANSI_GREEN+"🤓 Enter name of Player " + i + ": "+DisplayEffects.ANSI_RESET);
            }
            playerNames.add(playerName);
            Player player = new Player(playerName);
            player.getPlayerHand().initHand(deck);
            createdPlayerList.add(player);
            UIManager.clearScreen();
        }

        if (numBots != 0) {
            for (int i = 1; i <= numBots; i++) {
                String botName = inputManager.getString(DisplayEffects.BOLD+DisplayEffects.ANSI_GREEN+"👾 Enter name of Bot " + i + ": "+DisplayEffects.ANSI_RESET);
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
                bot.setPreferMenu(false);
                while (playerNames.contains(botName)) {
                    System.out.println();
                    System.out.println(DisplayEffects.BOLD+"Name taken. Please choose another name."+DisplayEffects.ANSI_RESET);
                    botName = inputManager.getString(DisplayEffects.BOLD+DisplayEffects.ANSI_GREEN+"👾 Enter name of Bot " + i + ": "+DisplayEffects.ANSI_RESET);
                }
                playerNames.add(botName);
                bot.getPlayerHand().initHand(deck);
                createdPlayerList.add(bot);
                UIManager.clearScreen();
            }
        }

        UIManager.clearScreen();

        this.startingIndex = ThreadLocalRandom.current().nextInt(0, createdPlayerList.size());
        this.playerList = createdPlayerList;
        GameContext newContext = createGameContext();
        this.context = newContext;

    }

    public GameContext createGameContext() {
        return new GameContext(this.playerList, 0, startingIndex, deck, paradeBoard);
    }

    @Override
    public void exit() {
    }
}

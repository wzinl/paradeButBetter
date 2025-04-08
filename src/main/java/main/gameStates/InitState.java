package main.gameStates;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

import org.fusesource.jansi.Ansi;

import main.context.GameContext;
import main.helpers.inputHandlers.InputManager;
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
        int numPlayers = inputManager.getIntInRange(
        Ansi.ansi().bold().fg(Ansi.Color.CYAN).a("🎮 Enter number of players: ").reset().toString(), 1, 6);
        System.out.println();
        int numBots = 0;
        int difficulty = 0;

        if (numPlayers != 6 && numPlayers != 1) {
            numBots = inputManager.getIntInRange(
                    Ansi.ansi().bold().fg(Ansi.Color.MAGENTA).a("🤖 Enter number of bots: ").reset().toString(), 0, 6 - numPlayers);
            System.out.println();
            if (numBots != 0) {
                difficulty = inputManager.getIntInRange(
                        Ansi.ansi().bold().fg(Ansi.Color.RED).a("Choose bot level (1-3): ").reset().toString(), 1, 3);
                System.out.println();
            }
        } else if (numPlayers == 1) {
            numBots = inputManager.getIntInRange(
                    Ansi.ansi().bold().fg(Ansi.Color.MAGENTA).a("🤖 Enter number of bots: ").reset().toString(), 1, 5);
            System.out.println();
            difficulty = inputManager.getIntInRange(
                    Ansi.ansi().bold().fg(Ansi.Color.RED).a("Choose bot level (1-3): ").reset().toString(), 1, 3);
            System.out.println();
        }

        ArrayList<Player> createdPlayerList = new ArrayList<>();
        HashSet<String> playerNames = new HashSet<>();
        UIManager.clearScreen();

        for (int i = 1; i <= numPlayers; i++) {
            String playerName = inputManager.getString(
                                             Ansi.ansi().bold().fg(Ansi.Color.GREEN).a(
                                             "🤓 Enter name of Player " + i + ": ")
                                             .reset().toString());
            
                while (playerNames.contains(playerName)) {
                System.out.println();
                System.out.println(Ansi.ansi().bold().a("Player name taken. Please choose another name.").reset());
                playerName = inputManager.getString(
                                          Ansi.ansi().bold().fg(Ansi.Color.GREEN).a(
                                          "🤓 Enter name of Player " + i + ": "
                                          ).reset().toString());
            }
            playerNames.add(playerName);
            Player player = new Player(playerName);
            player.getPlayerHand().initHand(deck);
            createdPlayerList.add(player);
            UIManager.clearScreen();
        }

        if (numBots != 0) {
            for (int i = 1; i <= numBots; i++) {
                String botName = "";
                do {
                    botName = inputManager.getString(
                            Ansi.ansi().bold().fg(Ansi.Color.GREEN).a("👾 Enter name of Bot " + i + ": ").reset().toString());
                    if (playerNames.contains(botName)) {
                        System.out.println();
                        System.out.println(Ansi.ansi().bold().a(
                                     "Bot name taken. Please choose another name.")
                                           .reset());
                    }
                } while (playerNames.contains(botName));

                playerNames.add(botName); 

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

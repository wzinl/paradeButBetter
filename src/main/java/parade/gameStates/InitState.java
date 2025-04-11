package parade.gameStates;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

import org.fusesource.jansi.Ansi;

import parade.context.GameContext;
import parade.helpers.inputHandlers.InputManager;
import parade.helpers.ui.UIManager;
import parade.models.ParadeBoard;
import parade.models.cards.Deck;
import parade.models.player.Player;
import parade.models.player.bots.RandomBot;
import parade.models.player.bots.SmartBot;
import parade.models.player.bots.SmarterBot;

/**
 * Represents the initialization phase of the game. This state is responsible for:
 * - Prompting for player and bot setup
 * - Handling bot difficulty
 * - Collecting and validating player names
 * - Generating the deck and initial parade board
 * - Creating and passing a GameContext to the GameStateManager
 */
public class InitState extends GameState {

    /** Index of the player who will start the game. Chosen randomly. */
    private int startingIndex;

    /**
     * Constructs the InitState with a fresh deck and parade board.
     *
     * @param gsm           the game state manager
     * @param inputManager  the input manager for handling user input
     */
    public InitState(GameStateManager gsm, InputManager inputManager) {
        super(gsm, inputManager);
        this.deck = new Deck();
        this.paradeBoard = new ParadeBoard(deck);
    }

    /**
     * Runs the initialization state:
     * - Prompts for number of players and bots
     * - Sets bot difficulty
     * - Handles name entry and duplication
     * - Initializes player and bot hands
     * - Randomly chooses a starting player
     * - Prepares the GameContext for the next state
     */
    @Override
    public void run() {
        UIManager.clearScreen();

        int numPlayers = inputManager.getIntInRange(
            Ansi.ansi().bold().fg(Ansi.Color.CYAN).a("🎮 Enter number of players: ").reset().toString(), 1, 6);
        System.out.println();

        int numBots = 0;
        int difficulty = 0;

        // Bot setup logic based on player count
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

        String repeatPrompt = "Name taken. Please choose another name.";
        // Prompt for human player names
        for (int i = 1; i <= numPlayers; i++) {
            String namePrompt = UIManager.getNamePrompt(false, i);
            String playerName = inputManager.getString(namePrompt);
            while (playerNames.contains(playerName)) {
                System.out.println();
                System.out.println(Ansi.ansi().bold().a(repeatPrompt).reset());
                playerName = inputManager.getString(namePrompt);
            }

            playerNames.add(playerName);
            Player player = new Player(playerName);
            player.getPlayerHand().initHand(deck);
            createdPlayerList.add(player);
            UIManager.clearScreen();
        }

        // Add bots if needed
        if (numBots != 0) {
            for (int i = 1; i <= numBots; i++) {
                String botName;
                do {
                    String namePrompt = UIManager.getNamePrompt(true, i);
                    botName = inputManager.getString(namePrompt);
                    if (playerNames.contains(botName)) {
                        System.out.println();
                        System.out.println(Ansi.ansi().bold().a(repeatPrompt).reset());
                    }
                } while (playerNames.contains(botName));

                playerNames.add(botName);
                Player bot;

                // Instantiate bot based on difficulty
                switch (difficulty) {
                    case 1 -> bot = new RandomBot(botName);
                    case 2 -> bot = new SmartBot(botName);
                    case 3 -> bot = new SmarterBot(botName);
                    default -> bot = new SmarterBot(botName);
                }

                bot.getPlayerHand().initHand(deck);
                createdPlayerList.add(bot);
                UIManager.clearScreen();
            }
        }

        UIManager.clearScreen();

        // Randomly determine the starting player
        this.startingIndex = ThreadLocalRandom.current().nextInt(0, createdPlayerList.size());
        this.playerList = createdPlayerList;

        // Create and assign game context for next game state
        GameContext newContext = createGameContext();
        this.context = newContext;
    }

    /**
     * Creates and returns the initial {@link GameContext} with all configured players, deck,
     * board, and starting player index.
     *
     * @return the initialized game context
     */
    public GameContext createGameContext() {
        return new GameContext(this.playerList, 0, startingIndex, deck, paradeBoard);
    }
}

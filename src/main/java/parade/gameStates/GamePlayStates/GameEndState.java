package parade.gameStates.GamePlayStates;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import parade.context.GameContext;
import parade.exceptions.InvalidCardException;
import parade.exceptions.SelectionException;
import parade.gameStates.GameStateManager;
import parade.helpers.inputHandlers.InputManager;
import parade.helpers.inputTypes.ActionInput;
import parade.helpers.inputTypes.CardInput;
import parade.helpers.inputTypes.SelectionInput;
import parade.helpers.ui.UIManager;
import parade.models.cards.Card;
import parade.models.cards.Deck;
import parade.models.player.Player;
import parade.models.player.PlayerBoard;
import parade.models.player.PlayerHand;
import parade.models.player.bots.Bot;
import parade.models.selections.ActionSelection;
import parade.models.selections.CardSelection;
import parade.models.selections.TurnSelection;

/**
 * Final game state that executes after all players have finished their final turns.
 * Handles final discards, card flipping rules, score calculation, and winner announcement.
 */
public class GameEndState extends GamePlayState {

    /** The index of the player who triggered the final round. */
    private final int finalPlayerIndex;

    /**
     * Constructs the GameEndState with required game context and managers.
     *
     * @param gsm           The GameStateManager managing the game's flow.
     * @param context       The shared GameContext.
     * @param inputManager  The InputManager to retrieve user input.
     */
    public GameEndState(GameStateManager gsm, GameContext context, InputManager inputManager) {
        super(gsm, context, inputManager);
        this.finalPlayerIndex = context.getFinalRoundStarterIndex();
    }

    /** Executes the sequence of actions to end the game. */
    @Override
    public void run() {
        try {
            performFinalDiscardPhase();
            handleCardFlipping();
            handleScoring();
        } catch (InvalidCardException e) {
            UIManager.displayErrorMessage(e.getMessage());
        }
    }

    /**
     * Prompts and manages the final discard phase for all players after the final turn.
     *
     * @throws InvalidCardException if an invalid card is discarded.
     */
    private void performFinalDiscardPhase() throws InvalidCardException {
        UIManager.clearScreen();
        UIManager.displayDiscardPrompt();
        inputManager.getEnter();

        for (int i = 0; i < playerList.size(); i++) {
            UIManager.clearScreen();
            context.setCurrentPlayerIndex((finalPlayerIndex + i + 1) % playerList.size());
            performDiscardPhase();
        }

        UIManager.clearScreen();
    }

    /**
     * Handles discarding 2 cards and moving the rest to the board for a single player.
     *
     * @param player The player to process.
     * @throws InvalidCardException if card discard is invalid.
     */
    private void performDiscardPhase() throws InvalidCardException {
        discardTwoCards();
        Player player = playerList.get(context.getCurrentPlayerIndex());
        PlayerHand hand = player.getPlayerHand();
        PlayerBoard board = player.getPlayerBoard();

        for (Card card : hand.getCardList()) {
            board.addCard(card);
        }
        hand.getCardList().clear();
        UIManager.clearScreen();
    }

    /**
     * Retrieves the discard selection from the player.
     *
     * @param current The player discarding.
     * @return The TurnSelection for discard.
     * @throws SelectionException if selection is not valid.
     */
    private TurnSelection getDiscardSelection(Player current) throws SelectionException {
        SelectionInput input = getSelectionInput(current);

        if (input instanceof CardInput cardInput) {
            return new CardSelection(() -> discardCard(current.getPlayerHand(), cardInput));
        }

        if (input instanceof ActionInput action) {
            return new ActionSelection(() -> performAction(action.getActionChar()));
        }

        throw new SelectionException("Error with selection!");
    }

    /**
     * Removes the selected card from the player's hand.
     *
     * @param hand      The player's hand.
     * @param cardInput The selected card index.
     */
    private void discardCard(PlayerHand hand, CardInput cardInput) {
        int cardIndex = cardInput.getCardIndex();
        hand.removeCard(hand.getCardList().get(cardIndex));
    }

    /** Applies the card flipping logic depending on player count and card colors. */
    private void handleCardFlipping() {
        for (String color : Deck.colours) {
            if (playerList.size() > 2) {
                flipForMoreThanTwoPlayers(color);
            } else {
                flipForTwoPlayers(color);
            }
        }
    }

    /**
     * Flips cards for the player(s) who have the most cards of a specific color.
     *
     * @param color The card color being evaluated.
     */
    private void flipForMoreThanTwoPlayers(String color) {
        int highestCount = playerList.stream()
                .mapToInt(p -> p.getPlayerBoard().getCardNumberByColor(color))
                .max().orElse(0);

        for (Player player : playerList) {
            if (player.getPlayerBoard().getCardNumberByColor(color) == highestCount) {
                flipCards(player, color);
            }
        }
    }

    /**
     * For 2-player games, flips cards only if there's a 2+ card difference in that color.
     *
     * @param color The card color being evaluated.
     */
    private void flipForTwoPlayers(String color) {
        Player p1 = playerList.get(0);
        Player p2 = playerList.get(1);

        int p1Count = p1.getPlayerBoard().getCardNumberByColor(color);
        int p2Count = p2.getPlayerBoard().getCardNumberByColor(color);

        if (Math.abs(p1Count - p2Count) >= 2) {
            PlayerBoard boardToFlip = (p1Count > p2Count ? p1 : p2).getPlayerBoard();
            for (Card card : boardToFlip.getPlayerBoardMap().get(color)) {
                card.setIsFaceUp(false);
            }
        }
    }

    /**
     * Helper method to flip all cards of a specific color for a player.
     *
     * @param player The player whose cards will be flipped.
     * @param color  The color of the cards to flip.
     */
    private void flipCards(Player player, String color) {
        for (Card card : player.getPlayerBoard().getPlayerBoardMap().get(color)) {
            card.setIsFaceUp(false);
        }
    }

    /** Calculates the final scores, displays them, and announces the winner. */
    private void handleScoring() {
        UIManager.displayLoadingMessage("Calculating score", 3);

        ArrayList<Player> winners = new ArrayList<>(playerList);
        
        // Sort by score first, then by total cards in playerboard(fewer is better) in case of ties
        winners.sort(
            Comparator.comparing(Player::calculateScore)
            .thenComparing(p -> p.getPlayerBoard().getPlayerBoardMap().values().stream()
                       .mapToInt(List::size)
                       .sum())
        );
        UIManager.displayFinalScores(winners, paradeBoard);
        UIManager.displayScoreboard(winners);
        announceWinner(winners);
    }

    /**
     * Announces the winner or declares a tie.
     *
     * @param winners A sorted list of players by score.
     */
    private void announceWinner(List<Player> winners) {
        // Get the lowest score
        int lowestScore = winners.get(0).getPlayerScore();

        // Filter players with the lowest score
        List<Player> tiedPlayers = winners.stream()
                                .filter(p -> p.getPlayerScore() == lowestScore)
                                .collect(Collectors.toList());
        
        // Check for ties
        if (tiedPlayers.size() > 1 ) {
            UIManager.displayTieResults(tiedPlayers);
        } else {
            UIManager.displayWinner(winners.get(0));
        }
    }

    /**
     * Prompts the player (or bot) to discard two cards at the end of the game.
     *
     * @param player The player discarding.
     * @throws InvalidCardException if a card discard is invalid.
     */
    private void discardTwoCards() throws InvalidCardException {
        Player player = context.getPlayerList().get(context.getCurrentPlayerIndex());
        for (int i = 0; i < 2; i++) {
            if (player instanceof Bot bot) {
                int discardIndex = bot.discardCardEndgame(context.getPlayerList());
                Card toDiscard = bot.getPlayerHand().getCardList().get(discardIndex);
                bot.getPlayerHand().removeCard(toDiscard);

                UIManager.clearScreen();
            } else {
                    boolean discardCompleted = false;
                    
                    while (!discardCompleted) {
                        try {
                            TurnSelection selection = getDiscardSelection(player);
                            selection.execute();
                            if (selection instanceof CardSelection) {
                                discardCompleted = true;
                            }
                        } catch (InvalidCardException e) {
                            UIManager.displayErrorMessage("Invalid card. Please enter a valid card.");
                        } catch (SelectionException e) {
                            UIManager.displayErrorMessage(e.getMessage());
                            System.out.println("Trying Again...");
                        }
                    }
            }
        }
    }
}

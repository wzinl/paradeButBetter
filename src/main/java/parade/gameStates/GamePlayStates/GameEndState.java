package parade.gameStates.GamePlayStates;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

public class GameEndState extends GamePlayState {

    private final int finalPlayerIndex;

    public GameEndState(GameStateManager gsm, GameContext context, InputManager inputManager) {
        super(gsm, context, inputManager);
        this.finalPlayerIndex = context.getFinalRoundStarterIndex();

    }

    @Override
    public void run() {
        try {
            UIManager.clearScreen();

            performFinalDiscardPhase();
            handleCardFlipping();
            handleScoring();

        } catch (InvalidCardException e) {
            UIManager.displayErrorMessage(e.getMessage());
        }
    }

    private void performFinalDiscardPhase() throws InvalidCardException {
        UIManager.displayDiscardPrompt();
        inputManager.getEnter();
        for (int i = 0; i < playerList.size(); i++) {
            int index = (finalPlayerIndex + i + 1) % playerList.size();
            performDiscardPhase(playerList.get(index));
            UIManager.clearScreen();

        }
    }

    private void performDiscardPhase(Player player) throws InvalidCardException {

        discardTwoCards(player);
        // Move remaining cards to board
        PlayerHand hand = player.getPlayerHand();
        PlayerBoard board = player.getPlayerBoard();

        for (Card card : hand.getCardList()) {
            board.addCard(card);
        }
        hand.getCardList().clear();

        UIManager.clearScreen();
    }

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

    private void discardCard(PlayerHand hand, CardInput cardInput) {
        int cardIndex = cardInput.getCardIndex();
        hand.removeCard(hand.getCardList().get(cardIndex));
    }

    private void handleCardFlipping() {
        for (String color : Deck.colours) {
            if (playerList.size() > 2) {
                flipForMoreThanTwoPlayers(color);
            } else {
                flipForTwoPlayers(color);
            }
        }
    }

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

    private void flipForTwoPlayers(String color) {
        Player p1 = playerList.get(0);
        Player p2 = playerList.get(1);

        int p1Count = p1.getPlayerBoard().getCardNumberByColor(color);
        int p2Count = p2.getPlayerBoard().getCardNumberByColor(color);

        if (Math.abs(p1Count - p2Count) >= 2) {
            PlayerBoard boardToFlip = (p1Count > p2Count ? p1 : p2).getPlayerBoard();
            for (Card card : boardToFlip.getPlayerBoardHash().get(color)) {
                card.setIsFaceUp(false);
            }
        }
    }

    private void flipCards(Player player, String color) {
        for (Card card : player.getPlayerBoard().getPlayerBoardHash().get(color)) {
            card.setIsFaceUp(false);
        }
    }

    private void handleScoring() {
        UIManager.displayLoadingMessage("Calculating score", 3);

        ArrayList<Player> winners = new ArrayList<>(playerList);
        winners.sort(Comparator.comparing(Player::calculateScore));

        UIManager.displayFinalScores(playerList, paradeBoard);

        UIManager.displayScoreboard(winners);
        announceWinner(winners);

    }

    private void announceWinner(List<Player> winners) {
        winners.sort((p1, p2) -> Integer.compare(p1.getPlayerScore(), p2.getPlayerScore()));
    
        if (winners.size() > 1 && winners.get(0).getPlayerScore() == winners.get(1).getPlayerScore()) {
            UIManager.displayTieResults(winners);
        } else {
            UIManager.displayWinner(winners.get(0));
        }
    }
    

    private void discardTwoCards(Player player) throws InvalidCardException {
        if (player instanceof Bot bot) {
            for (int i = 0; i < 2; i++) {
                int discardIndex = bot.discardCardEndgame(player.getPlayerHand(), paradeBoard);
                Card discarded = player.getPlayerHand().getCardList().get(discardIndex);
                player.getPlayerHand().removeCard(discarded);

                UIManager.displayBotDiscard(player, discarded);
                UIManager.pauseExecution(3000);
            }
        } else {
            for (int i = 0; i < 2; i++) {
                boolean discardCompleted = false;
                while (!discardCompleted) {
                    try {
                        TurnSelection selection = getDiscardSelection(player);
                        selection.execute();
                        discardCompleted = true;
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

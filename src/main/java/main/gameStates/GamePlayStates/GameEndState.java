package main.gameStates.GamePlayStates;

import java.util.ArrayList;
import java.util.Comparator;

import main.context.GameContext;
import main.exceptions.InvalidCardException;
import main.exceptions.SelectionException;
import main.gameStates.GameStateManager;
import main.helpers.InputHandler;
import main.helpers.ScreenUtils;
import main.models.cards.Card;
import main.models.cards.Deck;
import main.models.player.Player;
import main.models.player.PlayerBoard;
import main.models.player.PlayerHand;
import main.models.selections.ActionSelection;
import main.models.selections.CardSelection;
import main.models.selections.TurnSelection;
import main.models.selections.input.ActionInput;
import main.models.selections.input.CardInput;
import main.models.selections.input.SelectionInput;

public class GameEndState extends GamePlayState {

    public GameEndState(GameStateManager gsm, GameContext context, InputHandler inputHandler) {
        super(gsm, context, inputHandler);
    }

    @Override
    public void enter() {
        try {
            System.out.println("Game End State entering");
            System.out.print("\033c");

            performFinalDiscardPhase();
            handleCardFlipping();
            handleScoring();

        } catch (InvalidCardException e) {
            System.out.println(e.getMessage());
        }
    }

    private void performFinalDiscardPhase() throws InvalidCardException {
        for (int i = 0; i < playerList.size(); i++) {
            int index = (finalPlayerIndex + i) % playerList.size();
            performDiscardPhase(playerList.get(index));
        }
    }

    private void performDiscardPhase(Player currentPlayer) throws InvalidCardException {
        PlayerHand hand = currentPlayer.getPlayerHand();
        PlayerBoard board = currentPlayer.getPlayerBoard();

        System.out.println(currentPlayer.getPlayerName() + "'s turn");
        ScreenUtils.getDisplay(currentPlayer, paradeBoard);
        System.out.println("Discard 2 cards from hand.\n");

        for(int i = 0; i < 2; i++){
            
            boolean discardCompleted = false;
            while (!discardCompleted) {
                try {
                    System.out.println(hand);
                    TurnSelection selection = getDiscardSelection(currentPlayer);
                    selection.execute();
                    discardCompleted = true;
                } catch (InvalidCardException e) {
                    System.out.println("Invalid card. Please enter a valid card.");
                } catch (SelectionException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Trying Again...");
                }
            }
        }

        // Add remaining cards to board and clear hand
        for (Card card : hand.getCardList()) {
            board.addCard(card);
        }
        hand.getCardList().clear();
        System.out.print("\033c");
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
        simulateLoading("Calculating score", 3);

        ArrayList<Player> winners = new ArrayList<>(playerList);
        winners.sort(Comparator.comparing(Player::calculateScore));

        System.out.println(ScreenUtils.getDisplay(playerList, paradeBoard));
        for (Player player : winners) {
            System.out.printf("%s has scored: %d%n", player.getPlayerName(), player.getPlayerScore());
        }

        System.out.println("\n" + winners.get(0).getPlayerName() + " wins!");
    }

    private void simulateLoading(String message, int dotCount) {
        System.out.print(message);
        for (int i = 0; i < dotCount; i++) {
            try {
                Thread.sleep(500);
                System.out.print(".");
            } catch (InterruptedException e) {
                System.out.println("Sleep has been interrupted!");
            }
        }
        System.out.println();
    }

    @Override
    public void exit() {
        // Cleanup if needed
    }
}

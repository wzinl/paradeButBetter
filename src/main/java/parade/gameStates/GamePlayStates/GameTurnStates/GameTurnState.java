package parade.gameStates.GamePlayStates.GameTurnStates;

import java.util.ArrayList;
import java.util.List;

import parade.context.GameContext;
import parade.exceptions.InvalidCardException;
import parade.exceptions.SelectionException;
import parade.gameStates.GamePlayStates.GamePlayState;
import parade.gameStates.GameStateManager;
import parade.helpers.inputHandlers.InputManager;
import parade.helpers.inputTypes.ActionInput;
import parade.helpers.inputTypes.CardInput;
import parade.helpers.inputTypes.SelectionInput;
import parade.helpers.ui.UIManager;
import parade.models.ParadeBoard;
import parade.models.cards.Card;
import parade.models.player.Player;
import parade.models.player.PlayerBoard;
import parade.models.player.PlayerHand;
import parade.models.player.bots.Bot;
import parade.models.selections.ActionSelection;
import parade.models.selections.CardSelection;
import parade.models.selections.TurnSelection;

/**
 * Abstract class for handling turn-based gameplay logic during normal and final rounds.
 */
public abstract class GameTurnState extends GamePlayState {

    /**
     * Constructs a GameTurnState.
     *
     * @param gsm            The GameStateManager handling the game flow.
     * @param context        The shared GameContext.
     * @param inputManager   The input handler for this state.
     */
    public GameTurnState(GameStateManager gsm, GameContext context, InputManager inputManager) {
        super(gsm, context, inputManager);
    }

    /**
     * Executes a single turn for the given player, including input handling and card playing.
     *
     * @param current The player taking the turn.
     */
    protected void playTurn(Player current) {
        PlayerHand hand = current.getPlayerHand();
        List<Card> cardList = hand.getCardList();
        boolean turnCompleted = false;

        while (!turnCompleted) {
            try {
                if (current instanceof Bot currBot) {
                    int index = currBot.getNextCardIndex(cardList, paradeBoard);
                    playCard(current, index);
                    turnCompleted = true;
                } else {
                    TurnSelection selection = getTurnSelection(current);
                    selection.execute();
                    if (selection instanceof CardSelection) {
                        turnCompleted = true;
                    }
                }
            } catch (InvalidCardException e) {
                System.out.println("Invalid card. Please enter a valid card.");
            } catch (SelectionException e) {
                System.out.println(e.getMessage());
                System.out.println("Trying Again...");
            }
            UIManager.clearScreen();
        }
    }

    /**
     * Plays the selected card from the player's hand and updates all relevant game state.
     *
     * @param current The player playing the card.
     * @param index   The index of the card in the player's hand.
     * @throws InvalidCardException If the card cannot be legally played.
     */
    protected void playCard(Player current, int index) throws InvalidCardException {
        UIManager.clearScreen();
        PlayerHand hand = current.getPlayerHand();
        List<Card> cardList = hand.getCardList();
        Card chosenCard = cardList.get(index);
        PlayerBoard playerBoard = current.getPlayerBoard();

        if (current instanceof Bot) {
            UIManager.displayBotAction(current, index);
        }

        int chosenValue = chosenCard.getValue();
        ArrayList<Card> removedCards = new ArrayList<>();
        ParadeBoard beforeRemovalParadeBoard = paradeBoard;

        for (int i = 0; i < paradeBoard.getCardList().size() - chosenValue; i++) {
            Card currentCard = paradeBoard.getCardList().get(i);
            if (currentCard.getValue() <= chosenValue || currentCard.getColor().equals(chosenCard.getColor())) {
                removedCards.add(currentCard);
            }
        }

        hand.removeCard(chosenCard);

        if (this instanceof NotFinalRoundTurnState) {
            hand.drawCard(deck);
        }

        for (Card card : removedCards) {
            playerBoard.addCard(card);
        }

        UIManager.displayCardPlay(current, chosenCard, beforeRemovalParadeBoard, playerBoard, removedCards);

        for (Card card : removedCards) {
            paradeBoard.remove(card);
        }

        paradeBoard.addToBoard(chosenCard);
    }

    /**
     * Processes and returns the next user selection input for the current player.
     *
     * @param current The player whose input is being processed.
     * @return A TurnSelection representing the selected action or card.
     * @throws SelectionException If input is invalid or cannot be converted to a valid selection.
     */
    protected TurnSelection getTurnSelection(Player current) throws SelectionException {
        SelectionInput input = getSelectionInput(current);

        if (input instanceof CardInput card) {
            return new CardSelection(() -> playCard(current, card.getCardIndex()));
        }

        if (input instanceof ActionInput action) {
            return new ActionSelection(() -> performAction(action.getActionChar()));
        }

        throw new SelectionException("Error with selection!");
    }
}

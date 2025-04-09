package parade.gameStates.GamePlayStates.GameTurnStates;

import java.util.ArrayList;
import java.util.List;

import parade.context.GameContext;
import parade.exceptions.InvalidCardException;
import parade.exceptions.SelectionException;
import parade.gameStates.GamePlayStates.GamePlayState;
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
import parade.gameStates.GameStateManager;

public abstract class GameTurnState extends GamePlayState{
    public GameTurnState(GameStateManager gsm, GameContext context, InputManager inputManager) {
        super(gsm, context, inputManager);
    }

    protected void playTurn(Player current) {
        PlayerHand hand = current.getPlayerHand();
        List <Card> cardList = hand.getCardList();
        boolean turnCompleted = false;
        while (!turnCompleted) {
            try {
                if(current instanceof Bot currBot){
                    int index = currBot.getNextCardIndex(cardList, paradeBoard);
                    playCard(current, index);
                    turnCompleted = true;
                }
                // if Human player
                 else {
                    TurnSelection selection = getTurnSelection(current);
                    selection.execute();
                    if(selection instanceof CardSelection){
                        turnCompleted = true;
                    }
                }
                UIManager.clearScreen();
            } catch (InvalidCardException e) {
                System.out.println("Invalid card. Please enter a valid card.");
            } catch(SelectionException e){
                System.out.println(e.getMessage());
                System.out.println("Trying Again...");
            }
        }
        
    }

    protected void playCard(Player current, int index) throws InvalidCardException{
        UIManager.clearScreen();
        PlayerHand hand = current.getPlayerHand();
        List <Card> cardList = hand.getCardList();
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

        UIManager.displayCardPlay(current,chosenCard, beforeRemovalParadeBoard, playerBoard, removedCards);
        for (Card card : removedCards) {
            paradeBoard.remove(card);
        }
        paradeBoard.addToBoard(chosenCard);

    }

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

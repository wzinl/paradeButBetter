package main.gameStates.GamePlayStates.GameTurnStates;

import java.util.ArrayList;
import java.util.List;

import main.context.GameContext;
import main.exceptions.InvalidCardException;
import main.exceptions.SelectionException;
import main.gameStates.GamePlayStates.GamePlayState;
import main.gameStates.GameStateManager;
import main.helpers.inputHandlers.InputManager;
import main.helpers.inputTypes.ActionInput;
import main.helpers.inputTypes.CardInput;
import main.helpers.inputTypes.SelectionInput;
import main.helpers.ui.DisplayEffects;
import main.helpers.ui.UIManager;
import main.models.ParadeBoard;
import main.models.cards.Card;
import main.models.player.Player;
import main.models.player.PlayerBoard;
import main.models.player.PlayerHand;
import main.models.player.bots.Bot;
import main.models.player.bots.RandomBot;
import main.models.player.bots.SmartBot;
import main.models.selections.ActionSelection;
import main.models.selections.CardSelection;
import main.models.selections.TurnSelection;

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
                System.out.println(DisplayEffects.BOLD+DisplayEffects.ANSI_BRIGHT_WHITE+DisplayEffects.RED_BG+"Invalid card. Please enter a valid card."+DisplayEffects.ANSI_RESET);
            } catch(SelectionException e){
                System.out.println(e.getMessage());
                System.out.println(DisplayEffects.BOLD+"Trying Again..."+DisplayEffects.ANSI_RESET);
            }
        }
        
    }

    protected void playCard(Player current, int index) throws InvalidCardException{
        PlayerHand hand = current.getPlayerHand();
        List <Card> cardList = hand.getCardList();
        Card chosenCard = cardList.get(index);
        PlayerBoard playerBoard = current.getPlayerBoard();

        

        if (current instanceof RandomBot || current instanceof SmartBot) {
            UIManager.displayBotAction(current, index);
            UIManager.pauseExecution(3000);
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
        
        UIManager.displayCardPlay(current,chosenCard, beforeRemovalParadeBoard, playerBoard, removedCards);
        for (Card card : removedCards) {
            paradeBoard.remove(card);
            playerBoard.addCard(card);
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
        throw new SelectionException(DisplayEffects.BOLD+DisplayEffects.RED_BG+DisplayEffects.ANSI_BRIGHT_WHITE+"Error with selection!"+DisplayEffects.ANSI_RESET);
    }
}

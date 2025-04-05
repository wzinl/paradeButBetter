package main.gameStates.GamePlayStates;

import java.util.List;

import main.context.GameContext;
import main.exceptions.InvalidCardException;
import main.exceptions.SelectionException;
import main.gameStates.GameStateManager;
import main.helpers.CardEffects;
import main.helpers.InputHandler;
import main.java.main.helpers.ui.UIManager;
import main.models.cards.Card;
import main.models.input.ActionInput;
import main.models.input.CardInput;
import main.models.input.SelectionInput;
import main.models.player.Player;
import main.models.player.PlayerBoard;
import main.models.player.PlayerHand;
import main.models.player.bots.Bot;
import main.models.player.bots.RandomBot;
import main.models.player.bots.SmartBot;
import main.models.selections.ActionSelection;
import main.models.selections.CardSelection;
import main.models.selections.TurnSelection;


public class GameTurnState extends GamePlayState {

    private boolean isInFinalRound;    

    public GameTurnState(GameStateManager gsm, GameContext context, InputHandler inputHandler) {
        super(gsm, context, inputHandler);
        this.isInFinalRound = context.getIsInFinalRound();
    }

    @Override
    public void enter() {
        UIManager.clearScreen();

        while (!isInFinalRound) {
            Player currentPlayer = playerList.get(currentPlayerIndex);
            playTurn(currentPlayer);

            if (currentPlayer.hasCollectedAllColours() || deck.isEmpty()) {
                context.setInFinalRound(true);
                isInFinalRound = true;
                System.out.println("You have collected all 6 colors! Moving on to the final round!");
                context.setFinalRoundStarterIndex(currentPlayerIndex);
                this.finalPlayerIndex = currentPlayerIndex;
            }

            this.currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
        }
        finalRound();
    }

    private void playTurn(Player current) {
        PlayerHand hand = current.getPlayerHand();
        List <Card> cardList = hand.getCardList();

        boolean turnCompleted = false;
        while (!turnCompleted) {
            try {
                if(current instanceof Bot currBot){
                    int index = currBot.getNextCardIndex(cardList, paradeBoard);
                    playCard(current, index);
                }
                // if Human player
                 else {
                    TurnSelection selection = getTurnSelection(current);
                    selection.execute();
                }
                UIManager.clearScreen();
                turnCompleted = true;
            } catch (InvalidCardException e) {
                System.out.println("Invalid card. Please enter a valid card.");
            } catch(SelectionException e){
                System.out.println(e.getMessage());
                System.out.println("Trying Again...");
            }
        }
    }

    private void playCard(Player current, int index) throws InvalidCardException{
        PlayerHand hand = current.getPlayerHand();
        PlayerBoard board = current.getPlayerBoard();
        List <Card> cardList = hand.getCardList();
        Card chosenCard = cardList.get(index);

        if (current instanceof RandomBot || current instanceof SmartBot) {
            UIManager.displayBotAction(current, index);
            UIManager.pauseExecution(3000);
        }

        CardEffects.apply(chosenCard, paradeBoard, board);
        hand.removeCard(chosenCard);
        if (!isInFinalRound) {
            hand.drawCard(deck);
        }
    }

    private TurnSelection getTurnSelection(Player current) throws SelectionException {
        SelectionInput input = getSelectionInput(current);
    
        if (input instanceof CardInput card) {
            return new CardSelection(() -> playCard(current, card.getCardIndex()));
        }
        if (input instanceof ActionInput action) {
            return new ActionSelection(() -> performAction(action.getActionChar()));
        }
        throw new SelectionException("Error with selection!");
    }
    

    private void finalRound() {
        this.isInFinalRound = true;
        System.out.println("Each player gets one final turn! No more cards will be drawn!");

        while (currentPlayerIndex != finalPlayerIndex) {
            Player currentPlayer = playerList.get(currentPlayerIndex);
            playTurn(currentPlayer);
            this.currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
        }

        playTurn(playerList.get(finalPlayerIndex));
        this.currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
    }

    @Override
    public void exit() {
        System.out.println("Exiting TurnState.");
    }
}
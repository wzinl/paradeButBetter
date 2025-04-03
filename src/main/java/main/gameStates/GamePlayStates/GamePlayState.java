package main.gameStates.GamePlayStates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import main.context.GameContext;
import main.gameStates.GameState;
import main.gameStates.GameStateManager;
import main.helpers.InputHandler;
import main.helpers.ScreenUtils;
import main.models.cards.Card;
import main.models.player.Player;
import main.models.selections.input.SelectionInput;

public abstract class GamePlayState extends GameState{

    private static final Map<String, Character> ACTION_MAP = Map.of(
        "Save Game", 'S',
        "Exit Game", 'Q',
        "Change Input Type", 'C'
    );


    public GamePlayState(GameStateManager gsm, GameContext context,  InputHandler inputHandler) {
        super(gsm, context, inputHandler);
    }

    protected void performAction(char action){
        System.out.println("Performing action");
    }

    protected SelectionInput getSelectionInput(Player current) {
        if (current.getPreferMenu()) {
            try {
                return getMenuSelection(current);
            } catch (IOException e) {
                System.out.println("An error has occurred using menu selection mode! Defaulting to entry selection.");
            }
        }
        return getEntrySelection(current);
    }

    protected SelectionInput getEntrySelection(Player current) {
        System.out.println(ScreenUtils.getDisplay(current, paradeBoard));
        ArrayList <Card> cardList = current.getPlayerHand().getCardList();
        return inputHandler.getIntInRangeWithExceptions(
            String.format("Which card would you like to play? (%d to %d): ", 1, cardList.size()),
            1, cardList.size(), ACTION_MAP);
    }

    protected SelectionInput getMenuSelection(Player current) throws IOException{
        return inputHandler.turnSelect(paradeBoard, current, ACTION_MAP);
    }

    
}

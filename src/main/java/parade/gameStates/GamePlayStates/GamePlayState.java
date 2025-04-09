package parade.gameStates.GamePlayStates;

import java.util.HashMap;
import java.util.Map;

import parade.context.GameContext;
import parade.gameStates.GamePlayStates.GameTurnStates.FinalRoundTurnState;
import parade.gameStates.GamePlayStates.GameTurnStates.NotFinalRoundTurnState;
import parade.helpers.inputHandlers.InputManager;
import parade.helpers.inputTypes.SelectionInput;
import parade.helpers.ui.UIManager;
import parade.models.player.Player;
import parade.gameStates.GameState;
import parade.gameStates.GameStateManager;

public abstract class GamePlayState extends GameState{

    private final Map<Character, Runnable> actionHandlers = new HashMap<>();
    protected int currentPlayerIndex;



    public GamePlayState(GameStateManager gsm, GameContext context,  InputManager inputManager) {
        super(gsm, context, inputManager);
        this.currentPlayerIndex = context.getCurrentPlayerIndex();
        initializeActionHandlers();
    }
    
    private void initializeActionHandlers() {
        actionHandlers.put('C', this::changeInputType);
        actionHandlers.put('E', this::exitGame);
        actionHandlers.put('D', this::displayAllPlayerBoards);
    }

    protected void performAction(char action) {
        Runnable actionHandler = actionHandlers.get(action);
        if (actionHandler != null) {
            actionHandler.run();
        } else {
            System.out.println("Invalid action: " + action);
        }
    }

    private void exitGame() {
        gsm.exitGame();
    }

    public void displayAllPlayerBoards() {
        UIManager.displayBoardOverview(context.getPlayerList(), context.getParadeBoard());
        inputManager.getEnter();
    }

    private void changeInputType() {
        Player currentPlayer = context.getPlayerList().get(context.getCurrentPlayerIndex());
        currentPlayer.setPreferMenu(!currentPlayer.getPreferMenu());
    }

    public void updateContext() {
        
        int currentStateIdx;
        if(this instanceof NotFinalRoundTurnState){
            currentStateIdx = 0;
        } else if(this instanceof FinalRoundTurnState){
            currentStateIdx = 1;
            
        } else {
            currentStateIdx = 2;
        }
        context.setCurrentStateIdx(currentStateIdx);
        
    }


    protected SelectionInput getSelectionInput(Player current) {
        return inputManager.turnSelect(paradeBoard, current);
    }
    
}

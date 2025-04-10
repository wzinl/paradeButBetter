package parade.helpers.inputHandlers;

import java.io.IOException;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import parade.helpers.inputTypes.ActionInput;
import parade.helpers.inputTypes.SelectionInput;
import parade.models.ParadeBoard;
import parade.models.player.Player;


public class InputManager {

    protected Terminal terminal;
    protected LineInputHandler lineHandler;
    protected MenuInputHandler menuHandler;
    protected InputHandler currentHandler;

    private static final String[] ACTIONS = {
        "Change Input Type",
        "Display Everybody's Boards",
        "Exit Game"
    };

    public InputManager() {
        try {
            this.terminal = TerminalBuilder.builder().system(true).build();
            this.lineHandler = new LineInputHandler(terminal);
            this.menuHandler = new MenuInputHandler(terminal);
            currentHandler = lineHandler;
            lineHandler.startInput();

        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize terminal input", e);
        }
    }

    public SelectionInput turnSelect(ParadeBoard paradeBoard, Player currentPlayer) {
        try {
            ensureCorrectInputHandler(currentPlayer.getPreferMenu());
            return currentHandler.turnSelect(paradeBoard, currentPlayer, ACTIONS);
        } catch (IOException e) {            
            throw new RuntimeException("Error during turn selection", e);
        }
    }

    private void ensureCorrectInputHandler(boolean preferMenu) throws IOException {
        if (preferMenu) {
            ensureMenuInput();
        } else {
            ensureLineInput();
        }
    }

    public void ensureLineInput() {
        try {
            switchInputHandler(lineHandler);
        } catch (IOException e) {
            throw new RuntimeException("Failed to switch to LineInputHandler", e);
        }
    }
    
    public void ensureMenuInput() {
        try {
            switchInputHandler(menuHandler);
        } catch (IOException e) {
            throw new RuntimeException("Failed to switch to MenuInputHandler", e);
        }
    }

    private void switchInputHandler(InputHandler newHandler) throws IOException {
        if (currentHandler != newHandler) {
            currentHandler.stopInput();
            newHandler.startInput();
            currentHandler = newHandler;
        }
    }
    


    public ActionInput getIntroInput(String[] intoActions) throws IOException{
        ensureMenuInput();
        return menuHandler.introSelect(intoActions);
    }
    

    public int getInt(String prompt) {
        ensureLineInput();
        return lineHandler.getInt(prompt);
    }

    public int getIntInRange(String prompt, int min, int max) {
        ensureLineInput();
        return lineHandler.getIntInRange(prompt, min, max);
    }

    public String getString(String prompt) {
        ensureLineInput();
        return lineHandler.getString(prompt);
    }

    public boolean getYesNo(String prompt) {
        ensureLineInput();
        return lineHandler.getYesNo(prompt);
    }

    public void getEnter() {
        ensureLineInput();
        lineHandler.getEnter();
    }


}

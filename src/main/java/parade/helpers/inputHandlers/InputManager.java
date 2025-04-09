package parade.helpers.inputHandlers;

import java.io.IOException;

import org.jline.terminal.Attributes;
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
    Attributes lineReaderAttributes;
    boolean inLineInput;

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
            inLineInput = false;
            startLineInput();

            lineReaderAttributes = terminal.getAttributes();
            terminal.setAttributes(lineReaderAttributes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize terminal input", e);
        }
    }

    public void startLineInput() {
            this.lineHandler.resume();
            this.lineHandler.startInputThread();
            inLineInput = true; 
    }

    public void stopLineInput() {
        lineHandler.stopInputThread();
        lineReaderAttributes = terminal.getAttributes();
        inLineInput = false;
        menuHandler.flushStdin();
    }


    public void startMenuInput() {
        terminal.enterRawMode();
}
    public void stopSelectInput() {
        terminal.setAttributes(lineReaderAttributes); // Restore original attributes
        menuHandler.flushStdin();
    }


    public SelectionInput turnSelect(ParadeBoard paradeBoard, Player currentPlayer) {
        if(currentPlayer.getPreferMenu()){
            try{
                return menuturnSelect(paradeBoard, currentPlayer);
            }
            catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        return lineTurnSelect(paradeBoard, currentPlayer);
    }

    public SelectionInput lineTurnSelect(ParadeBoard paradeBoard, Player currentPlayer) {
        ensureLineInput();
        return lineHandler.turnSelect(paradeBoard, currentPlayer, ACTIONS);
    }

    public SelectionInput menuturnSelect(ParadeBoard paradeBoard, Player currentPlayer) throws IOException{
        ensureMenuInput();
        return menuHandler.turnSelect(paradeBoard, currentPlayer, ACTIONS);
    }

    public void ensureLineInput() {
        if(!inLineInput){
            stopSelectInput();
            startLineInput();
        }
    }
    public void ensureMenuInput() {
        if(inLineInput){
            stopLineInput();
            startMenuInput();
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

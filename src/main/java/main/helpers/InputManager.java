package main.helpers;

import java.io.IOException;
import java.util.Map;

import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import main.models.ParadeBoard;
import main.models.input.SelectionInput;
import main.models.player.Player;


public class InputManager {

    protected Terminal terminal;
    protected LineInputHandler lineHandler;
    
    protected MenuInputHandler menuHandler;
    Attributes lineReaderAttributes;
    boolean inLineInput;

    private static final Map<String, Character> ACTION_MAP = Map.of(
        "Save Game", 'S',
        "Exit Game", 'Q',
        "Change Input Type", 'C'
    );


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


    public void startSelectInput() {
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
        if(!inLineInput){
            stopSelectInput();
            startLineInput();
        }
        return lineHandler.turnSelect(paradeBoard, currentPlayer, ACTION_MAP);
    }

    public SelectionInput menuturnSelect(ParadeBoard paradeBoard, Player currentPlayer) throws IOException{
        if(inLineInput){
            stopLineInput();
            startSelectInput();
        }
        return menuHandler.turnSelect(terminal,paradeBoard, currentPlayer, ACTION_MAP);
    }





    

    public int getInt(String prompt) {
        return lineHandler.getInt(prompt);
    }

    public int getIntInRange(String prompt, int min, int max) {
        return lineHandler.getIntInRange(prompt, min, max);
    }

    public String getString(String prompt) {
        return lineHandler.getString(prompt);
    }

    public boolean getYesNo(String prompt) {
        return lineHandler.getYesNo(prompt);
    }

    public void getEnter(String prompt) {
        lineHandler.getEnter(prompt);
    }


}

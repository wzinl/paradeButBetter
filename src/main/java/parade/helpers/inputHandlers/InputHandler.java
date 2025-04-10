package parade.helpers.inputHandlers;

import java.io.IOException;

import org.jline.terminal.Terminal;

import parade.helpers.inputTypes.SelectionInput;
import parade.models.ParadeBoard;
import parade.models.player.Player;

public abstract class InputHandler {
    protected final Terminal terminal;

    public InputHandler(Terminal terminal) {
        this.terminal = terminal;
    }
    
    protected abstract void flush();

    public abstract SelectionInput turnSelect(ParadeBoard paradeBoard, Player currentPlayer, String[] actionStrings) throws IOException;
    public abstract void startInput() throws IOException;
    public abstract void stopInput() throws IOException;
    
}

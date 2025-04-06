package main.helpers.inputTypes;

public final class ActionInput implements SelectionInput {
    private final char actionChar;

    public ActionInput(char actionChar) {

        this.actionChar = (actionChar +"").toUpperCase().charAt(0);
    }

    public char getActionChar() {
        return actionChar;
    }
}

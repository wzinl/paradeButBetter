package main.helpers.inputTypes;

public final class ActionInput implements SelectionInput {
    private final char actionChar;

    public ActionInput(char actionChar) {

        this.actionChar = actionChar ;
    }

    public char getActionChar() {
        return actionChar;
    }
}

package main.models.selections;

public final class ActionSelection implements TurnSelection {
    private final Runnable executable;

    public ActionSelection(Runnable action) {
        executable = action;
    }

    @Override
    public void execute() {
        executable.run();
    }
}

package parade.models.selections;

public final class CardSelection implements TurnSelection {
    private final Runnable executable;

    public CardSelection(Runnable action) {
        this.executable = action;
    }

    @Override
    public void execute(){
        executable.run();
    }
}
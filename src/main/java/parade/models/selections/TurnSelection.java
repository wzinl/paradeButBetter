package parade.models.selections;

public sealed interface TurnSelection permits ActionSelection, CardSelection {
    void execute();
}

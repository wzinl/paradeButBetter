package main.models.selections.input;

public final class CardInput implements SelectionInput {
    private final int cardIndex;

    public CardInput(int cardIndex) {
        this.cardIndex = cardIndex;
    }

    public int getCardIndex() {
        return cardIndex;
    }
}
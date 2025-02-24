import java.util.*;

public class ParadeBoard {
    private ArrayList<Card> parade;
    private int numOfCards;

    public ParadeBoard(ArrayList<Card> parade) {
        // to-do
    }

    public int getNumberOfCards() {
        return parade.size();
    }

    public void addToBoard(Card card) {
        parade.add(card);
    }

    //to check
    public ArrayList<Card> removefromBoard(Card card) {
        ArrayList<Card> selectedcards = new ArrayList<>();
        // check if cards value is greater than board size
        if (card.getValue() > parade.size()) {
            return selectedcards;
        }
        // Cards beyond playedCard's value index enter "removal mode"
        int removalStartIndex = parade.size() - card.getValue() - 1;    
                
        // "removal mode" cards
        ArrayList<Card> remainingParade = new ArrayList<>();

        // if not, the removal pile is no.of cards in board - card val
        for (int i = parade.size()-1; i >= 0 ; i--) {
            Card currentCard = parade.get(i);
            
            if (i >= removalStartIndex) {
                // Remove if same colour or value less than or equal to the value of played card
                if (currentCard.getColor().equals(card.getColor()) || currentCard.getValue() <= card.getValue()) {
                    selectedcards.add(currentCard);
                } else {
                    remainingParade.add(currentCard);
                }

            } 
            // even if not in "removal mode", add to remaining parade
            else {
                remainingParade.add(currentCard);
            }
        }

        // upadte the parade after removal
        parade = remainingParade;
        return selectedcards;
    }

    public ArrayList<Card> displayParade() {
        return parade;
    }
}

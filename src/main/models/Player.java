import java.util.ArrayList;
import java.util.List;

public class Player {
    private String playerName;
    private int playerID;
    private int playerScore;
    private List<Card> playerCards;
    
    // HashMap<Character, ArrayList<Card>> playerCollectedCards; // ????? 

    public Player (String playerName) {
        this.playerName = playerName;
        this.playerCards = new ArrayList<>();
    }


    public int getPlayerScore () {
        return playerScore;
    }

    public List<Card> getPlayerCards() {
        return playerCards;
    }

    

    
}

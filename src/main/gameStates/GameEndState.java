package main.gameStates;

public class GameEndState implements GameState {

    public static final CARDCOLOURS = 6;

    public Player calculateScore(ArrayList<Player> players) {
        
    }


    //take in arrayist of players first
    //for each player get playeyrBoard
    //find who has the most cards per color
    //change value of isFlipped to true for each one
    //calculate score afterwards, flipped cards = 1 point each.
    public void enter() {

        ArrayList<Player> playerList = GameContext.getPlayerList();
        

        //loop to 
        for (int i = 0; i < CARDCOLOURS; i++) { 
            //CHECK FOR EACH COLOR

            ArrayList<Boolean> cardCountCheck = new ArrayList<>; //peg true/false values to flip player cards by color
            int highestColor = -1;

            for (Player player : playerList) {
            int playerColor = player.getPlayerBoard().getCardNumberByColor()
            if (playerColor >= highestColor) {
                highestColor = playerColor
            }
        }
        }

    }




}
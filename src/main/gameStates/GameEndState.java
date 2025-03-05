package main.gameStates;

import java.util.ArrayList;
import java.util.Comparator;

import main.context.GameContext;
import main.models.Card;
import main.models.Player;

public class GameEndState implements GameState {

    

    // take in arrayist of players first
    // for each player get playeyrBoard
    // find who has the most cards per color
    // change value of isFlipped to true for each one
    // calculate score afterwards, flipped cards = 1 point each.
    public void enter() {

        ArrayList<Player> playerList = GameContext.getPlayerList();
        String[] colors = { "Green", "Purple", "Red", "Blue", "Orange", "Grey" };

        // loop to
        for (String color : colors) { // looping throgh each color
            int highestColor = -1;

            // get players with the highest cards per color
            for (Player player : playerList) {
                int playerColor = player.getPlayerBoard().getCardNumberByColor(color);
                highestColor = Math.max(highestColor, playerColor);
            }

            // flip cards of players with highest cards per color
            for (Player player : playerList) {
                if (player.getPlayerBoard().getCardNumberByColor(color) == highestColor) {
                    for (Card card : player.getPlayerCards()) {
                        card.setIsFaceUp(false);
                    }
                }
            }

            ArrayList<Player> winners = new ArrayList<>(playerList);

            //sort by descending order so that the winner with the highest score comes first
            Collections.sort(winners, Comparator.comparing(Player::calculateScore).reversed()) 
            //undecided what to do with winners yet
        }
    }
}


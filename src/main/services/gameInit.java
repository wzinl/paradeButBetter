//kickstart the game
package services;
import main.services.*;


import java.util.ArrayList;



public  class gameInit {
    public static gameInit(){
        ArrayList<Player> playerList = new ArrayList<>();
        for(int i = 0; i < numPlayers; i++){
            playerList.add(new Player(nameList.get(i)));
        }
    }
}
import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    private Game(ArrayList<String> nameList) {
        System.out.println("Game initialized");
        ArrayList<Player> playerList = new ArrayList<>();
        for(int i = 0; i < nameList.size(); i++){
            playerList.add(new Player(nameList.get(i)));
        }
    }
    public static void main(String[] args) {
        
        Scanner scannerObj = new Scanner(System.in);

        System.out.println("Welcome to Parade. Choose number of players: ");
        System.out.println("Choose number of players: ");

        String numPlayersStr = scannerObj.nextLine(); 
        ArrayList<String> nameList = new ArrayList<>();
        int numPlayers = Integer.parseInt(numPlayersStr);
        for (int i = 1; i <= numPlayers; i++){
            System.out.printf("Choose name of player %d: \n", i);
            String playerName = scannerObj.nextLine(); 
            nameList.add(playerName);
        }  
        Game mainGame = new Game(nameList);
        System.out.println(mainGame);
        

    }
}
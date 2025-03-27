package main.error;
public class CardNotFoundException extends Exception{
    public CardNotFoundException(String message){
        super(message);
    }
}java -cp "classes:lib/*" main.server.GameServer

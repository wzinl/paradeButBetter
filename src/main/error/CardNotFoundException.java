package main.error;
public CardNotFoundException extends Exception{
    public CardNotFoundException(String message){
        super(message);
    }
}
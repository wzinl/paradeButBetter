package main.exceptions;

public class InvalidCardException extends Exception {
    public InvalidCardException(String message) {
        super(message);
    }
}

// when the card played does not meet the conditions
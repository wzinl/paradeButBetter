package main.models;

import main.error.InvalidCardException;

public interface CardCollection {
    void addCard(Card card);
    void removeCard(Card card) throws InvalidCardException;

}

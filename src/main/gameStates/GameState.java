package main.gameStates;

interface GameState {
    void enter();    // Called when the state is entered
    void exit();     // Called when switching to another state
}
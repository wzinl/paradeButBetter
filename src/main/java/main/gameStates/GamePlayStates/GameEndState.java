package main.gameStates.GamePlayStates;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import main.context.GameContext;
import main.exceptions.InvalidCardException;
import main.exceptions.SelectionException;
import main.gameStates.GameStateManager;
import main.helpers.InputHandler;
import main.helpers.ScreenUtils;
import main.models.cards.Card;
import main.models.cards.Deck;
import main.models.input.ActionInput;
import main.models.input.CardInput;
import main.models.input.SelectionInput;
import main.models.player.Player;
import main.models.player.PlayerBoard;
import main.models.player.PlayerHand;
import main.models.player.bots.Bot;
import main.models.player.bots.RandomBot;
import main.models.player.bots.SmartBot;
import main.models.player.bots.SmarterBot;
import main.models.selections.ActionSelection;
import main.models.selections.CardSelection;
import main.models.selections.TurnSelection;

public class GameEndState extends GamePlayState {

    public GameEndState(GameStateManager gsm, GameContext context, InputHandler inputHandler) {
        super(gsm, context, inputHandler);
    }

    @Override
    public void enter() {
        try {
            System.out.println("Game End State entering");
            System.out.print("\033c");

            performFinalDiscardPhase();
            handleCardFlipping();
            handleScoring();

        } catch (InvalidCardException e) {
            System.out.println(e.getMessage());
        }
    }

    private void performFinalDiscardPhase() throws InvalidCardException {
        for (int i = 0; i < playerList.size(); i++) {
            int index = (finalPlayerIndex + i) % playerList.size();
            performDiscardPhase(playerList.get(index));
        }
    }

    private void performDiscardPhase(Player currentPlayer) throws InvalidCardException {
        PlayerHand hand = currentPlayer.getPlayerHand();
        PlayerBoard board = currentPlayer.getPlayerBoard();

        System.out.println(currentPlayer.getPlayerName() + "'s turn");
        ScreenUtils.getDisplay(currentPlayer, paradeBoard);
        System.out.println("Discard 2 cards from hand.\n");

        if (currentPlayer instanceof Bot curBot) {
            for (int i = 0; i < 2; i++) {
                hand.removeCard(hand.getCardList().get(curBot.discardCardEndgame(hand, paradeBoard)));
            }
        } 
        //if human player
        else {
            for (int i = 0; i < 2; i++) {

                boolean discardCompleted = false;
                while (!discardCompleted) {
                    try {
                        System.out.println(hand);
                        TurnSelection selection = getDiscardSelection(currentPlayer);
                        selection.execute();
                        discardCompleted = true;
                    } catch (InvalidCardException e) {
                        System.out.println("Invalid card. Please enter a valid card.");
                    } catch (SelectionException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Trying Again...");
                    }
                }
            }
        }

        // Add remaining cards to board and clear hand
        for (Card card : hand.getCardList()) {
            board.addCard(card);
        }
        hand.getCardList().clear();
        System.out.print("\033c");
    }

    private TurnSelection getDiscardSelection(Player current) throws SelectionException {
        SelectionInput input = getSelectionInput(current);

        if (input instanceof CardInput cardInput) {
            return new CardSelection(() -> discardCard(current.getPlayerHand(), cardInput));
        }
        if (input instanceof ActionInput action) {
            return new ActionSelection(() -> performAction(action.getActionChar()));
        }

        throw new SelectionException("Error with selection!");
    }

    // --------------------------------------
    private void discardCard(PlayerHand hand, CardInput cardInput) {
        int cardIndex = cardInput.getCardIndex();
        hand.removeCard(hand.getCardList().get(cardIndex));
    }

    private void handleCardFlipping() {
        for (String color : Deck.colours) {
            if (playerList.size() > 2) {
                flipForMoreThanTwoPlayers(color);
            } else {
                flipForTwoPlayers(color);
            }
        }
    }

    private void flipForMoreThanTwoPlayers(String color) {
        int highestCount = playerList.stream()
                .mapToInt(p -> p.getPlayerBoard().getCardNumberByColor(color))
                .max().orElse(0);

        for (Player player : playerList) {
            if (player.getPlayerBoard().getCardNumberByColor(color) == highestCount) {
                flipCards(player, color);
            }
        }
    }

    private void flipForTwoPlayers(String color) {
        Player p1 = playerList.get(0);
        Player p2 = playerList.get(1);

        int p1Count = p1.getPlayerBoard().getCardNumberByColor(color);
        int p2Count = p2.getPlayerBoard().getCardNumberByColor(color);

        if (Math.abs(p1Count - p2Count) >= 2) {
            PlayerBoard boardToFlip = (p1Count > p2Count ? p1 : p2).getPlayerBoard();
            for (Card card : boardToFlip.getPlayerBoardHash().get(color)) {
                card.setIsFaceUp(false);
            }
        }
    }

    private void flipCards(Player player, String color) {
        for (Card card : player.getPlayerBoard().getPlayerBoardHash().get(color)) {
            card.setIsFaceUp(false);
        }
    }

    private void handleScoring() {
        simulateLoading("Calculating score", 3);

        ArrayList<Player> winners = new ArrayList<>(playerList);
        winners.sort(Comparator.comparing(Player::calculateScore));

        System.out.println(ScreenUtils.getDisplay(playerList, paradeBoard));
        
        displayScoreboard(winners);
        announceWinner(winners);

        for (Player player: winners){
            System.out.println(player.getPlayerName() + ":" + player.getPlayerScore());
        }
    }

    private void displayScoreboard(List<Player> winners) {
        
        System.out.println(
            "██████╗ ███████╗███████╗██╗   ██╗██╗  ████████╗███████╗\n" +
            "██╔══██╗██╔════╝██╔════╝██║   ██║██║  ╚══██╔══╝██╔════╝\n" +
            "██████╔╝█████╗  ███████╗██║   ██║██║     ██║   ███████╗\n" +
            "██╔══██╗██╔══╝  ╚════██║██║   ██║██║     ██║   ╚════██║\n" +
            "██║  ██║███████╗███████║╚██████╔╝███████╗██║   ███████║\n" +
            "╚═╝  ╚═╝╚══════╝╚══════╝ ╚═════╝ ╚══════╝╚═╝   ╚══════╝"
        );
            
        
        // Dynamic column sizing
        int maxNameLength = winners.stream()
            .mapToInt(p -> p.getPlayerName().length())
            .max()
            .orElse(10);
    
        int nameWidth = Math.max(maxNameLength + 2, 12);
        int scoreWidth = 8;
        int positionWidth = 10;
    
        String divider = String.join("+", 
            "-".repeat(positionWidth),
            "-".repeat(nameWidth),
            "-".repeat(scoreWidth));
    
        System.out.println("\n" + "=".repeat(divider.length() + 2) + "\n");
        System.out.printf("%-" + positionWidth + "s | %-" + nameWidth + "s | %" + scoreWidth + "s%n",
            "POSITION", "PLAYER", "SCORE");
        System.out.println(divider);
    
        int displayRank = 1;
    
        for (int i = 0; i < winners.size(); i++) {
            Player player = winners.get(i);
            String position;
    
            if (i > 0 && player.getPlayerScore() == winners.get(i - 1).getPlayerScore()) {
                position = "";
            } else {
                switch (displayRank) {
                    case 1: position = "1st"; break;
                    case 2: position = "2nd"; break;
                    case 3: position = "3rd"; break;
                    default: position = displayRank + "th";
                }
                displayRank++;
            }
    
            System.out.printf("%-" + positionWidth + "s | %-" + nameWidth + "s | %" + scoreWidth + "d%n",
                    position, player.getPlayerName(), player.getPlayerScore());
        }
    
        System.out.println("\n" + "=".repeat(divider.length() + 2) + "\n");
    }
    
    private void announceWinner(List<Player> winners) {
        if (winners.size() > 1 && winners.get(0).getPlayerScore() == winners.get(1).getPlayerScore()) {
            displayTie(winners);
        } else {
            displayChampion(winners.get(0));
        }
    }
    
    //Tie
    public void displayTie(List<Player> winners){
        int topScore = winners.get(0).getPlayerScore();
        int nameWidth = Math.max(
            winners.stream()
                .filter(p -> p.getPlayerScore() == topScore)
                .mapToInt(p -> p.getPlayerName().length())
                .max()
                .orElse(10) + 2,
            12
        );

        System.out.println("TIED RESULTS");
        for (Player p : winners) {
            if (p.getPlayerScore() != topScore) break;
            System.out.printf("• %-" + (nameWidth - 2) + "s - %d points%n",
                    p.getPlayerName(), p.getPlayerScore());
        }
    }

    // One winner
    private void displayChampion(Player winner) {
        String winnerName = winner.getPlayerName();
        int winnerScore = winner.getPlayerScore();
        int boxWidth = 40; // Total width of the box
    
        String topBorder = "╔" + "═".repeat(boxWidth - 2) + "╗";
        String middleBorder = "╠" + "═".repeat(boxWidth - 2) + "╣";
        String bottomBorder = "╚" + "═".repeat(boxWidth - 2) + "╝";
    
        System.out.println(topBorder);
        System.out.println(formatBoxLine("CHAMPION", boxWidth));
        System.out.println(middleBorder);
        System.out.println(formatBoxLine(winnerName, boxWidth));
        System.out.println(formatBoxLine(winnerScore + " POINTS", boxWidth));
        System.out.println(bottomBorder);
        System.out.println(centerText("CONGRATULATIONS!", boxWidth));
    }
    
    private String formatBoxLine(String text, int width) {
        int contentWidth = width - 4; // 2 for borders '║', 2 for padding
        int padding = contentWidth - text.length();
        int leftPad = padding / 2;
        int rightPad = padding - leftPad;
    
        return "║ " + " ".repeat(leftPad) + text + " ".repeat(rightPad) + " ║";
    }
    
    private String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text;
    }
    

    private void simulateLoading(String message, int dotCount) {
        System.out.print(message);
        for (int i = 0; i < dotCount; i++) {
            try {
                Thread.sleep(10);
                System.out.print(".");
            } catch (InterruptedException e) {
                System.out.println("Sleep has been interrupted!");
            }
        }
        System.out.println();
    }

    @Override
    public void exit() {
        // Cleanup if needed
    }
}

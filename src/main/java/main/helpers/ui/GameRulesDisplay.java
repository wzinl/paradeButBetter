package main.helpers.ui;

public class GameRulesDisplay {
    
    public static String constructGameInstructions () {
        String ruleHeader = DisplayEffects.BOLD + DisplayEffects.ANSI_CYAN + "\n🎪 Parade Game Rules 🃏" + DisplayEffects.ANSI_RESET +"\n\n\n";

        StringBuilder objectiveBuilder = new StringBuilder();
        objectiveBuilder.append(DisplayEffects.BOLD + DisplayEffects.ANSI_PURPLE + "🎯 Objective:" + DisplayEffects.ANSI_RESET)
                        .append("Score the fewest points by strategically playing cards and collecting as few as possible.\n\n");
        
        String gameObjective = objectiveBuilder.toString();

        StringBuilder setupBuilder = new StringBuilder();
        setupBuilder.append(DisplayEffects.BOLD + DisplayEffects.ANSI_PURPLE + "Setup:" + DisplayEffects.ANSI_RESET +"\n")
                    .append("1. Deck Composition:\n")
                    .append("   • 6 colors (e.g., Red, Yellow, Green, Blue, Purple, Grey).\n")
                    .append("   • Each color has cards numbered 0 to 10.\n")
                    .append("2. Deal Cards:\n")
                    .append("   • 5 cards will be dealt to each player (face down).\n")
                    .append("   • 6 cards will be randomly chosen to form the initial parade\n")
                    .append("   • The remaining cards become the draw pile.\n\n");

        String gameSetup = setupBuilder.toString();

        StringBuilder turnBuilder = new StringBuilder();
        turnBuilder.append(DisplayEffects.BOLD + DisplayEffects.ANSI_PURPLE + "🔁 On Your Turn:" + DisplayEffects.ANSI_RESET +"\n")
                    .append("1. Play a Card:\n")
                    .append("   • Choose a card from your hand and place it at the end of the parade\n")
                    .append("2. Check for Card Collection:\n")
                    .append("   • The number on your card determines the number of safe cards\n")
                    .append("   • These cards are safe and stay on the parade\n")
                    .append("   • From the rest, collect cards that are:\n")
                    .append("       • The same color as the played card, OR\n")
                    .append("       • Have a number less than or equal to the played card.\n")
                    .append("3. Add Collected Cards:\n")
                    .append("   • Your board (containing all the cards you have collected) will be shown to you\n")
                    .append("4. Draw a New Card:\n")
                    .append("   • You will always have 5 cards on hand (unless draw pile is empty).\n\n");

        String gameTurnIntruc = turnBuilder.toString();

        StringBuilder endTriggBuilder = new StringBuilder();
        endTriggBuilder.append(DisplayEffects.BOLD + DisplayEffects.ANSI_PURPLE + "🚨 Game End Trigger:" + DisplayEffects.ANSI_RESET +'\n')
                        .append("• When the draw pile is empty\n")
                        .append("• One player has collected cards of all 6 colours\n\n");

        String endTriggString = endTriggBuilder.toString();

        StringBuilder scoreBuilder = new StringBuilder();
        scoreBuilder.append(DisplayEffects.BOLD + DisplayEffects.ANSI_PURPLE + "📊 Scoring:" + DisplayEffects.ANSI_RESET +"\n")
                    .append("1. Each card = its number in points\n")
                    .append("2. Color Majority Bonus:\n")
                    .append("   • If you have the most cards of a color, all of them = 1 point each\n")
                    .append("   • Ties don't count\n")
                    .append("3. Add up your total points\n")
                    .append("4. Lowest score wins\n\n\n");  

        String scoreString = scoreBuilder.toString();

        String ruleEndString = DisplayEffects.BOLD + DisplayEffects.ANSI_GREEN + "Good luck and enjoy the game!" + DisplayEffects.ANSI_RESET;

        StringBuilder instructionBuilder = new StringBuilder();
        instructionBuilder.append(ruleHeader).append(gameObjective).append(gameSetup).append(gameTurnIntruc).append(endTriggString).append(scoreString).append(ruleEndString);

        String gameInstructions = instructionBuilder.toString();
        return gameInstructions;
    }
}

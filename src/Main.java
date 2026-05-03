/**
 * Entry point for War card game.
 * Creates a CardGame instance and starts the loop.
 */
public class Main {

    /**
     * Main Method - launches card game
     * @param args command line arguments (not used)
     */

    public static void main(String[] args) {
        CardGame game = new CardGame();
        game.run();
    }
}


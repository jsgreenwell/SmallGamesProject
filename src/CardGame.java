import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Manages the War card game
 * Handles the game loop, player turns, War resolution, input validation,
 * and saving results to a file
 */
public class CardGame {

    /** File where game results are saved. */
    private static final String RESULTS_FILE = "results.txt";

    /** Max rounds before declaring a draw to prevent infinite games. */
    private static final int MAX_ROUNDS = 1000;

    /** True while the player wants to keep playing. */
    public boolean keepPlaying = true;

    /** Scanner for reading player input. */
    private final Scanner scanner;

    /** Each player's hand stored as a list of cards. */
    private List<List<Card>> playerHands;

    /** Player names. */
    private final List<String> playerNames;

    /** Number of players (3 for this game). */
    private static final int NUM_PLAYERS = 3;

    /**
     * Constructs a new CardGame and initializes player names.
     */
    public CardGame() {
        scanner = new Scanner(System.in);
        playerNames = new ArrayList<>();
        playerHands = new ArrayList<>();
    }

    /**
     * Runs the main game loop - asks for names, deals cards,
     * plays rounds, and asks if the player wants to play again.
     */
    public void run() {
        System.out.println("================================");
        System.out.println("   Welcome to War (3 Players!   ");
        System.out.println("================================");

        while (keepPlaying) {
            setupPlayers();
            playGame();
            checkExit();
        }

        System.out.println("Thanks for playing! Goodbye.");
        scanner.close();
    }

    /**
     * Prompts each player to enter their name with basic validation.
     * Names cannot be blank or longer than 20 Char.
     */

    private void setupPlayers() {
        playerNames.clear();
        System.out.println("\nEnter player names:");

        for (int i = 1; i <= NUM_PLAYERS; i++) {
            String name = "";
            while (name.isBlank() || name.length() > 20) {
                System.out.print("Player " + i + " name: ");
                name = scanner.nextLine().trim();
                if (name.isBlank()) {
                    System.out.println(" Name cannot be blank. Try again.");
                }   else if (name.length() > 20) {
                    System.out.println(" Name too long (max 20 characters). Try again.");
                }
            }
            playerNames.add(name);
        }

        // Deal the deck evenly to all 3 players
        Deck deck = new Deck();
        playerHands = deck.dealToPlayers(NUM_PLAYERS);

        System.out.println("\nCards dealt! Each player has " + playerHands.get(0).size() + " cards.");
    }

    /**
     * Runs the full game until one player has all the cards,
     * a player runs out, or the max round has been hit.
     */
    private void playGame() {
        int round = 1;

        while (round <= MAX_ROUNDS && activePlayers() > 1) {
            System.out.println("\n--- Round " + round + "---");
            System.out.print("Press Enter to flip cards...");
            scanner.nextLine();

            playRound();
            printHandSizes();
            round++;
        }

        announceWinner(round);
    }

    /**
     * Plays a single round of War: each active player flips a card,
     * the highest card wins all flipped cards.
     * Ties trigger a War sub-round.
     */
    private void playRound() {
        List<Card> table = new ArrayList<>();
        List<Integer> activePlayers = getActivePlayerIndexes();
        List<Card> flipped = new ArrayList<>();

        // Each player flips their top card.
        for (int index : activePlayers) {
            Card card = playerHands.get(index).remove(0);
            flipped.add(card);
            table.add(card);
            System.out.println(" " + playerNames.get(index) + " plays: " + card.getName());
        }

        // Find the highest card
        Card highest = flipped.stream().max(Card::compareTo).orElse(null);
        if (highest == null) {
            return;
        }

        // Check for ties among the highest rank
        long tieCount = flipped.stream()
                .filter(c -> c.compareTo(highest) == 0)
                .count();

        if (tieCount > 1) {
            System.out.println(" ** WAR! Tied at " + highest.getName() + "! **");
            resolveWar(activePlayers, table);
        }   else {
            // Find the winner
            int winnerIndex = activePlayers.get(flipped.indexOf(highest));
            System.out.println(" >>" + playerNames.get(winnerIndex) + " wins the round!");
            playerHands.get(winnerIndex).addAll(table);
        }
    }















}
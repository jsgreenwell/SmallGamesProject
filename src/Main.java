import javax.swing.*;
import java.util.Scanner;

/**
 * Main.java
 * Entry for Snake game.
 * Shows a text menu, collects player name, then launches the Swing window
 */

public class Main {

    static FileHandler fileHandler = new FileHandler();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("================================");
        System.out.println("          SNAKE  GAME          ");
        System.out.println("================================");
        System.out.print("Enter your name or initials: ");
        String playerName = scanner.nextLine().trim();
        if (playerName.isEmpty()) playerName = "AAA";


        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("--- MAIN MENU ---");
            System.out.println("1. Play Snake");
            System.out.println("2. View High Scores");
            System.out.println("3. Quit");
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    final String name = playerName;
                    SwingUtilities.invokeLater(() -> new Display(name));
                    System.out.println("Game window opened - close it to return to this menu.");
                    // Small pause so the window can open before menu redraws
                    try { Thread.sleep(500); } catch (InterruptedException ex) { /* ignore */ }
                    break;
                case "2":
                    System.out.println();
                    System.out.println(fileHandler.getFormattedScores());
                    break;
                case "3":
                    running = false;
                    System.out.println("Thanks for playing! Goodbye.");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }

        scanner.close();
    }
}
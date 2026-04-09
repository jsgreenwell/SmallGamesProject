public class RockPaperScissors {
    // Boolean variable which is true until player wants to quit
    public boolean keepPlaying = true;

    /**
     * Runs the main game loop - calling other functions as needed.
     * You will create this as for now it just prints out - TBD.
     */
    public void run() {
        System.out.println("Rock Paper Scissors - TBD");
    }

    /**
     * Asks the player if they want to exit or keep playing
     * If they want to exit - change keepPlaying flag (variable) to false.
     * For now just changes flog so this can exit
     */
    public void checkExit() {
        keepPlaying = false;
    }
}

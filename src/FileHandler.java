import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * FileHandler.java
 * CRUD operations for the high score file (highscores.txt).
 * Scores are stored as CSV: name, score, level, date
 */
public class FileHandler {

    private static final String FILE_NAME  = "highscores.txt";
    private static final int    MAX_SCORES = 10;
    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // ---------------------------------------------------------------
    // CREATE — save a new score
    // ---------------------------------------------------------------

    /**
     * Saves a score to highscores.txt.
     * Automatically keeps only the top MAX_SCORES entries.
     */
    public void saveScore(String name, int score, int level) {
        List<String[]> scores = readAll();

        String date = LocalDateTime.now().format(DATE_FMT);
        scores.add(new String[]{name, String.valueOf(score),
                String.valueOf(level), date});

        // Sort descending by score
        scores.sort((a, b) ->
                Integer.compare(Integer.parseInt(b[1]), Integer.parseInt(a[1])));

        // Keep only top MAX_SCORES
        if (scores.size() > MAX_SCORES)
            scores = scores.subList(0, MAX_SCORES);

        writeAll(scores);
    }

    // ---------------------------------------------------------------
    // READ — get scores
    // ---------------------------------------------------------------

    /** Returns the leaderboard as a formatted string for display. */
    public String getFormattedScores() {
        List<String[]> scores = readAll();
        StringBuilder sb = new StringBuilder();
        sb.append("=== HIGH SCORES ===\n\n");

        if (scores.isEmpty()) {
            sb.append("  No scores recorded yet.\n");
        } else {
            sb.append(String.format("  %-4s %-12s %-7s %-5s  %s%n",
                    "#", "Name", "Score", "Level", "Date"));
            sb.append("  ----------------------------------------\n");
            for (int i = 0; i < scores.size(); i++) {
                String[] e = scores.get(i);
                sb.append(String.format("  %-4d %-12s %-7s %-5s  %s%n",
                        i + 1,
                        e[0],
                        e.length > 1 ? e[1] : "0",
                        e.length > 2 ? e[2] : "-",
                        e.length > 3 ? e[3] : ""));
            }
        }
        return sb.toString();
    }

    /** Returns the all-time top score as an int. */
    public int getTopScore() {
        List<String[]> scores = readAll();
        if (scores.isEmpty()) return 0;
        try { return Integer.parseInt(scores.get(0)[1]); }
        catch (NumberFormatException e) { return 0; }
    }

    // ---------------------------------------------------------------
    // DELETE — remove scores
    // ---------------------------------------------------------------

    /** Removes all entries for a given player name (case-insensitive). */
    public void deleteScoresFor(String name) {
        List<String[]> scores = readAll();
        scores.removeIf(e -> e[0].equalsIgnoreCase(name));
        writeAll(scores);
    }

    /** Wipes all scores from the file. */
    public void clearAllScores() {
        writeAll(new ArrayList<>());
    }

    // ---------------------------------------------------------------
    // Private helpers
    // ---------------------------------------------------------------

    private List<String[]> readAll() {
        List<String[]> result = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return result;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length >= 2) result.add(parts);
            }
        } catch (IOException e) {
            System.out.println("[FileHandler] Could not read: " + e.getMessage());
        }
        return result;
    }

    private void writeAll(List<String[]> scores) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME, false))) {
            for (String[] entry : scores) pw.println(String.join(",", entry));
        } catch (IOException e) {
            System.out.println("[FileHandler] Could not write: " + e.getMessage());
        }
    }
}

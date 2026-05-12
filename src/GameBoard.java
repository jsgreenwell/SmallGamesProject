import java.io.*;
import java.util.ArrayList;

/**
 * GameBoard.java
 * Stores the game grid and handles wall collision detection.
 * Works on grid coordinates {row, col} matching Snake.java and snakeFood.java.
 * Board settings (size) are saved and loaded from board_settings.txt.
 */

public class GameBoard {

    // Default board size - change these to resize the game.
    public static final int DEFAULT_ROWS = 24;
    public static final int DEFAULT_COLS = 32;


    private int rows;
    private int cols;
    private char[][] grid;
    private ArrayList<String> wallPositions; // LIST data structure

    // Constructor - sets up the board
    public GameBoard(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new char[rows][cols];
        this.wallPositions = new ArrayList<>();
        initializeBoard();
        saveSettings();
    }

    // Fills the grid with walls on edges, empty space inside
    public void initializeBoard() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (r == 0 || r == rows - 1 || c == 0 || c == cols - 1) {
                    grid[r][c] = '#';
                    wallPositions.add(r + "," + c);
                } else {
                    grid[r][c] = '.';
                }
            }
        }
    }

    // Place a symbol (snake, food, etc.) on the board
    public void placeSymbol(int row, int col, char symbol) {
        if (inBounds(row, col)) grid[row][col] = symbol;
    }

    // Clear a cell back to empty
    public void clearCell(int row, int col) {
        if (inBounds(row, col)) grid[row][col] = '.';
    }


    // Check if a position is a wall (for collision detection)
    public boolean isWall(int row, int col) {
        if (!inBounds(row, col)) return true;
        return grid[row][col] == '#';
    }

    // Returns true if the position is within the grid
    public boolean inBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public void saveSettings() {
        try (FileWriter fw = new FileWriter("board_settings.txt")) {
            fw.write("rows=" + rows + "\n");
            fw.write("cols=" + cols + "\n");
        } catch (IOException e) {
            System.out.println("[Board] Could not save settings: " + e.getMessage());
        }
    }

/**
 * Loads board dimensions from board_settings.txt.
 * Returns {DEFAULT_ROWS, DEFAULT_COLS} if file not found.
 */

    public static int[] loadSettings() {
        int[] settings = {DEFAULT_ROWS, DEFAULT_COLS};
        try (BufferedReader br = new BufferedReader(new FileReader("board_settings.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    if (parts[0].equals("rows")) settings[0] = Integer.parseInt(parts[1].trim());
                    if (parts[0].equals("cols")) settings[1] = Integer.parseInt(parts[1].trim());
                }
            }
        } catch (IOException e) {
            System.out.println("[Board] No settings file found. Using " +
                    DEFAULT_ROWS + "x" + DEFAULT_COLS + ".");
        }
        return settings;
    }

    // GETTERS
    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public ArrayList<String> getWalls() { return wallPositions; }
}


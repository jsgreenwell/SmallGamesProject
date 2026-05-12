public class Board {

    private int rows;
    private int cols;
    private char[][] grid;
    private ArrayList<String> wallPositions; // LIST data structure

    // Constructor - sets up the board
    public Board(int rows, int cols) {
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
        grid[row][col] = symbol;
    }

    // Clear a cell back to empty
    public void clearCell(int row, int col) {
        grid[row][col] = '.';
    }

    // Print the board to the console
    public void drawBoard() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                System.out.print(grid[r][c]);
            }
            System.out.println();
        }
    }

    // Check if a position is a wall (for collision detection)
    public boolean isWall(int row, int col) {
        return grid[row][col] == '#';
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }

    // Write board size to a file
    public void saveSettings() {
        try {
            FileWriter writer = new FileWriter("board_settings.txt");
            writer.write("rows=" + rows + "\n");
            writer.write("cols=" + cols + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Could not save settings.");
        }
    }

    // Read board size from a file
    public static int[] loadSettings() {
        int[] settings = {20, 20};
        try {
            BufferedReader reader = new BufferedReader(new FileReader("board_settings.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts[0].equals("rows")) settings[0] = Integer.parseInt(parts[1]);
                if (parts[0].equals("cols")) settings[1] = Integer.parseInt(parts[1]);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("No settings file found. Using 20x20.");
        }
        return settings;
    }

    // Test the board on its own
    public static void main(String[] args) {
        int[] settings = Board.loadSettings();
        Board board = new Board(settings[0], settings[1]);
        board.placeSymbol(5, 5, 'S');
        board.placeSymbol(3, 8, 'F');
        board.drawBoard();
    }
}

import java.util.Random;
import java.util.ArrayList;

/**
 * snakeFood.java
 * Tracks the food position {row, col} grid coords
 * Respawns in a random empty cell when snake eats food.
  */
public class snakeFood {

    private int row;
    private int col;
    private Random rand;

    public snakeFood(int boardRows, int boardCols) {
        rand = new Random();
        respawn(boardRows, boardCols, null);
    }

    /**
     * Moves the food to a new random cell that the snake is not in
     * @param boardRows total rows on the board (not walls)
     * @param boardCols total cols on the board (not walls)
     * @param snakeBody snake segments to avoid = pass null if not needed
     */
    public void respawn(int boardRows, int boardCols, ArrayList<int[]> snakeBody) { //returns everything to the board in different spots
        int newRow, newCol;
        do {
            // Stay inside walls (1 to rows-2, 1 to cols-2)
            newRow = 1 + rand.nextInt(boardRows - 2);
            newCol = 1 + rand.nextInt(boardCols - 2);
        } while (isOnSnake(newRow, newCol, snakeBody));

        row = newRow;
        col = newCol;
    }

    /** @returns true if the given cell has the snake in it.*/
     private boolean isOnSnake(int r, int c, ArrayList<int[]> snakeBody) {
         if (snakeBody == null) return false;
         for (int[] seg : snakeBody) {
             if (seg[0] == r && seg[1] == c) return true;
         }
         return false;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }

}

// for when the snake actually gets close to the banana and eats it, it will disappear and the snake will get longer
//code for when the snake gets bigger
// and then return the snake back to the board

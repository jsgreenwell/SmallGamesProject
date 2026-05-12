import java.util.ArrayList;

/**
 * Snake.java
 * Stores the snake body as a list of {row, col} grid positions
 * Handles movement, growth, and direction changes.
 */
public class Snake {
    //the snake has to move like a array
    private ArrayList<int[]> body;
    private int dirRow;
    //directions of the snakes movements
    private int dirCol;



    public Snake(int startRow, int startCol) {
        body = new ArrayList<>();
        // Start with 3 segments so the snake can be seen as the game starts.
        body.add(new int[]{startRow,     startCol});
        body.add(new int[]{startRow,     startCol - 1});
        body.add(new int[]{startRow,     startCol - 2});
        // Start moving right
        dirRow = 0;
        dirCol = 1;
    }
    /** Moves the snake one step in the current direction. */
    public void move() {
        int[] head = body.get(0);
        int newRow = head[0] + dirRow;
        int newCol = head[1] + dirCol;
        // its important for board size to be declared than you can fix this to fit the snake

        body.add(0, new int[]{newRow, newCol}); //keeps adding the head when eating food
        body.remove(body.size() - 1);
    }

    /** Grows the snake by duplcating the tail segment. */
    public void grow() {
       int[] tail = body.get(body.size() -1);
       body.add(new int[]{tail[0], tail[1]});
    }

    /**
     * Sets the direction. Prevents reversing directly into the body.
     * Called from Display.java on keypress.
     */
  public void setDirection(int dRow, int dCol) {
      // Ignore if trying to reverse
      if (dRow == -dirRow && dCol == -dirCol) return;
      dirRow = dRow;
      dirCol = dCol;
  }

  /** returns the full list of body segments as {row, col} pairs. */
  public ArrayList<int[]> getBody() {
      return body;
  }

  /** Returns the head position as {row, col}. */
  public int[] getHead() {
      return body.get(0);
  }

  public int getDirRow() { return dirRow; }
  public int getDirCol() { return dirCol; }
}
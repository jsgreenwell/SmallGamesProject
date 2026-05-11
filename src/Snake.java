import java.util.ArrayList;

public class Snake {

    private final ArrayList<int[]> body;
    private int snakeSize;
    //the snake has to move like a array
    private int movement1 = 1;
    private int movement2 = 0;  //directions of the snakes movements
    //someone add in wasd keys or arrow keys

    public Snake(int startA, int startD, int snakeSize) {
        this.snakeSize = snakeSize;
        body = new ArrayList<>();

        body.add(new int[]{startA, startD}); //head of the snake
    }

    public void move() {
        int[] head = body.get(0);

        int newX = head[0] + movement1 * snakeSize;
        int newY = head[1] + movement2 * snakeSize;
        // its important for board size to be declared than you can fix this to fit the snake

        body.add(0, new int[]{newX, newY}); //keeps adding the head when eating food

       body.remove(body.size() - 1);
        }


    public void grow() {
       int[] tail = body.get(body.size() -1);
       body.add(new int[]{tail[0], tail[1]});
    }

  public void setDirection(int dx, int dy) {
        this.movement1 = dx;
        this.movement2 = dy;
  }

  public ArrayList<int[]> getBody() {
      return body;
  }
}
// make sure to change the numbers when doing the board height and width
// i left them mainly 0 for right now
// this might be a little wocky it can be fixed once board and game panel is added

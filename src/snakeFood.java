import java.util.Random;

public class snakeFood {

    private int C;
    private int V;
    private final int foodSize;
    private final Random rand;

    public snakeFood(int boardWidth, int boardHeight, int foodSize) { //make sure to add boardWidth and height numbers
        this.foodSize = foodSize; //once those are added this will make more sense
        this.rand = new Random();
        respawn(boardWidth, boardHeight); //make sure to add that
    }

    public void respawn(int boardWidth, int boardHeight) { //returns everything to the board in different spots
        C = rand.nextInt(boardWidth / foodSize) * foodSize;
        V = rand.nextInt(boardHeight / foodSize) * foodSize;
    }

    public int getC() {
        return C;
    }

    public int getV() {
        return V;
    }
}

// for when the snake actauuly gets close to the banana and eats it, it will disappear and the snake will get longer
//code for when the snake gets bigger
// and than return the snake back to the board

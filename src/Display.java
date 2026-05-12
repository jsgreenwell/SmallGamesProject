import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Display.java
 * Full Swing GUI for the Snake game.
 * Works with Snake.java, snakeFood.java, Board.java, and FileHandler.java.
 */
public class Display extends JFrame {

    // ---------------------------------------------------------------
    // Tile size — one grid cell = TILE pixels on screen
    // ---------------------------------------------------------------
    static final int TILE = 24;

    // ---------------------------------------------------------------
    // Colors
    // ---------------------------------------------------------------
    static final Color C_BG          = new Color(13,  17,  13);
    static final Color C_GRID        = new Color(22,  30,  22);
    static final Color C_WALL        = new Color(60,  180, 60);
    static final Color C_WALL_FILL   = new Color(20,  50,  20);
    static final Color C_HEAD        = new Color(0,   255, 80);
    static final Color C_BODY_BRIGHT = new Color(0,   200, 55);
    static final Color C_BODY_DARK   = new Color(0,   90,  25);
    static final Color C_EYE         = new Color(5,   5,   5);
    static final Color C_FOOD        = new Color(255, 55,  55);
    static final Color C_FOOD_SHINE  = new Color(255, 180, 180);
    static final Color C_HUD_BG      = new Color(8,   14,  8);
    static final Color C_HUD_TEXT    = new Color(0,   230, 70);
    static final Color C_HUD_DIM     = new Color(0,   120, 40);
    static final Color C_OVERLAY     = new Color(0,   0,   0,  175);
    static final Color C_TITLE       = new Color(0,   255, 80);
    static final Color C_SUBTITLE    = new Color(160, 240, 160);
    static final Color C_DANGER      = new Color(255, 70,  70);
    static final Color C_GOLD        = new Color(255, 215, 0);

    // ---------------------------------------------------------------
    // Game objects
    // ---------------------------------------------------------------
    private GameBoard       board;
    private Snake       snake;
    private snakeFood   food;
    private FileHandler fileHandler;

    private String    playerName;
    private int       score;
    private int       highScore;
    private int       level;
    private GameState state;
    private Timer     gameTimer;
    private int       baseSpeed  = 160; // ms — lower = faster

    enum GameState { MENU, PLAYING, PAUSED, GAME_OVER }

    // ---------------------------------------------------------------
    // Constructor — builds the window
    // ---------------------------------------------------------------
    public Display(String playerName) {
        this.playerName  = playerName;
        this.fileHandler = new FileHandler();
        this.highScore   = fileHandler.getTopScore();
        this.state       = GameState.MENU;

        // Load board settings from file
        int[] settings = GameBoard.loadSettings();
        board = new GameBoard(settings[0], settings[1]);

        int windowW = board.getCols() * TILE;
        int windowH = board.getRows() * TILE + 44; // +44 for HUD bar

        setTitle("Snake");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GamePanel panel = new GamePanel(windowW, windowH);
        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        panel.requestFocusInWindow();
    }

    // ---------------------------------------------------------------
    // Inner class: GamePanel
    // ---------------------------------------------------------------
    class GamePanel extends JPanel implements ActionListener, KeyListener {

        int pw, ph;

        GamePanel(int w, int h) {
            pw = w; ph = h;
            setPreferredSize(new Dimension(pw, ph));
            setBackground(C_BG);
            setFocusable(true);
            addKeyListener(this);
            gameTimer = new Timer(baseSpeed, this);
        }

        // -----------------------------------------------------------
        // Start / end
        // -----------------------------------------------------------
        void startGame() {
            int midRow = board.getRows() / 2;
            int midCol = board.getCols() / 2;
            snake  = new Snake(midRow, midCol);
            food   = new snakeFood(board.getRows(), board.getCols());
            score  = 0;
            level  = 1;
            state  = GameState.PLAYING;
            gameTimer.setDelay(baseSpeed);
            gameTimer.start();
        }

        void endGame() {
            gameTimer.stop();
            if (score > highScore) highScore = score;
            fileHandler.saveScore(playerName, score, level);
            state = GameState.GAME_OVER;
            repaint();
        }

        // -----------------------------------------------------------
        // Game tick
        // -----------------------------------------------------------
        @Override
        public void actionPerformed(ActionEvent e) {
            if (state != GameState.PLAYING) return;

            snake.move();
            int[] head = snake.getHead();

            // Wall collision
            if (board.isWall(head[0], head[1])) {
                endGame(); return;
            }

            // Self collision
            ArrayList<int[]> body = snake.getBody();
            for (int i = 1; i < body.size(); i++) {
                if (body.get(i)[0] == head[0] && body.get(i)[1] == head[1]) {
                    endGame(); return;
                }
            }

            // Food eaten
            if (head[0] == food.getRow() && head[1] == food.getCol()) {
                snake.grow();
                food.respawn(board.getRows(), board.getCols(), snake.getBody());
                score++;
                // Level up every 5 apples, speed up slightly
                if (score % 5 == 0) {
                    level++;
                    int newDelay = Math.max(55, gameTimer.getDelay() - 12);
                    gameTimer.setDelay(newDelay);
                }
            }

            repaint();
        }

        // -----------------------------------------------------------
        // Paint
        // -----------------------------------------------------------
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            drawHUD(g2);
            drawGrid(g2);
            drawWalls(g2);

            switch (state) {
                case MENU:
                    drawMenuOverlay(g2); break;
                case PLAYING:
                    drawFood(g2); drawSnake(g2); break;
                case PAUSED:
                    drawFood(g2); drawSnake(g2); drawPauseOverlay(g2); break;
                case GAME_OVER:
                    drawFood(g2); drawSnake(g2); drawGameOverOverlay(g2); break;
            }
        }

        // -----------------------------------------------------------
        // HUD bar
        // -----------------------------------------------------------
        void drawHUD(Graphics2D g) {
            g.setColor(C_HUD_BG);
            g.fillRect(0, 0, pw, 44);

            g.setColor(C_WALL);
            g.setStroke(new BasicStroke(2f));
            g.drawLine(0, 43, pw, 43);

            g.setFont(new Font("Courier New", Font.BOLD, 13));

            // Player name
            g.setColor(C_HUD_DIM);
            g.drawString(playerName.toUpperCase(), 14, 27);

            // Score centered
            g.setColor(C_HUD_TEXT);
            String scoreStr = "SCORE: " + score;
            FontMetrics fm = g.getFontMetrics();
            g.drawString(scoreStr, (pw - fm.stringWidth(scoreStr)) / 2, 27);

            // Best + level right side
            g.setColor(C_HUD_DIM);
            String right = "BEST:" + highScore + "  LV:" + level;
            g.drawString(right, pw - fm.stringWidth(right) - 14, 27);
        }

        // -----------------------------------------------------------
        // Grid lines
        // -----------------------------------------------------------
        void drawGrid(Graphics2D g) {
            g.setColor(C_GRID);
            g.setStroke(new BasicStroke(0.5f));
            for (int c = 0; c <= board.getCols(); c++)
                g.drawLine(c * TILE, 44, c * TILE, ph);
            for (int r = 0; r <= board.getRows(); r++)
                g.drawLine(0, 44 + r * TILE, pw, 44 + r * TILE);
        }

        // -----------------------------------------------------------
        // Walls
        // -----------------------------------------------------------
        void drawWalls(Graphics2D g) {
            for (int r = 0; r < board.getRows(); r++) {
                for (int c = 0; c < board.getCols(); c++) {
                    if (board.isWall(r, c)) {
                        int px = c * TILE;
                        int py = 44 + r * TILE;
                        g.setColor(C_WALL_FILL);
                        g.fillRect(px, py, TILE, TILE);
                        g.setColor(C_WALL);
                        g.setStroke(new BasicStroke(1f));
                        g.drawRect(px, py, TILE - 1, TILE - 1);
                    }
                }
            }
        }

        // -----------------------------------------------------------
        // Food
        // -----------------------------------------------------------
        void drawFood(Graphics2D g) {
            int px = food.getCol() * TILE;
            int py = 44 + food.getRow() * TILE;

            // Glow effect
            g.setColor(new Color(255, 55, 55, 40));
            g.fillRoundRect(px - 3, py - 3, TILE + 6, TILE + 6, 12, 12);

            // Apple body
            g.setColor(C_FOOD);
            g.fillRoundRect(px + 3, py + 3, TILE - 6, TILE - 6, 7, 7);

            // Shine
            g.setColor(C_FOOD_SHINE);
            g.fillOval(px + 6, py + 6, 4, 4);

            // Stem
            g.setColor(new Color(90, 55, 15));
            g.setStroke(new BasicStroke(1.5f));
            g.drawLine(px + TILE / 2, py + 3, px + TILE / 2 + 2, py);
        }

        // -----------------------------------------------------------
        // Snake
        // -----------------------------------------------------------
        void drawSnake(Graphics2D g) {
            ArrayList<int[]> body = snake.getBody();

            for (int i = body.size() - 1; i >= 0; i--) {
                int px = body.get(i)[1] * TILE;        // col → x
                int py = 44 + body.get(i)[0] * TILE;  // row → y

                if (i == 0) {
                    // Head
                    g.setColor(C_HEAD);
                    g.fillRoundRect(px + 1, py + 1, TILE - 2, TILE - 2, 8, 8);
                    drawEyes(g, px, py);
                } else {
                    // Body — fades from bright to dark toward tail
                    float t     = (float) i / body.size();
                    int   green = (int) (200 * (1 - t * 0.65f));
                    int   blue  = (int) (25  * (1 - t));
                    g.setColor(new Color(0, Math.max(green, 50), Math.max(blue, 10)));
                    g.fillRoundRect(px + 2, py + 2, TILE - 4, TILE - 4, 6, 6);

                    // Segment outline
                    g.setColor(new Color(0, 70, 15));
                    g.setStroke(new BasicStroke(1f));
                    g.drawRoundRect(px + 2, py + 2, TILE - 4, TILE - 4, 6, 6);
                }
            }
        }

        /** Draws eyes on the head facing the direction of travel. */
        void drawEyes(Graphics2D g, int px, int py) {
            g.setColor(C_EYE);
            int s  = 4;
            int dr = snake.getDirRow();
            int dc = snake.getDirCol();

            if      (dc > 0)  { g.fillOval(px+TILE-8, py+5,  s,s); g.fillOval(px+TILE-8, py+13, s,s); } // right
            else if (dc < 0)  { g.fillOval(px+4,      py+5,  s,s); g.fillOval(px+4,      py+13, s,s); } // left
            else if (dr > 0)  { g.fillOval(px+5, py+TILE-8,  s,s); g.fillOval(px+13,py+TILE-8,  s,s); } // down
            else              { g.fillOval(px+5,      py+4,  s,s); g.fillOval(px+13,     py+4,  s,s); } // up
        }

        // -----------------------------------------------------------
        // Overlays
        // -----------------------------------------------------------
        void drawMenuOverlay(Graphics2D g) {
            g.setColor(C_OVERLAY);
            g.fillRect(0, 44, pw, ph - 44);

            // Title
            g.setFont(new Font("Courier New", Font.BOLD, 50));
            g.setColor(C_TITLE);
            drawCentered(g, "SNAKE", ph / 2 - 40);

            // Little ASCII snake
            g.setFont(new Font("Courier New", Font.PLAIN, 13));
            g.setColor(C_SUBTITLE);
            drawCentered(g, "~  o  o  o  o  O", ph / 2);

            // Instructions
            g.setFont(new Font("Courier New", Font.PLAIN, 15));
            drawCentered(g, "Press  ENTER  to Start", ph / 2 + 36);

            g.setFont(new Font("Courier New", Font.PLAIN, 12));
            g.setColor(new Color(90, 170, 90));
            drawCentered(g, "W A S D  or  Arrow Keys to move", ph / 2 + 62);
            drawCentered(g, "P = Pause          Q = Quit",      ph / 2 + 82);
        }

        void drawPauseOverlay(Graphics2D g) {
            g.setColor(C_OVERLAY);
            g.fillRect(0, 44, pw, ph - 44);

            g.setFont(new Font("Courier New", Font.BOLD, 38));
            g.setColor(C_TITLE);
            drawCentered(g, "PAUSED", ph / 2);

            g.setFont(new Font("Courier New", Font.PLAIN, 14));
            g.setColor(C_SUBTITLE);
            drawCentered(g, "Press P to Resume", ph / 2 + 34);
        }

        void drawGameOverOverlay(Graphics2D g) {
            g.setColor(C_OVERLAY);
            g.fillRect(0, 44, pw, ph - 44);

            g.setFont(new Font("Courier New", Font.BOLD, 38));
            g.setColor(C_DANGER);
            drawCentered(g, "GAME  OVER", ph / 2 - 44);

            g.setFont(new Font("Courier New", Font.BOLD, 20));
            g.setColor(C_HUD_TEXT);
            drawCentered(g, "Score: " + score, ph / 2 + 4);

            // New high score flash
            if (score > 0 && score == highScore) {
                g.setFont(new Font("Courier New", Font.BOLD, 14));
                g.setColor(C_GOLD);
                drawCentered(g, "★  NEW HIGH SCORE!  ★", ph / 2 + 28);
            }

            g.setFont(new Font("Courier New", Font.PLAIN, 13));
            g.setColor(C_SUBTITLE);
            drawCentered(g, "ENTER = Play Again     Q = Quit", ph / 2 + 58);
        }

        // -----------------------------------------------------------
        // Utility
        // -----------------------------------------------------------
        void drawCentered(Graphics2D g, String text, int y) {
            FontMetrics fm = g.getFontMetrics();
            g.drawString(text, (pw - fm.stringWidth(text)) / 2, y);
        }

        // -----------------------------------------------------------
        // Keys
        // -----------------------------------------------------------
        @Override
        public void keyPressed(KeyEvent e) {
            int k = e.getKeyCode();

            if (state == GameState.MENU && k == KeyEvent.VK_ENTER) {
                startGame(); return;
            }
            if (state == GameState.GAME_OVER) {
                if (k == KeyEvent.VK_ENTER) startGame();
                if (k == KeyEvent.VK_Q)     System.exit(0);
                return;
            }
            if (k == KeyEvent.VK_P) {
                if (state == GameState.PLAYING) {
                    state = GameState.PAUSED; gameTimer.stop();
                } else if (state == GameState.PAUSED) {
                    state = GameState.PLAYING; gameTimer.start();
                }
                repaint(); return;
            }
            if (k == KeyEvent.VK_Q) System.exit(0);
            if (state != GameState.PLAYING) return;

            if      (k == KeyEvent.VK_W || k == KeyEvent.VK_UP)    snake.setDirection(-1,  0);
            else if (k == KeyEvent.VK_S || k == KeyEvent.VK_DOWN)  snake.setDirection( 1,  0);
            else if (k == KeyEvent.VK_A || k == KeyEvent.VK_LEFT)  snake.setDirection( 0, -1);
            else if (k == KeyEvent.VK_D || k == KeyEvent.VK_RIGHT) snake.setDirection( 0,  1);
        }

        @Override public void keyReleased(KeyEvent e) {}
        @Override public void keyTyped(KeyEvent e)    {}
    }
}

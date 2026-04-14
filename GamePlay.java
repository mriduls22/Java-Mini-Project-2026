import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class GamePlay extends JPanel implements KeyListener, ActionListener {

    private boolean play = false;
    private boolean start = false;
    private boolean paused = false;

    private int score = 0;
    private int highScore = 0;
    private int lives = 3;
    private int level = 1;

    private int totalBricks;

    private Timer timer;
    private int delay = 8;

    private int playerX = 310;

    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballXdir;
    private int ballYdir;

    private boolean lifeLost = false;

    private MapGenerator map;
    private Random rand = new Random();

    public GamePlay() {
        newLevel();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
        requestFocusInWindow();
    }

    private void newLevel() {
        int rows = 3 + level;
        int cols = 7 + level;

        map = new MapGenerator(rows, cols);
        totalBricks = rows * cols;

        ballPosX = 120;
        ballPosY = 350;

        ballXdir = rand.nextBoolean() ? -2 - level : 2 + level;
        ballYdir = -2 - level;

        lifeLost = false;
    }

    public void paint(Graphics g) {

        // START SCREEN
        if (!start) {
            g.setColor(Color.black);
            g.fillRect(1, 1, 692, 592);

            g.setColor(Color.white);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("BRICK BREAKER", 180, 250);

            g.setFont(new Font("serif", Font.PLAIN, 20));
            g.drawString("Press ENTER to Start", 220, 300);
            return;
        }

        // BACKGROUND
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        map.draw((Graphics2D) g);

        // BORDERS
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // INFO
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 20));
        g.drawString("Score: " + score, 500, 30);
        g.drawString("Lives: " + lives, 20, 30);
        g.drawString("Level: " + level, 300, 30);

        // PADDLE
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        // BALL
        g.setColor(Color.yellow);
        g.fillOval(ballPosX, ballPosY, 20, 20);

        // GAME LOGIC
        if (play) {

            // WIN
            if (totalBricks <= 0) {
                play = false;
            }

            // LIFE LOSS
            if (ballPosY > 570 && !lifeLost) {
                lifeLost = true;
                lives--;

                if (lives > 0) {
                    newLevel();
                } else {
                    play = false;
                }
            }
        }

        // END SCREEN
        if (!play && start) {

            g.setColor(Color.white);
            g.setFont(new Font("serif", Font.BOLD, 30));

            if (lives <= 0) {
                g.drawString("GAME OVER", 220, 260);
            } else if (totalBricks <= 0) {
                g.drawString("YOU WON!", 230, 260);
            }

            g.setFont(new Font("serif", Font.PLAIN, 20));
            g.drawString("Press ENTER to Restart", 200, 300);
        }

        g.dispose();
    }

    public void actionPerformed(ActionEvent e) {

        if (play && !paused) {

            // PADDLE COLLISION
            if (new Rectangle(ballPosX, ballPosY, 20, 20)
                    .intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYdir = -ballYdir;
            }

            // BRICK COLLISION
            A:
            for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {

                    if (map.map[i][j] > 0) {

                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;

                        Rectangle rect = new Rectangle(brickX, brickY,
                                map.brickWidth, map.brickHeight);

                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);

                        if (ballRect.intersects(rect)) {

                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (score > highScore) {
                                highScore = score;
                            }

                            Toolkit.getDefaultToolkit().beep();

                            ballYdir = -ballYdir;
                            break A;
                        }
                    }
                }
            }

            ballPosX += ballXdir;
            ballPosY += ballYdir;

            // WALL COLLISION
            if (ballPosX < 0 || ballPosX > 670) {
                ballXdir = -ballXdir;
            }
            if (ballPosY < 0) {
                ballYdir = -ballYdir;
            }
        }

        repaint();
    }

    public void keyPressed(KeyEvent e) {

        // START / RESTART
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!start) {
                start = true;
                play = true;
            } else if (!play) {
                score = 0;
                lives = 3;
                level = 1;
                newLevel();
                play = true;
            }
        }

        // PAUSE
        if (e.getKeyCode() == KeyEvent.VK_P) {
            paused = !paused;
        }

        // MOVE RIGHT
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && playerX < 600) {
            playerX += 25;
            play = true;
        }

        // MOVE LEFT
        if (e.getKeyCode() == KeyEvent.VK_LEFT && playerX > 10) {
            playerX -= 25;
            play = true;
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}

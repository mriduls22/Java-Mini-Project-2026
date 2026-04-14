import java.awt.*;
import java.util.Random;

public class MapGenerator {

    public int map[][];
    public int brickWidth;
    public int brickHeight;

    public MapGenerator(int row, int col) {
        map = new int[row][col];
        Random rand = new Random();

        // generate random patterns
        int type = rand.nextInt(3);

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {

                if (type == 0) {
                    map[i][j] = 1; // full
                } else if (type == 1) {
                    map[i][j] = (i % 2 == 0) ? 1 : 0; // horizontal stripes
                } else {
                    map[i][j] = (j % 2 == 0) ? 1 : 0; // vertical stripes
                }
            }
        }

        brickWidth = 540 / col;
        brickHeight = 150 / row;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {

                if (map[i][j] > 0) {

                    if (i % 3 == 0)
                        g.setColor(Color.red);
                    else if (i % 3 == 1)
                        g.setColor(Color.orange);
                    else
                        g.setColor(Color.green);

                    g.fillRect(j * brickWidth + 80,
                               i * brickHeight + 50,
                               brickWidth,
                               brickHeight);

                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.black);
                    g.drawRect(j * brickWidth + 80,
                               i * brickHeight + 50,
                               brickWidth,
                               brickHeight);
                }
            }
        }
    }

    public void setBrickValue(int value, int row, int col) {
        map[row][col] = value;
    }
}

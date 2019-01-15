// Tile
// Harish Parthasarathy
// 2018-01-10
// ICS4U1-02
// Ms. Strelkovska

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile extends Rectangle {

    int locX;
    int locY;

    boolean wall;

    public Tile (int x,int y) {
        wall = false;

        this.locX = x;
        this.locY = y;

        this.x = x;
        this.y = y;

        this.width = 50;
        this.height = 50;
    }

    public Tile (int x, int y, String path, boolean wall) {
        this(x,y);
        this.wall = wall;
    }
}

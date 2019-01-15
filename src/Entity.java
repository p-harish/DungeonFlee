// entity
// Harish Parthasarathy
// 2018-01-11
// ICS4U1-02
// Ms. Strelkovska

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity extends Rectangle {

    public BufferedImage sprite;

    protected int speed;
    protected boolean alive =true;

    Entity(int x, int y, int width, int height, int speed) {
        this.x = x + (int)Game.mapX;
        this.y = y + (int)Game.mapY;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }


    protected void updateSize() {
        this.width = sprite.getWidth() * 3;
        this.height = sprite.getHeight() * 3;
    }

}


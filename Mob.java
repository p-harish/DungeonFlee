import java.awt.*;
import java.awt.image.BufferedImage;
public class Mob extends Entity{
    protected boolean flip;
    protected int deltaX, deltaY;
    protected double angle;
    protected int randDir;
    protected int health;
    protected int powerLvl;
    protected int mod;
    protected boolean move = false;
    protected int deltaFrame;
    protected int idleFrame;
    protected int moveFrame;
    protected int totalIdleFrames;
    protected int totalMoveFrames;
    protected BufferedImage[] idle;
    protected BufferedImage[] moveBack;
    protected BufferedImage[] moveFront;
    protected BufferedImage hand;
    protected boolean gun;
    protected Weapon weapon;
    protected double shootAngle = 0;
    Mob(int x, int y, int width, int height, int speed, int health, int powerLvl, int totalIdleFrames, int totalMoveFrames) {
        super(x, y, width, height, speed);
        this.flip = false;
        this.health = health;
        this.powerLvl = powerLvl;
        this.totalIdleFrames = totalIdleFrames;
        this.totalMoveFrames = totalMoveFrames;
        this.deltaFrame = (int)Math.random() * mod;
    }
    public int getPowerLvl() {
        return powerLvl;
    }
    protected void updateLocation() {}
    protected boolean updateCollision() {
        Rectangle roomBox = new Rectangle(Game.room.tileArray[0][0].x + 50, Game.room.tileArray[0][0].y + 50, 1150, 1150);
        updateSize();
        Rectangle monBox = new Rectangle(x + deltaX, y + deltaY, width, height);
        if(roomBox.contains(monBox)) {
            deltaX += (int) (Game.mapX);
            deltaY += (int) (Game.mapY);
            x += deltaX;
            y += deltaY;
            deltaX = 0;
            deltaY = 0;
            return true;
        }
        x += (int) (Game.mapX);
        y += (int) (Game.mapY);
        deltaX = 0;
        deltaY = 0;
        return false;
    }
    protected void updateSprite() {
        if (((Game.fps + deltaFrame) % mod) == 0) {
            if (Math.cos(angle) < 0)
                flip = true;
            else
                flip = false;
            if (move) {
                if (Math.sin(angle) < 0)
                    sprite = moveBack[moveFrame];
                else
                    sprite = moveFront[moveFrame];
                moveFrame++;
                if (moveFrame >= totalMoveFrames)
                    moveFrame = 0;
            }
            else {
                sprite = idle[idleFrame];
                idleFrame++;
                if (idleFrame >= totalIdleFrames) {
                    idleFrame = 0;
                }
            }
        }
    }
}

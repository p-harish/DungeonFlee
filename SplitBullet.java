import java.awt.image.BufferedImage;
public class SplitBullet extends Bullet {
    private int numSplits;
    public SplitBullet(int x, int y, int speed, double angle,int numSplits, BufferedImage sprite) {
        super(x, y, speed, angle, sprite);
        this.numSplits = numSplits;
    }
    public void updateLocation() {
        super.updateLocation();
        //Base case (last time bullets split)
        if (numSplits == 1 && Game.fps % 60 == 0) {
            Game.room.bullets.add(new Bullet(x,y,speed,angle+Math.PI/6, sprite));
            Game.room.bullets.add(new Bullet(x,y,speed,angle-Math.PI/6, sprite));
            alive = false;
        }

        //Bullets recursively create more of themselves, branching off at different angles a certain number of times (numSplits variable)
        else if (Game.fps % 60 == 0) {
            Game.room.bullets.add(new SplitBullet(x,y,speed,angle+Math.PI/6,numSplits-1,sprite));
            Game.room.bullets.add(new SplitBullet(x,y,speed,angle-Math.PI/6,numSplits-1, sprite));
            //The bullet splits into two new bullets with new angles, so the original bullet must disappear
            alive = false;
        }
    }
}

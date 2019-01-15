import java.awt.image.BufferedImage;
import java.util.ArrayList;
public class Bullet extends Entity{
    double angle;
    double fX,fY;
    boolean alive;
    int hitFrame = 0;
    BufferedImage[] hit;
    public Bullet(int x, int y, int speed, double angle, BufferedImage sprite) {
        super(x, y, 1, 1, speed);
        alive = true;
        hit = ImageLoader.arrayLoad("pistol/knav3_impact_vertical_00", 7);
        this.sprite = sprite;
        super.height = sprite.getHeight() * 3;
        super.width = sprite.getWidth() * 3;
        fX = x;
        fY = y;
        this.angle = angle;
    }
    public void updateLocation() {

        //Moves the bullet at a set speed in a set direction
        //fX and fY are used because they store doubles which are more accurate, but the actual coordinates must be integers
        fX += speed*(Math.cos(angle)) + (int)Game.mapX;
        fY += speed*(Math.sin(angle)) + (int)Game.mapY;
        x = (int)fX;
        y = (int)fY;
    }
    public boolean collision(Room room, ArrayList<Bullet> bullets, int i, boolean player) {
        for (int j = 0; j < 25; j++) {
            for (int k = 0; k < 25; k++) {
                if (room.tileArray[j][k].wall && bullets.get(i).intersects(room.tileArray[j][k])) {
                    //if a bullet collides with a wall, it "dies", and is removed from the bullet array. A "hit" animation plays
                    bullets.get(i).alive = false;
                    if (player)
                        room.hits.add(bullets.get(i));
                    bullets.remove(i);
                    i--;
                    return true;
                }
            }
        }
        return false;
    }
}

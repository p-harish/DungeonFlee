import java.awt.image.BufferedImage;

public class Fireball extends Bullet{
    int timer;



    public Fireball (int x, int y, int speed, double angle, BufferedImage sprite) {
        super(x,y,speed, angle, sprite);
        alive = true;
        updateSize();
        timer = 0;
    }

    public void updateLocation() {

        x+= Game.mapX;
        y+= Game.mapY;

        timer++;
        if (timer == 60) {
            angle = Math.atan2(Player.y - y,Player.x - x);
            alive = false;
            Game.room.enemyBul.remove(this);
            Game.room.enemyBul.add(new Bullet(x + width / 2 + (int) (width / 2 * Math.cos(angle)), y + height / 2 + (int) (height / 2 * Math.sin(angle)), 8, angle, sprite));
        }

    }

    protected void updateSize() {
        this.width = sprite.getWidth() * 4;
        this.height = sprite.getHeight() * 4;
    }
}

import java.awt.*;
import java.awt.image.BufferedImage;
public class Boss extends Mob {
    private int shootPattern;
    private static int speed = 2;
    double shootAngle;
    private int start;
    private BufferedImage bulletImg;
    private BufferedImage[] attack;
    private int attackFrame;
    public Boss (int x, int y) {
        super(x, y, 200, 200, 3, 25, 1000, 2, 5);
        bulletImg =  ImageLoader.load("Monsters/5x5_enemy_projectile_001_dark.png");
        idle = ImageLoader.arrayLoad("Boss/bullet_king_idle_00", 2);
        attack = ImageLoader.arrayLoad("Boss/bullet_king_pound_00", 5);
        attackFrame = 0;
        idleFrame = 0;
        mod = 12;
        sprite = idle[idleFrame];
        updateSize();
    }
    public void updateLocation() {
        //Angle between the player and the boss
        angle = Math.atan2(Player.y - y,Player.x - x);
        //Randomly selects one of three shooting patterns (machine gun fire, splitting bullets, and spray)
        if (Game.fps % 150 == 0)
            shootPattern = (int)(Math.random()*3);

        //Angle between the spot from which bullets are fired from and the player
        shootAngle = Math.atan2(Player.y + Player.height/2 - (y + height/2 + (int)(height/2*Math.sin(angle))),Player.x + Player.width/2 - (x + width/2 + (int)(width/2*Math.cos(angle))));
        //Machine gun fire shooting pattern
        if (shootPattern == 0)
            Game.room.enemyBul.add(new Bullet(x - 20 + width/2 + (int)(width/2*Math.cos(angle)),y - 20 + height/2 + (int)(height/2*Math.sin(angle)),10,shootAngle, bulletImg));
            //Splitting bullets shooting pattern
        else if (shootPattern == 1 && Game.fps % 50 == 0)
            Game.room.enemyBul.add(new SplitBullet(x - 20 + width/2 + (int)(width/2*Math.cos(angle)),y - 20 + height/2 + (int)(height/2*Math.sin(angle)),4,shootAngle,2, bulletImg));
            //Spray shooting pattern
        else if (shootPattern == 2 && Game.fps % 40 == 0){
            start = Game.fps;
            //The angle is offset by a random amount each time to make the bullets harder to dodge
            double rand = Math.random();
            for (int i = 0; i < 12;i++)
                Game.room.enemyBul.add(new Bullet(x - 20 + width/2,y - 20+ height/2,4,Math.PI/6*i+rand, bulletImg));

            //Choosing a random direction to walk in
            if (Game.fps % 20 == 0)
                randDir = (int)(Math.random()*6);
            if (randDir == 1)
                angle += Math.PI / 2;
            else if (randDir == 2)
                angle -= Math.PI / 2;
            //Change in the location of the boss
            deltaX = (int)(speed*(Math.cos(angle)));
            deltaY = (int)(speed*(Math.sin(angle)));
        }
        updateSprite();
        updateCollision();
    }
    protected void updateSprite() {
        if (shootPattern == 2) {
            if ((Game.fps - start)% 12 ==0) {
                sprite = attack[attackFrame];
                attackFrame++;
                if (attackFrame >= 5)
                    attackFrame = 0;
            }
        }
        else if (Game.fps % 30 == 0){
            sprite = idle[idleFrame];
            idleFrame++;
            if (idleFrame >= 2) {
                idleFrame = 0;
            }
        }
    }
    protected void updateSize() {
        width = sprite.getWidth() * 5;
        height = sprite.getHeight() * 5;
    }
}

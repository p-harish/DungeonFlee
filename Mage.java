import java.awt.image.BufferedImage;
public class Mage extends Mob {
    private boolean summoning;
    private BufferedImage summon[];
    private int totalSummonFrames = 0;
    private int summonFrame = 0;
    private BufferedImage bulletImg;
    public Mage (int x, int y) {
        super(x, y, 0, 0, 5, 5, 10, 4, 6);
        totalSummonFrames = 10;
        idle = ImageLoader.arrayLoad("Monsters/Mage/bluewizard_idle_00", totalIdleFrames);
        moveBack = ImageLoader.arrayLoad("Monsters/Mage/bluewizard_move_back_00", totalMoveFrames);
        moveFront = ImageLoader.arrayLoad("Monsters/Mage/bluewizard_move_00", totalMoveFrames);
        summon = ImageLoader.arrayLoad("Monsters/Mage/bluewizard_summon_00", totalSummonFrames);
        bulletImg =  ImageLoader.load("Monsters/5x5_enemy_projectile_001_dark.png");
        mod = 10;
        sprite = idle[idleFrame];
        updateSize();
    }
    public void updateLocation() {

        //Distance between the mage and the player
        int dist = (int)Math.sqrt(Math.pow(Player.y-y,2) + Math.pow(Player.x-x,2));
        //Angle between the mage and the player
        angle = Math.atan2(Player.y - y,Player.x - x);
        //Angle between the spot from which the fireball is placed and the player
        shootAngle = Math.atan2(Player.y + Player.height/2 - (y + height/2 + (int)(height/2*Math.sin(angle))),Player.x + Player.width/2 - (x + width/2 + (int)(width/2*Math.cos(angle))));
        //Randomly choosing whether or not to make a fireball
        randDir = (int)Math.random() * 5;
        if ((Game.fps+deltaFrame) % 50 == 0 && randDir < 1)
            Game.room.enemyBul.add(new Fireball(x + width / 2 + (int) (width / 2 * Math.cos(angle)), y + height / 2 + (int) (height / 2 * Math.sin(angle)), 8, shootAngle, bulletImg));
        //if not making a fireball, move away from the player when in range, and towards the player when out of range
        if (randDir >= 1) {
            if (dist < 400) {
                deltaX = (int) (-speed * (Math.cos(angle)));
                deltaY = (int) (-speed * (Math.sin(angle)));
                move = true;
            } else {
                deltaX = (int) (speed * (Math.cos(angle)));
                deltaY = (int) (speed * (Math.sin(angle)));
            }
        }
        updateSprite();
        updateCollision();
    }
    protected void updateSprite() {
        if (summoning) {
            if (((Game.fps + deltaFrame) % mod) == 0) {
                if (Math.cos(angle) < 0)
                    flip = true;
                else
                    flip = false;
                sprite = summon[summonFrame];
                summonFrame++;
                if (summonFrame >= totalSummonFrames) {
                    summonFrame = 0;
                    summoning = false;
                }
            }
        }
        else
            super.updateSprite();
    }
}

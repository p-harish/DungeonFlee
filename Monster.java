import java.awt.*;
import java.awt.font.GlyphMetrics;
import java.awt.image.BufferedImage;

public class Monster extends Mob {


    public Monster (int x, int y) {
        super(x, y, 0, 0, 5, 2, 3, 4, 6);

        idle = ImageLoader.arrayLoad("Monsters/blob/blobuloid_idle_front_00", totalIdleFrames);
        moveBack = ImageLoader.arrayLoad("Monsters/blob/blobuloid_move_right_back_00", totalMoveFrames);
        moveFront = ImageLoader.arrayLoad("Monsters/blob/blobuloid_move_right_front_00", totalMoveFrames);

        mod = 10;
        gun = false;

        sprite = idle[idleFrame];

        updateSize();
    }

    public void updateLocation() {

        int dist = (int) Math.sqrt(Math.pow(Player.y + Player.height / 2 - y - height / 2, 2) + Math.pow(Player.x + Player.width / 2 - x - width / 2, 2));

        if (Game.fps % 20 == 0) {
            randDir = (int)(Math.random()*6);
        }

        if (dist < 500) {

            angle = Math.atan2(Player.y + Player.height / 2 - y - height / 2, Player.x + Player.width / 2 - x - width / 2);

            if (randDir == 1)
                angle += Math.PI / 2;
            else if (randDir == 2)
                angle -= Math.PI / 2;

            deltaX = (int) (speed * (Math.cos(angle)));
            deltaY = (int) (speed * (Math.sin(angle)));

            move = true;

        }

        updateSprite();
        updateCollision();
    }

}

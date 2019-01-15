public class Gunner extends Mob {
    public Gunner (int x, int y) {
        super(x, y, 0, 0, 2, 3, 5, 2, 6);
        idle = ImageLoader.arrayLoad("Monsters/bullet/bullet_run_right_back_00", totalIdleFrames);
        moveBack = ImageLoader.arrayLoad("Monsters/bullet/bullet_run_right_back_00", totalMoveFrames);
        moveFront = ImageLoader.arrayLoad("Monsters/bullet/bullet_run_right_00", totalMoveFrames);
        hand = ImageLoader.load("Monsters/bullet/bullet_hand_001.png");
        mod = 10;
        gun = true;
        sprite = idle[idleFrame];
        weapon = new enemyPistol();
        updateSize();
    }
    public void updateLocation() {

        //Choosing a random direction to walk in
        if (Game.fps % 20 == 0)
            randDir = (int)(Math.random()*2);

        //The distance between the player and the gunner
        int dist = (int)Math.sqrt(Math.pow(Player.y-y,2) + Math.pow(Player.x-x,2));
        //The angle between the player and the gunner
        angle = Math.atan2(Player.y - y,Player.x - x);
        //The angle between the spot from which the gunner shoots and the player
        shootAngle = Math.atan2(Player.y + Player.height/2 - (y + height/2 + (int)(height/2*Math.sin(angle))),Player.x + Player.width/2 - (x + width/2 + (int)(width/2*Math.cos(angle))));
        //If the gunner is within its range, it will move away from the player, shoot, and strafe
        if (dist < 350) {
            if (randDir == 1)
                angle += Math.PI/2;
            else if (randDir == 2)
                angle -= Math.PI/2;
            deltaX = (int)(-speed*(Math.cos(angle)));
            deltaY = (int)(-speed*(Math.sin(angle)));
            if (Game.fps % 40 == 0) {
                Game.room.enemyBul.add(weapon.createNewBullet(x + width / 2 + (int) (width/2 * Math.cos(angle)), y + height/2 + (int) (height/2 * Math.sin(angle)), 8, shootAngle));
                weapon.firing = true;
            }
            move = true;
        }
        else {
            deltaX = (int) (speed * (Math.cos(angle)));
            deltaY = (int) (speed * (Math.sin(angle)));
        }
        updateSprite();
        updateCollision();
        weapon.weaponUpdate();
    }
}

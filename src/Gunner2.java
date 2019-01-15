public class Gunner2 extends Gunner {
    public Gunner2 (int x, int y) {
        super(x, y);
        this.powerLvl = 7;
        weapon = new enemyRifle();
    }
    public void updateLocation() {

        //Distance between the player and the gunner
        int dist = (int)Math.sqrt(Math.pow(Player.y-y,2) + Math.pow(Player.x-x,2));
        //Angle between the player and the gunner
        angle = Math.atan2(Player.y - y,Player.x - x);
        //Angle between the spot from which bullets are created and the player
        shootAngle = Math.atan2(Player.y + Player.height/2 - (y + height/2 + (int)(height/2*Math.sin(angle))),Player.x + Player.width/2 - (x + width/2 + (int)(width/2*Math.cos(angle))));

        //Choosing a random direction
        //the number is large because the direction also controls the shooting and this gunner
        //shoots very rapidly so it was too difficult if he shot more often
        if (Game.fps % 40 == 0)
            randDir = (int)(Math.random()*10);

        //If the gunner is inside his range, he will strafe, or move away from the player, and shoot a burst every once in a while
        if (dist < 400) {
            if (randDir == 0)
                angle += Math.PI/2;
            else if (randDir == 1)
                angle -= Math.PI/2;
            deltaX = (int)(-speed*(Math.cos(angle)));
            deltaY = (int)(-speed*(Math.sin(angle)));
            if (Game.fps % 5 == 0 && randDir < 2) {
                Game.room.enemyBul.add(weapon.createNewBullet(x + width / 2 + (int) (width / 2 * Math.cos(angle)), y + height / 2 + (int) (height / 2 * Math.sin(angle)), 8, shootAngle));
                weapon.firing = true;
            }
            move = true;
        }

        //If the gunner is outside his range, he will move into range
        else {
            deltaX = (int) (speed * (Math.cos(angle)));
            deltaY = (int) (speed * (Math.sin(angle)));
        }
        updateSprite();
        updateCollision();
        weapon.weaponUpdate();
    }
}

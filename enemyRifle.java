public class enemyRifle extends Weapon {

    //Rifle for enemy
    enemyRifle() {
        //Loading images, initialize sprite, and scale
        idle = ImageLoader.load("Monsters/enemyRifle/AK-47_001.png");
        fire = ImageLoader.arrayLoad("Monsters/enemyRifle/ak-47_shoot_00", 2);
        bulletImg = ImageLoader.load("Monsters/5x5_enemy_projectile_001_dark.png");


        sprite = idle;
        width = sprite.getWidth() * 3;
        height = sprite.getHeight() * 3;
    }

    public void weaponUpdate() {
        if (firing) {
            // update Sprite when firing
            sprite = fire[fireFrame];
            fireFrame++;
            if (fireFrame == 2) {
                fireFrame = 0;
                firing = false;
            }
        }
    }
}



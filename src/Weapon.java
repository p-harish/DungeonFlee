// Weapon
// Harish Parthasarathy
// 2018-01-11
// ICS4U1-02
// Ms. Strelkovska

import javafx.scene.transform.Affine;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Weapon {
    public BufferedImage sprite;

    public int width;
    public int height;
    public BufferedImage idle;
    public BufferedImage fire[];
    public BufferedImage reload;

    public int fireFrame = 0;

    public boolean firing;

    int reloadFrame = 0;

    public int maxAmmo = 12;
    public int ammo;

    public BufferedImage bulletImg = ImageLoader.load("pistol/knav3_bolt_001.png");

    Weapon() {
        idle = ImageLoader.load("pistol\\knav3_idle_001.png");
        fire = ImageLoader.arrayLoad("pistol\\knav3_fire_00", 5);
        reload = ImageLoader.load("pistol\\knav3_reload_001.png");

        sprite = idle;


        ammo = maxAmmo;
        width = sprite.getWidth() * 3;
        height = sprite.getHeight() * 3;
    }

    public synchronized void weaponUpdate() {
        if (Game.reloading) {
            if (ammo == maxAmmo)
                Game.reloading = false;
            else {
                sprite = reload;
                reloadFrame++;
                if (reloadFrame >= 70) {
                    reloadFrame = 0;
                    sprite = idle;
                    Game.reloading = false;
                    ammo = 12;
                }
            }
        }
        else if (firing) {
            sprite = fire[fireFrame];
            if (Game.fps % 5 == 0)
                fireFrame++;
            if (fireFrame == 5){
                fireFrame = 0;
                firing = false;
            }
        }
    }

    public Bullet createNewBullet(int x, int y, int speed, double angle) {
        return new Bullet(x,y,speed,angle, bulletImg);
    }
}

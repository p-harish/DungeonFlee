// Player
// Harish Parthasarathy
// 2018-01-10
// ICS4U1-02
// Ms. Strelkovska

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;

public class Player extends Rectangle {



    public static double health = 6;

    private static int idleFrame;
    private static int moveFrame;

    public static boolean flip = false;
    public static boolean range = false;


    public static boolean goLeft, goRight, goUp, goDown;
    public static boolean colLeft, colRight, colUp, colDown;

    public static int speed = 6;
    private static double diag = Math.sqrt(2);


    public static boolean immune = false;
    public static int counter = 0;

    public static BufferedImage hand = ImageLoader.load("SpaceRogue_Swap/rogue_hand_001.png");

    private static BufferedImage[] idleBackSide = ImageLoader.arrayLoad("SpaceRogue_Swap/pilot_0h_idle_back_right_00", 4);
    private static BufferedImage[] idleBack = ImageLoader.arrayLoad("SpaceRogue_Swap/pilot_idle_1h_back_00", 4);
    private static BufferedImage[] idleFront = ImageLoader.arrayLoad("SpaceRogue_Swap/pilot_1h_idle_front_00", 4);
    private static BufferedImage[] idleSide = ImageLoader.arrayLoad("SpaceRogue_Swap/pilot_1h_idle_right_00", 4);

    private static BufferedImage[] moveBackSide = ImageLoader.arrayLoad("SpaceRogue_Swap/pilot_0h_run_back_right_00", 6);
    private static BufferedImage[] moveBack = ImageLoader.arrayLoad("SpaceRogue_Swap/pilot_1h_move_back_00", 6);
    private static BufferedImage[] moveFront = ImageLoader.arrayLoad("SpaceRogue_Swap/pilot_1h_move_front_00", 6);
    private static BufferedImage[] moveSide = ImageLoader.arrayLoad("SpaceRogue_Swap/pilot_1h_move_right_00", 6);

    public static BufferedImage sprite = moveBack[moveFrame];

    public static int width = sprite.getWidth() * 3;
    public static int height = sprite.getHeight() * 3;
    public static int x = Main.fWidth/2 - width/2;
    public static int y = Main.fHeight/2 - height/2;

    public static Rectangle colBox = new Rectangle(x, y, width, height);

    public static synchronized void updateSprite() {

        if(goDown||goRight||goLeft||goUp) {
            spriteChangeMouse(moveBack, moveBackSide, moveFront, moveSide, moveFrame);
            if (Game.fps % 6 == 0) {
                moveFrame++;
                if(moveFrame > 5){
                    moveFrame = 0;
                }
            }
        }

        else {
            spriteChangeMouse(idleBack, idleBackSide, idleFront, idleSide, idleFrame);
            if (Game.fps % 10 == 0) {
                idleFrame++;
                if(idleFrame > 3){
                    idleFrame = 0;
                }
            }
        }

    }

    public static void spriteChangeMouse(BufferedImage[] back, BufferedImage[]backSide, BufferedImage[]front, BufferedImage[] frontSide, int frame) {
        if (Main.mouseY < Main.fCenterY) {
            if (Main.mouseX < Main.fWidth/3) {
                sprite = backSide[frame];
                flip = true;
            }
            else if (Main.mouseX < Main.fWidth/3 + Main.fWidth/3) {
                sprite = back[frame];
                if (Main.mouseX < Main.fCenterX)
                    flip = true;
                else
                    flip = false;
            }
            else {
                sprite = backSide[frame];
                flip = false;
            }
        }
        else {
            if (Main.mouseX < Main.fWidth * 0.4) {
                sprite = frontSide[frame];
                flip = true;
            } else if (Main.mouseX < Main.fWidth * 0.6) {
                sprite = front[frame];
                if (Main.mouseX < Main.fCenterX)
                    flip = true;
                else
                    flip = false;
            } else {
                sprite = frontSide[frame];
                flip = false;
            }
        }
    }

    public static synchronized void updateLocation() {

        colLeft = false;
        colRight = false;
        colUp = false;
        colDown = false;
        speed = 6;

        if ((goLeft && goUp) || (goLeft && goDown) || (goRight && goUp) || (goRight && goDown))
            speed /= diag;

        collide();
        if (goLeft && !colLeft) {
            Game.mapX += speed;
        }
        if (goRight && !colRight) {
            Game.mapX -= speed;
        }
        if (goUp && !colUp) {
            Game.mapY += speed;
        }
        if (goDown && !colDown) {
            Game.mapY -= speed;
        }

    }

    public static synchronized void update() {
        updateSprite();
        updateLocation();
        if (immune) {
            counter++;
            if (counter == 60) {
                counter = 0;
                immune = false;
            }
        }
    }

    public static synchronized void collide () {

        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                if (Game.room.tileArray[i][j].wall) {
                    Tile t = Game.room.tileArray[i][j];
                    Point p1 = new Point(x-speed-3, y);
                    if (t.contains(p1)) {
                        colLeft = true;
                    }
                    Point p2 = new Point(x-speed-3, y + height);
                    if (t.contains(p2)) {
                        colLeft = true;
                    }

                    Point p3 = new Point(x+width+speed+3, y);
                    if (t.contains(p3)) {
                        colRight = true;
                    }
                    Point p4 = new Point(x+width+speed+3, y + height);
                    if (t.contains(p4)){
                        colRight = true;
                    }

                    Point p5 = new Point(x, y-speed-3);
                    if (t.contains(p5)){
                        colUp = true;
                    }
                    Point p6 = new Point(x, y-speed-3);
                    if (t.contains(p6)){
                        colUp = true;
                    }

                    Point p7 = new Point(x, y+height+speed+3);
                    if (t.contains(p7)){
                        colDown = true;
                    }
                    Point p8 = new Point(x+width, y + height+speed+3);
                    if (t.contains(p8)){
                        colDown = true;
                    }
                }

            }
        }
    }

}

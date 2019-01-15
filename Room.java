// Room
// Harish Parthasarathy
// 2018-01-15
// ICS4U1-02
// Ms. Strelkovska

import java.awt.*;
import java.util.ArrayList;

public class Room extends Rectangle{

    boolean spawned = false;
    int x, y;
    public Tile tileArray[][];
    public String type;
    ArrayList<Mob> monsters;
    ArrayList<Bullet> bullets;
    ArrayList<Bullet> hits;
    ArrayList<Bullet> enemyBul;
    int pathNum = 0;
    public Room(String type, int i, int j, int pathNum) {
        this.pathNum = pathNum;
        x = i; y = j;
         tileArray = new Tile[25][25];
        monsters = new ArrayList<Mob>();
        bullets = new ArrayList<Bullet>();
       hits = new ArrayList<Bullet>();
       enemyBul = new ArrayList<Bullet>();
        this.type = type;
        initialize(i , j);
    }

    public void initialize(int a, int b) {


        a -= 2;
        b -= 2;
        if (type != "null") {
            for (int i = 0; i < 25; i++) {
                for (int j = 0; j < 25; j++) {
                    boolean wall = false;
                    if (i == 0 || i == 24 || j == 0 || j == 24)
                        wall = true;
                    tileArray[i][j] = new Tile ((j * 50) + (b * 1250), ((i) * 50) + (a * 1250), "p", wall);
                }
            }
        }

        else {
            for (int i = 0; i < 25; i++) {
                for (int j = 0; j < 25; j++) {
                    tileArray[i][j] = new Tile ((j * 50) + (b * 1250), ((i) * 50) + (a * 1250), "p", true);
                }
            }
        }
    }


    public void monsterSpawn(int pLvl) {
        Rectangle roomBox = new Rectangle(tileArray[0][0].x, tileArray[0][0].y, 1250, 1250);
        int randX = (int)(Math.random() * 950) + 150;
        int randY = (int)(Math.random() * 950) + 150;


        if (pLvl <= 2) {
            monsters.add(new Monster(roomBox.x + randX, roomBox.y + randY));
            return;
        }
        Mob m;
        double rand = Math.random();

        if (pathNum == 4) {
            monsters.add(new Boss(roomBox.x + 500, roomBox.y + 500));
            return;
        }
        else {
            if (rand < 0.4)
                m = new Gunner(roomBox.x + randX, roomBox.y + randY);
            else if (rand < 0.6)
                m = new Gunner2(roomBox.x + randX, roomBox.y + randY);
            else if (rand < 0.8)
                m = new Mage(roomBox.x + randX, roomBox.y + randY);
            else
                m = new Monster(roomBox.x + randX, roomBox.y + randY);
        }

        monsters.add(m);
        pLvl = pLvl - m.getPowerLvl();
        monsterSpawn(pLvl);

    }

    public boolean check() {
        Point colBox = new Point(Player.x, Player.y);
        Rectangle roomBox = new Rectangle(tileArray[0][0].x, tileArray[0][0].y, 1150, 1150);
        if (roomBox.contains(colBox)) {
            if (!spawned && type.equals("room")) {
                monsterSpawn(25);
                spawned = true;
            }
            return true;
        }

        return false;
    }

    public void update(Room[][] rooms) {
        if (monsters.size() == 0 && pathNum == 4) {
            Game.endGame();
            Game.win = true;
        }
        if (monsters.size() == 0) {
            if (Game.path[pathNum].equals("up")) {
                tileArray[0][12].wall = false;
                tileArray[0][13].wall = false;

                tileArray[0][14].wall = false;


                rooms[x-1][y].tileArray[24][12].wall = false;
                rooms[x-1][y].tileArray[24][13].wall = false;
                rooms[x-1][y].tileArray[24][14].wall = false;

            }

            if (Game.path[pathNum].equals("down")) {

                tileArray[24][12].wall = false;
                tileArray[24][13].wall = false;

                tileArray[24][14].wall = false;


                rooms[x+1][y].tileArray[0][12].wall = false;
                rooms[x+1][y].tileArray[0][13].wall = false;
                rooms[x+1][y].tileArray[0][14].wall = false;
            }

            if (Game.path[pathNum].equals("left")) {

                tileArray[12][0].wall = false;
                tileArray[13][0].wall = false;

                tileArray[14][0].wall = false;


                rooms[x][y-1].tileArray[12][24].wall = false;
                rooms[x][y-1].tileArray[13][24].wall = false;
                rooms[x][y-1].tileArray[14][24].wall = false;
            }

            if (Game.path[pathNum].equals("right")) {

                tileArray[12][24].wall = false;
                tileArray[13][24].wall = false;

                tileArray[14][24].wall = false;


                rooms[x][y+1].tileArray[12][0].wall = false;
                rooms[x][y+1].tileArray[13][0].wall = false;
                rooms[x][y+1].tileArray[14][0].wall = false;
            }
        }

        else if (pathNum > 0) {

            if (Game.path[pathNum-1].equals("down")) {
                tileArray[0][12].wall = true;
                tileArray[0][13].wall = true;
                tileArray[0][14].wall = true;
                rooms[x-1][y].tileArray[24][12].wall = true;
                rooms[x-1][y].tileArray[24][13].wall = true;
                rooms[x-1][y].tileArray[24][14].wall = true;

            }

            if (Game.path[pathNum-1].equals("up")) {
                tileArray[24][12].wall = true;
                tileArray[24][13].wall = true;
                tileArray[24][14].wall = true;


                rooms[x+1][y].tileArray[0][12].wall = true;
                rooms[x+1][y].tileArray[0][13].wall = true;
                rooms[x+1][y].tileArray[0][14].wall = true;
            }

            if (Game.path[pathNum-1].equals("right")) {

                tileArray[12][0].wall = true;
                tileArray[13][0].wall = true;

                tileArray[14][0].wall = true;
                rooms[x][y-1].tileArray[12][24].wall = true;
                rooms[x][y-1].tileArray[13][24].wall = true;
                rooms[x][y-1].tileArray[14][24].wall = true;
            }

            if (Game.path[pathNum-1].equals("left")) {

                tileArray[12][24].wall = true;
                tileArray[13][24].wall = true;

                tileArray[14][24].wall = true;
                rooms[x][y+1].tileArray[12][0].wall = true;
                rooms[x][y+1].tileArray[13][0].wall = true;
                rooms[x][y+1].tileArray[14][0].wall = true;
            }

        }

        entitiesUpdate();
    }
    public void entitiesUpdate() {

        for (int i = 0; i < monsters.size(); i++) {
            if (monsters.get(i).alive) {
                monsters.get(i).updateLocation();
                if (monsters.get(i).intersects(Player.colBox) && !Player.immune) {
                    Player.health -= 1;
                    Player.immune = true;
                }
            }
            else {
                monsters.remove(i);
                i--;
            }

        }

        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            b.updateLocation();
            if (!b.collision(this, bullets, i, true)){
                for (int j = 0; j < monsters.size(); j++) {
                    if (monsters.get(j).intersects(bullets.get(i))) {
                        b.alive = false;
                        monsters.get(j).health-=1;
                        if (monsters.get(j).health <= 0)
                            monsters.get(j).alive = false;
                        hits.add(b);
                        bullets.remove(i);
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < enemyBul.size(); i++) {
            Bullet b = enemyBul.get(i);
            b.updateLocation();
            if (!b.collision(this, enemyBul, i, false)){
                Rectangle player = new Rectangle(Player.x, Player.y, Player.width, Player.height);
                if (b.intersects(player) && !Player.immune) {
                    b.alive = false;
                    Player.health -=1;
                    Player.immune = true;
                    enemyBul.remove(i);
                }
            }
        }


    }
}
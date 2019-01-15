import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.Timer;

public class Game extends JPanel {

    // Checks for timer, how long player has played for
    private static int seconds = 0;
    private static int minutes = 0;
    private static String timer= "00:00";

    // mapX and mapY are the change in directions for every frame change
    public static double mapX = 0, mapY = 0;
    // if player is reloading
    public static boolean reloading;
    // if the game is running, when false, ends gameLoop
    private static boolean gameRunning = false;
    // fps, used to repaint, aswell as dependancy for sprite updates. eg, using fps % 10 == 0, every 10th frame, a sprite will change
    public static int fps;

    // Player weapon
    private Weapon GUN = new Weapon();

    // Path is the path from one room to the other, directions such as "up", "down", "left", "right"
    public static String[] path = new String[5];
    // 2D array of all the rooms generated, always less than 5 by 5
    public static Room[][] rooms = new Room[5][5];

    // Current room player is in
    public static Room room;

    // When the player wins
    static boolean win;

    // If the player is in the menu, initialized as true
    private static boolean menu = true;


    public Game()
    {

        // MENU GENERATION AND LAYOUT
        setLayout(new FlowLayout(FlowLayout.CENTER, 100000, 60));

        // Calculating minutes and seconds from seconds
        int tempM = Main.lowTime /60;
        int tempS = Main.lowTime - tempM * 60;
        String tempTimer = String.format("Fastest Time: %02d:%02d", tempM, tempS);

        // Menu stuffs
        JButton button1 = new JButton("Start Game");
        JLabel label = new JLabel("<html><div style='text-align: center;'>" + tempTimer + "</div></html>");
        JButton button3 = new JButton("Exit");
        JCheckBox god = new JCheckBox("God Mode");

        button1.setPreferredSize(new Dimension(Main.fWidth/4, Main.fHeight/5));
        label.setPreferredSize(new Dimension(Main.fWidth/4, Main.fHeight/5));
        button3.setPreferredSize(new Dimension(Main.fWidth/4, Main.fHeight/5));

        label.setFont(new Font("Serif", Font.PLAIN, 36));
        button1.setFont(new Font("Serif", Font.PLAIN, 72));
        button3.setFont(new Font("Serif", Font.PLAIN, 72));

        // Check when button pressed
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == button1) {
                    // Once Player hits Start Game, menu is set to false, and gameRunning and gameLoop begins
                    gameRunning = true;
                    menu = false;
                    gameLoop();
                    // If player had selected godMode, their health bar is the max integer value
                    if (god.isSelected())
                        Player.health = Integer.MAX_VALUE;
                    // Removes all menu components from screen
                    removeAll();
                } else if (e.getSource() == button3) {
                    //EXIT
                    System.exit(0);
                }

            }
        };

        // Adding actionListener and buttons to screen and formatting
        button1.addActionListener(listener);
        button3.addActionListener(listener);

        button1.setBackground(Color.black);
        label.setBackground(Color.black);
        button3.setBackground(Color.black);
        god.setBackground(Color.gray);

        button1.setForeground(Color.blue);
        label.setForeground(Color.blue);
        button3.setForeground(Color.blue);

        add(button1);
        add(label);
        add(button3);
        add(god);


        // GAME GENERATION

        // Used to define the order of rooms in the pathway
        int pathNum = 0;

        // Goes through all the rooms, sets all to null so that they are all initialized
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                rooms[i][j] = new Room("null", i, j, 0);
            }
        }

        // Setting the middle room of the array as the start room
        rooms[2][2] = new Room("start",2 ,2, pathNum);

        // Recursive function that randomly generates rooms
        roomGeneration(4, 2, 2, 0, 0, pathNum);

        // Set initial room to starting room
        room = rooms[2][2];

        // Prints Map
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(rooms[i][j].type + " | ");
            }
            System.out.println();
            System.out.println("-------------------------");
        }

    }

    // GameLoop
    public void gameLoop() {
        // Initialize timer, so that target is 60 frames every second
        Timer timer = new java.util.Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(!Main.pause) {
                    runTick();
                    repaint();

                }
                if (!gameRunning) {
                    timer.cancel();
                }
            }
        };
        timer.schedule(task, 0, 1000 / 60);
    }


    public synchronized void runTick() {

        // fps increments so that it can be used for sprite calculations
        fps++;

        // Calculating timer
        if (fps % 60 == 0) {
            seconds++;
            if (seconds >= 60) {
                minutes++;
                seconds = 0;
            }
            timer = String.format("%02d:%02d", minutes, seconds);
        }

        // Go through array, and check which room player is in
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (rooms[i][j].check()) {
                    room = rooms[i][j];
                }
            }
        }

        // Updates to weapon, player and specific room
        // Only current room is updated, as it is unneccearry to update other rooms while not in them.
        GUN.weaponUpdate();
        Player.update();
        room.update(rooms);

    }

    protected synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        setBackground(Color.black);



        // If menu, set background to grey
        if (menu) {
            setBackground(Color.gray);
            System.out.println("In menu");
        }

        // Lose condition when player health is 0, or less, ends game
        else if (Player.health <= 0) {
            gameRunning = false;
            g2.setColor(Color.white);
            g2.setFont(new Font("TimesRoman", Font.PLAIN, 36));
            g2.drawString("Aww you died. Better luck next time! Click anywhere on screen to exit.", 200, 200);
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.exit(0);
                }
            });
            return;
        }

        // When player has won, print time, and end game
        else if (win) {
            gameRunning = false;
            g2.setColor(Color.white);
            g2.setFont(new Font("TimesRoman", Font.PLAIN, 36));
            g2.drawString("Wow you won! Nice Job! Your time was " + timer,  200, 200);
            if (minutes * 60 + seconds <= Main.lowTime) {
                g2.drawString("That's a new highscore! What a Legend!", 200, 300);
                try {
                    FileWriter writer = new FileWriter("lowTime.txt");

                    writer.write(Integer.toString(minutes * 60 + seconds));
                    writer.close();
                }
                catch (Exception e) {
                    System.out.println("Could not write fastest time.");
                }
            }
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.exit(0);
                }
            });
        }

        // If normal gameplay occurs
        else {
            // Drawing tiles for each room
            for (int h = 0; h < 5; h++) {
                for (int f = 0; f < 5; f++) {
                    Room temp = rooms[f][h];
                    for (int i = 0; i < 25; i++) {
                        for (int j = 0; j < 25; j++) {
                            if (temp.tileArray[i][j].wall)
                                g2.setColor(Color.black);
                            else
                                g2.setColor(new Color(75, 0, 130));
                            temp.tileArray[i][j].x += (int) mapX;
                            temp.tileArray[i][j].y += (int) mapY;
                            g2.fillRect(temp.tileArray[i][j].x, temp.tileArray[i][j].y, temp.tileArray[i][j].width, temp.tileArray[i][j].height);
                        }
                    }
                }
            }


            // All sprites are multiplied by 3, for visibility and consistancy
            // Calculate player height and width, as sprites have different size slightly
            Player.height = Player.sprite.getHeight() * 3;
            Player.width = Player.sprite.getWidth() * 3;

            // Calculates Angle between Player and the Mouse location, used for where the bullets should go and gun direction
            double angle = (Math.atan2(Main.fCenterY  - Main.mouseY, Main.fCenterX - Main.mouseX) - Math.PI);

            // If the player is shooting, spawn new bullets and add them to the players bullet arraylist
            if (Player.range && GUN.ammo > 0) {
                if (!GUN.firing) {
                    room.bullets.add(GUN.createNewBullet(Main.fWidth / 2 - 15 + (int) ((Player.width + 20) * Math.cos(angle - 0.14)), Main.fHeight / 2 - 5 + (int) ((Player.width + 20) * Math.sin(angle - 0.14)), 10, angle));
                    GUN.ammo--;
                }
                GUN.firing = true;
            }


            // Flip is used, as only right side sprites are loaded in
            // If player or Enemy is facing the left side, flip is set to true, and the image is flipped

            // Draws Enemies
            for (int i = 0; i < room.monsters.size(); i++) {
                // Reference point
                Mob temp = room.monsters.get(i);
                if (!temp.flip) {
                    // Draws actual Monster
                    g2.drawImage(temp.sprite, temp.x, temp.y, temp.width, temp.height, null);
                    // Checks if Monster is a ranged monster holding a gun, and rotates the gun to point at player
                    if (temp.gun) {
                        g2.rotate(temp.shootAngle, temp.x + temp.width / 2, temp.y + temp.height / 2);
                        g2.drawImage(temp.weapon.sprite, temp.x + temp.width, temp.y + temp.height / 2 - 12, temp.weapon.width, temp.weapon.height, null);
                        g2.drawImage(temp.hand, temp.x + temp.width - 2, temp.y + temp.height / 2 + 2, temp.hand.getWidth() * 3, temp.hand.getWidth() * 3, null);
                        // Reset rotation
                        g2.rotate(-temp.shootAngle, temp.x + temp.width / 2, temp.y + temp.height / 2);
                    }
                }
                // Same as above, just for flipped image
                else {
                    g2.drawImage(temp.sprite, temp.x + temp.width, temp.y, -temp.width, temp.height, null);
                    if (temp.gun) {
                        g2.rotate(temp.shootAngle, temp.x + temp.width / 2, temp.y + temp.height / 2);
                        g2.drawImage(temp.weapon.sprite, temp.x + temp.width, temp.y + temp.height / 2 + 12, temp.weapon.width, -temp.weapon.height, null);
                        g2.drawImage(temp.hand, temp.x + temp.width + 7, temp.y + temp.height / 2 - 2, temp.hand.getWidth() * 3, temp.hand.getWidth() * 3, null);
                        g2.rotate(-temp.shootAngle, temp.x + temp.width / 2, temp.y + temp.height / 2);
                    }
                }
            }


            // Checks if player is in his invulnerability frames, after being hit by enemy
            if ((Player.counter / 10) % 2 == 0) {
                // Similar to monster
                if (!Player.flip) {
                    // Draws Image, and rotates the gun, based on Mouse location
                    g2.drawImage(Player.sprite, Player.x, Player.y, Player.width, Player.height, null);
                    g2.rotate(angle, Main.fWidth / 2, Main.fHeight / 2);
                    g2.drawImage(GUN.sprite, Player.x + Player.width, Player.y + Player.height / 2 - 12, GUN.width, GUN.height, null);
                    g2.drawImage(Player.hand, Player.x + Player.width - 2, Player.y + Player.height / 2 + 2, Player.hand.getWidth() * 3, Player.hand.getWidth() * 3, null);
                } else { // Same as above
                    g2.drawImage(Player.sprite, Player.x + Player.width, Player.y, -Player.width, Player.height, null);
                    g2.rotate(angle, Main.fWidth / 2, Main.fHeight / 2);
                    g2.drawImage(GUN.sprite, Player.x + Player.width + 5, Player.y + Player.height / 2 + 12, GUN.width, -GUN.height, null);
                    g2.drawImage(Player.hand, Player.x + Player.width + 7, Player.y + Player.height / 2 - 2, Player.hand.getWidth() * 3, -Player.hand.getWidth() * 3, null);
                }
                // Reset rotation
                g2.rotate(-angle, Main.fWidth / 2, Main.fHeight / 2);


            }
            // Goes through arrayList of Players, bullets, and draws them
            for (int i = 0; i < room.bullets.size(); i++) {
                g2.rotate(room.bullets.get(i).angle, room.bullets.get(i).x + room.bullets.get(i).width / 2, room.bullets.get(i).y + room.bullets.get(i).height / 2);
                g2.drawImage(room.bullets.get(i).sprite, room.bullets.get(i).x + room.bullets.get(i).width / 2, room.bullets.get(i).y + room.bullets.get(i).height / 2, room.bullets.get(i).width, room.bullets.get(i).height, null);
                g2.rotate(-room.bullets.get(i).angle, room.bullets.get(i).x + room.bullets.get(i).width / 2, room.bullets.get(i).y + room.bullets.get(i).height / 2);
            }

            // Goes through arrayList of enemies bullets and draws them
            for (int i = 0; i < room.enemyBul.size(); i++) {
                g2.drawImage(room.enemyBul.get(i).sprite, room.enemyBul.get(i).x + room.enemyBul.get(i).width / 2, room.enemyBul.get(i).y + room.enemyBul.get(i).height / 2, room.enemyBul.get(i).width, room.enemyBul.get(i).height, null);
            }

            // After player hits wall or enemy, hits is array of bullets in the hit animation
            for (int i = 0; i < room.hits.size(); i++) {
                int tempW = room.hits.get(i).hit[room.hits.get(i).hitFrame].getWidth() * 3;
                int tempH = room.hits.get(i).hit[room.hits.get(i).hitFrame].getWidth() * 3;
                g2.drawImage(room.hits.get(i).hit[room.hits.get(i).hitFrame], room.hits.get(i).x, room.hits.get(i).y, tempW, tempH, null);
                room.hits.get(i).hitFrame++;
                if (room.hits.get(i).hitFrame == 7) {
                    room.hits.get(i).hitFrame = 0;
                    room.hits.remove(i);
                    i--;
                }

            }


            // Drawing UI

            // Initial heart location
            int heartX = 20;

            // Drawing from left to right heart
            if (Player.health >= 2)
                g2.drawImage(ImageLoader.load("fullHeart.png"), heartX, 20, null);
            else
                g2.drawImage(ImageLoader.load("halfHeart2.png"), heartX, 20, null);
            heartX += 60;

            if (Player.health >= 4)
                g2.drawImage(ImageLoader.load("fullHeart.png"), heartX, 20, null);
            else if (Player.health >= 3)
                g2.drawImage(ImageLoader.load("halfHeart2.png"), heartX, 20, null);
            heartX += 60;

            if (Player.health >= 6)
                g2.drawImage(ImageLoader.load("fullHeart.png"), heartX, 20, null);
            else if (Player.health >= 5)
                g2.drawImage(ImageLoader.load("halfHeart2.png"), heartX, 20, null);


            // Drawing timer, and ammo count for player
            g2.setColor(Color.white);
            g2.setFont(new Font("TimesRoman", Font.PLAIN, 36));
            g2.drawString(GUN.ammo + " / " + GUN.maxAmmo, 50, 100);
            g2.drawString(timer, 50, 140);

            // Reset changes in mapX and mapY
            Game.mapX = 0;
            Game.mapY = 0;
        }
    }


    // Called at the beginning of the Game
    // Recursively Generates rooms
    public void roomGeneration (int roomNumber, int i, int j, int pathLoc, int roomNum, int pathNum) {
        // baseCase, when the last room is generated
        if (roomNumber == 0) {
            path[pathLoc] = "final";
            return;
        }

        // Initial I and Inital J, incase the Random Variable does not allow a room to be spawned
        int iInit = i;
        int jInit = j;

        // Randomly Generate a number, from 0 - 3, for each of the 4 directions
        int randomNum = (int)(Math.random() * 4);

        // Corresponding room number to direction
        // Checks i or j, so that room is in the roomsArray which is 5x5 in size
        if (randomNum == 0 && i > 0) {
            i-= 1;
            path[pathLoc] = "up";
        }
        else if (randomNum == 1 && j < 4) {
            j+=1;
            path[pathLoc] = "right";
        }
        else if (randomNum == 2 && i < 4) {
            i+=1;
            path[pathLoc] = "down";
        }
        else if (randomNum == 3 && j > 0){
            j-=1;
            path[pathLoc] = "left";
        }


        // If the room is not null and has been generated as such, call the same method again, with the initial paramaters
        if (!rooms[i][j].type.equals("null"))
            roomGeneration(roomNumber, iInit, jInit, pathLoc, roomNum, pathNum);

        else {
            // Updating Variables
            pathLoc++;
            roomNum++;
            pathNum += 1;


            // Create new room
            rooms[i][j] = new Room("room", i, j, pathNum);
            // call function with new updated variables
            roomGeneration(roomNumber - 1, i, j, pathLoc, roomNum, pathNum);
        }
    }

    // Used as method to change gameRunning variable as it is private
    public static void endGame() {
        gameRunning = false;


    }


}
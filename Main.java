import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;

public class Main extends JFrame {

    // Width, Height, Center of ScreenX, Center of Scren Y
    public static int fWidth;
    public static int fHeight;
    public static int fCenterX;
    public static int fCenterY;

    // Point p is used to get the information of where the mouse is on the frame
    public static Point p = null;
    public static int mouseX = 0;
    public static int mouseY= 0;

    // HighScore, fastest time read from file
    public static int lowTime;

    // If mouse is outside of frame, pause everything about game
    public static boolean pause = false;


    Main() {

        // Reads file for highScore Value
        try {
            FileReader fileReader = new FileReader("lowTime.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            lowTime = Integer.parseInt(bufferedReader.readLine());
            bufferedReader.close();
        }
        catch (Exception e) {
            System.out.println("Could not read fastest time.");
        }

        // Gets the Size of User's screen to maximize frame dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        fWidth = screenSize.width - 40;
        fHeight = screenSize.height - 40;
        fCenterX = fWidth/2;
        fCenterY = fHeight/2;

        // F is used a reference point in the timer below
        Frame f = this;
        // Starting a new timer, used to check where the mouse location is
        Timer timer = new Timer(66, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(p, f);
                mouseX = (int)p.getX()+7;
                mouseY = (int)p.getY()-16;
            }
        });
        timer.start();

        // Replace cursor with custom image
        Image image = ImageLoader.load("cursor.png");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Cursor c = toolkit.createCustomCursor(image , new Point(mouseX, mouseY), "img");
        this.setCursor(c);

        // Initializes Game
        Game g = new Game();
        add(g);

        // Set Frame Variable's
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(true);
        setSize(fWidth,fHeight);
        setTitle("Bird's Eye");
        setVisible(true);
    }

    public static void main(String[] args) {
        // Creates a new Main
        Main m = new Main();

        // Adding Mouse listener
        m.addMouseListener(new MouseAdapter() {
            // Check if mouse pressed down, then ranged attacks begin
            @Override
            public void mousePressed(MouseEvent e) {
                Player.range = true;
            }
            // Once released, stop attacking
            @Override
            public void mouseReleased(MouseEvent e) {
                Player.range = false;
            }

            // Checks if mouse is in frame, in order to pause
            @Override
            public void mouseEntered(MouseEvent e) {
                pause = false;
            }

            @Override
            public void mouseExited(MouseEvent e){
                System.out.println("PAUSED");
                pause = true;
            }
        });

        // Checks for keys pressed by characters, WASD for directions, R to reload
        m.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A)
                    Player.goLeft = true;
                if (e.getKeyCode() == KeyEvent.VK_D)
                    Player.goRight = true;
                if (e.getKeyCode() == KeyEvent.VK_W)
                    Player.goUp = true;
                if (e.getKeyCode() == KeyEvent.VK_S)
                    Player.goDown = true;
            }
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A)
                    Player.goLeft = false;
                if (e.getKeyCode() == KeyEvent.VK_D)
                    Player.goRight = false;
                if (e.getKeyCode() == KeyEvent.VK_W)
                    Player.goUp = false;
                if (e.getKeyCode() == KeyEvent.VK_S)
                    Player.goDown = false;
            }

            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == 'r') {
                    System.out.println("reload");
                    Game.reloading = true;
                }
            }

        });

    }
}

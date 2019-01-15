import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageLoader {
    //Loads Images, try catch loop here to make code cleaner

    // Loads singular image
    public static BufferedImage load(String path) {
        try {
            return ImageIO.read(new File(path));
        }
        catch (IOException e) {
            System.out.println("ImageLoadingFailed:" + path);
            return null;
        }
    }

    // Loads array of images by using load from above
    public static BufferedImage[] arrayLoad (String path, int size) {
        BufferedImage[] array  = new BufferedImage[size];
        for (int i = 1; i <= size; i++) {
            if (i >= 10)
                array[i-1] = load(path.substring(0, path.length()-1) + i + ".png");
            else {
                array[i - 1] = load(path + i + ".png");
            }
        }
        return array;
    }
}

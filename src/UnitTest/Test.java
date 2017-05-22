package UnitTest;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Erwin on 4/19/2017.
 */
public class Test extends JPanel {

    private BufferedImage bg;

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if(bg != null) {
            g.drawImage(bg, 0, 0, bg.getWidth(), bg.getHeight(), null);
        }
    }

    public void updateBG(String path)
    {
        try {
            bg = ImageIO.read(getClass().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

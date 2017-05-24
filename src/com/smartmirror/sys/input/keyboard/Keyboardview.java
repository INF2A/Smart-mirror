package com.smartmirror.sys.input.keyboard;


import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by Erwin on 5/11/2017.
 */
public class Keyboardview extends JPanel {

    List<Keyboard.Key> keys;
    Keyboard.Key currentKey;

    public Keyboardview()
    {
        currentKey = null;
    }

    public void setKeys(List<Keyboard.Key> keys)
    {
        this.keys = keys;
    }
    public void setCurrentKey(Keyboard.Key key)
    {
        this.currentKey = key;
    }

    /**
     * Override the default JPanel paint settings to draw our keyboard
     * @param g The graphics object
     */
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        this.setBackground(Color.WHITE);

        for ( Keyboard.Key key : keys )
        {
            g.setColor(Color.GRAY);
            g.drawRect(key.x, key.y, key.width, key.height);

            // draw box to show the currently selected key
            if(key.equals(currentKey))
            {
                g.setColor(Color.BLUE);
                g.drawRect(key.x + 1, key.y + 1, key.width - 2, key.height - 2);
            }

            g.setColor(Color.BLACK);
            g.drawString(key.label, key.x + key.width / 2, key.y + key.height / 2);
        }
    }
}

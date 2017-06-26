package com.smartmirror.core.view;

import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Abstract class for widgets
 * Used for holding methods and fields used by widgets
 */
public abstract class AbstractWidget extends JPanel{
    public Dimension dimension;     // Holds preferred size as Dimension
    public Location location;       // Holds location on SystemWindow

//    public final ExecutorService service = Executors.newFixedThreadPool(4);
//    public Future<JSONObject> task;

    public enum Location            // Selections for location on SystemWindow
    {
        TOP,
        TOP_LEFT,
        TOP_RIGHT,
        CENTER,
        CENTER_LEFT,
        CENTER_RIGHT,
        BOTTOM
    }

    public enum FontSize
    {
        H1,
        H2,
        H3,
        H4,
        H5,
        H6,
        H7
    }

    public Font applyFontSize(FontSize fontSize)
    {
        Font font = null;

        switch (fontSize)
        {
            case H1:
                font = new Font("Dialog", Font.PLAIN, Toolkit.getDefaultToolkit().getScreenResolution());
                break;
            case H2:
                font = new Font("Dialog", Font.PLAIN, Toolkit.getDefaultToolkit().getScreenResolution() / 2);
                break;
            case H3:
                font = new Font("Dialog", Font.PLAIN, Toolkit.getDefaultToolkit().getScreenResolution() / 3);
                break;
            case H4:
                font = new Font("Dialog", Font.PLAIN, Toolkit.getDefaultToolkit().getScreenResolution() / 4);
                break;
            case H5:
                font = new Font("Dialog", Font.PLAIN, Toolkit.getDefaultToolkit().getScreenResolution() / 5);
                break;
            case H6:
                font = new Font("Dialog", Font.PLAIN, Toolkit.getDefaultToolkit().getScreenResolution() / 6);
                break;
            case H7:
                font = new Font("Dialog", Font.PLAIN, Toolkit.getDefaultToolkit().getScreenResolution() / 7);
                break;
        }

        return font;
    }

    // Set default settings for a Widget
    public AbstractWidget()
    {
        this.setBackground(Color.BLACK);
    }

    // Define components that update after a certain amount of time here
    // Call this method within ScheduledExecutorService or Timer
    public abstract void update();

    public abstract JSONObject requestJson();

    public void resync()
    {
        update();
    }
}

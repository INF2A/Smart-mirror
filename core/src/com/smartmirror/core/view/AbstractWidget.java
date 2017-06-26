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

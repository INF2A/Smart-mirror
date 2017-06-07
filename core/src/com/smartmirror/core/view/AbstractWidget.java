package com.smartmirror.core.view;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract class for widgets
 * Used for holding methods and fields used by widgets
 */
public abstract class AbstractWidget extends JPanel{
    public Dimension dimension;     // Holds preferred size as Dimension
    public Location location;       // Holds location on SystemWindow

    public enum Location            // Selections for location on SystemWindow
    {
        TOP,
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
    public abstract void init();
}

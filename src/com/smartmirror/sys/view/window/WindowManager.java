package com.smartmirror.sys.view.window;

import com.smartmirror.core.view.AbstractWindow;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Erwin on 6/3/2017.
 */
public class WindowManager {
    Map<String, AbstractWindow> windowList = new HashMap<>();;

    JPanel mainWindowHolder;
    JPanel windowHolder;

    // The window that is currently showing in the windowHolder.
    private AbstractWindow currentWindow;

    public WindowManager(JPanel windowHolder) {
        this.windowHolder = windowHolder;
        this.mainWindowHolder = windowHolder;
    }

    public JPanel getWindowHolder() {
        return windowHolder;
    }
    public void setWindowHolder(JPanel windowHolder) {
        this.windowHolder = windowHolder;
    }

    public void resetWindowHolder() {
        windowHolder = mainWindowHolder;
    }

    /**
     * Adds a window to the Window Manager which can be switched too.
     * The window will be held into memory.
     *
     * @param name The name of the window
     * @param window The window itself
     */
    public void addWindow(String name, AbstractWindow window) {
        windowList.put(name, window);
    }

    public AbstractWindow getWindow(String name) {
        return windowList.get(name);
    }

    public AbstractWindow getCurrentWindow() {
        return currentWindow;
    }


    /**
     * This will show the window specified.
     * It will only be known by the WindowManager for this instance
     * This window will be forgotten when changing to another window
     *
     * @param window The AbstractWindow to display
     */
    public void setWindow(AbstractWindow window) {
        // remove the current window
        if(currentWindow != null) windowHolder.remove(currentWindow.INTERNAL_getScreen());
        // set the new window
        windowHolder.add(window.INTERNAL_getScreen());
        // store the new window as the current window
        currentWindow = window;
        // update display
        update();
    }


    /**
     * This will search through the known windows and
     * display the window with the corresponding name
     *
     * Our container uses a BorderLayout and the given
     * window will be added to BorderLayout.Center
     *
     * @param windowName The name of the window to display
     */
    public void setWindow(String windowName) {
        // find the window
        AbstractWindow w = windowList.get(windowName);
        // check if it exists
        if(w != null) {
            // remove the current window
            if(currentWindow != null) windowHolder.remove(currentWindow.INTERNAL_getScreen());
            // set the new window
            windowHolder.add(w.INTERNAL_getScreen());
            // store the new window as the current window
            currentWindow = w;
            // update display
            update();
        }
    }

    /**
     * Updates the display with the latest visual changes.
     * Makes a call to the system container to update itself.
     * This will draw any new window, or changes to the current
     * window, to the display
     */
    public void update()
    {
        // repaints all children of the panel
        windowHolder.revalidate();
        // repaint the panel
        windowHolder.repaint();
    }
}

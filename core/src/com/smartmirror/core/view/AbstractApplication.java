package com.smartmirror.core.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by Erwin on 5/21/2017.
 */
public abstract class AbstractApplication extends AbstractWindow {

    public boolean INTERNAL_setupRun = false;
    public AbstractWidget SYSTEM_Widget; // Holds application widget
    public ImageIcon SYSTEM_Icon; // ImageIcon that holds a icon of the application


    public AbstractApplication()
    {
        SYSTEM_Screen.setBackground(Color.BLACK);
    }

    public void setSYSTEM_Icon(String pathFromResources)
    {
        ClassLoader classLoader = getClass().getClassLoader();
        SYSTEM_Icon = new ImageIcon(classLoader.getResource(pathFromResources));
    }

    /**
     * Will be called only once when app starts
     */
    public void INTERNAL_setup() {
        INTERNAL_setupRun = true;
        setup();
        INTERNAL_init();
    }

    /**
     * Will be called only once when app starts
     */
    protected abstract void setup();

    /**
     * Will be called everytime when an app opens
     */
    public abstract void init();


    @Deprecated
    JButton INTERNAL_DestroyHandle = new JButton(); // The handle used by the system for destroying the application


    JButton INTERNAL_ExitHandle = new JButton(); // The handle used by the system for closing the application

    /**
     * This is an INTERNAL method used by the system to check
     * if the SYSTEM_closeScreen method is called.
     *
     * It will add a listener to the jbutton INTERNAL_ExitHandle
     * by the system to know when to close the application.
     *
     * @param al The ActionListener for the exitHandle
     */
    public void INTERNAL_addExitActionListener(ActionListener al)
    {
        INTERNAL_ExitHandle.addActionListener(al);
    }

    /**
     * This is an INTERNAL method used by the system to check
     * if the SYSTEM_closeScreen method is called.
     *
     * It will add a listener to the jbutton INTERNAL_ExitHandle
     * by the system to know when to close the application.
     *
     * @param al The ActionListener for the exitHandle
     */
    @Deprecated
    public void INTERNAL_addDestroyActionListener(ActionListener al)
    {
        INTERNAL_DestroyHandle.addActionListener(al);
    }

    /**
     * This Method may be called to exit the application.
     *
     * It calls an internal JButton which is connected
     * with the system through an ActionListener
     */
    public void SYSTEM_closeScreen()
    {
        focusManager.Reset();
        INTERNAL_ExitHandle.doClick();
    }

    /**
     * Can be called to destroy the current application
     * Once the user opens the app again, it will first run setup
     */
    @Deprecated
    public void SYSTEM_destroy() {
        INTERNAL_DestroyHandle.doClick();
    }
}

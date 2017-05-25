package com.smartmirror.core.view;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Created by Erwin on 5/21/2017.
 */
public abstract class AbstractApplication extends AbstractWindow {

    public JPanel SYSTEM_Widget = new JPanel();  // The container that holds a widget of the application

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
     * This Method may be called to exit the application.
     *
     * It calls an internal JButton which is connected
     * with the system through an ActionListener
     */
    public void SYSTEM_closeScreen()
    {
        INTERNAL_ExitHandle.doClick();
    }

}

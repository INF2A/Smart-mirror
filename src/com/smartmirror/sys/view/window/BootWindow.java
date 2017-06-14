package com.smartmirror.sys.view.window;

import com.smartmirror.core.view.AbstractWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Erwin on 6/9/2017.
 */
public class BootWindow extends AbstractWindow {

    JLabel loading;

    public BootWindow() {
        SYSTEM_Screen.setLayout(new BoxLayout(SYSTEM_Screen, BoxLayout.Y_AXIS));
        SYSTEM_Screen.setBackground(Color.BLACK);
        ImageIcon icon;
        icon = new ImageIcon(getClass().getClassLoader().getResource("loading.gif"));
        JLabel loadingGif = new JLabel(icon);
        loadingGif.setAlignmentX(Component.CENTER_ALIGNMENT);
        SYSTEM_Screen.add(loadingGif);
        loading = new JLabel("Getting the system ready...");
        loading.setAlignmentX(Component.CENTER_ALIGNMENT);
        loading.setForeground(Color.WHITE);
        SYSTEM_Screen.add(loading);
    }
}

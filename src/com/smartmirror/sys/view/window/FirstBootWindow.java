package com.smartmirror.sys.view.window;

import applications.ProfileCreator;
import applications.Wifi;
import com.smartmirror.core.view.AbstractWindow;
import com.smartmirror.sys.DB;
import com.smartmirror.sys.MainSystemController;
import com.smartmirror.sys.view.*;
import com.smartmirror.sys.view.FocusManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Erwin on 6/2/2017.
 */
public class FirstBootWindow extends AbstractWindow{

    public volatile boolean isFinished = false;
    private JLabel messageLabel;
    private MainSystemController systemController;
    private JPanel windowHolder;


    public FirstBootWindow(MainSystemController systemController) {
        focusManager = new FocusManager();
        this.systemController = systemController;
        SYSTEM_Screen.setBackground(Color.BLACK);
        focusManager = new FocusManager();

        // new BoxLayout(SYSTEM_Screen, BoxLayout.Y_AXIS);
        SYSTEM_Screen.setLayout(new BorderLayout());
        SYSTEM_Screen.setBackground(Color.BLACK);

        windowHolder = new JPanel();
        windowHolder.setBackground(Color.BLACK);
        SYSTEM_Screen.add(windowHolder, BorderLayout.CENTER);

        messageLabel = new JLabel("temp");
        messageLabel.setForeground(Color.WHITE);
        windowHolder.add(messageLabel);

        JButton nextAccount = new JButton("Create Account ->");
        JButton nextWifi = new JButton("Setup Wifi ->");
        JButton nextStart = new JButton("Start ->");

        nextAccount.addActionListener(e -> {
            SYSTEM_Screen.remove(nextAccount);
            SYSTEM_Screen.add(nextWifi, BorderLayout.SOUTH);

            focusManager.removeComponents(focusManager.getComponentList());
            focusManager.addComponent(SYSTEM_Screen);
            focusManager.Reset();

            windowHolder.removeAll();
            windowHolder.add(((ProfileCreator)systemController.getApplication("profileCreator")).accountCreatorPanel);
            focusManager.addComponents(((ProfileCreator)systemController.getApplication("profileCreator")).focusManager.getComponentList());
            focusManager.addComponent(nextWifi);

            update();
        });

        nextWifi.addActionListener(e -> {
            SYSTEM_Screen.remove(nextWifi);
            SYSTEM_Screen.add(nextStart, BorderLayout.SOUTH);

            focusManager.removeComponents(focusManager.getComponentList());
            focusManager.addComponent(SYSTEM_Screen);
            focusManager.Reset();

            windowHolder.removeAll();
            windowHolder.add(((Wifi)systemController.getApplication("wifi")).wifiButtonsHolder);
            focusManager.addComponents(((Wifi)systemController.getApplication("wifi")).wifiButtons);
            focusManager.addComponent(nextStart);

            update();
        });

        nextStart.addActionListener(e -> {
            windowHolder.removeAll();
            SYSTEM_Screen.removeAll();
            update();
            isFinished = true;
        });

        focusManager.addComponent(SYSTEM_Screen);
        SYSTEM_Screen.add(nextAccount, BorderLayout.SOUTH);
        focusManager.addComponent(nextAccount);

        update();
    }

    public void start() {
        ActionListener showMessage = e -> {
            messageLabel.setText("Welcome to the smartMirror :)");
        };
        Timer timer = new Timer(3000 ,showMessage);
        timer.setRepeats(false);
        timer.start();
    }



    // add information that its first boot
    // add creation of profile
    // add wifi selection




}

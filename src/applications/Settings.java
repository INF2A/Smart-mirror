package applications;

import com.smartmirror.core.view.AbstractSystemApplication;
import com.sun.scenario.effect.impl.sw.java.JSWBlend_COLOR_BURNPeer;
import widgets.SettingsWidget;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * System application - Displays all system applications and settings to change
 */
public class Settings extends AbstractSystemApplication{

    /**
     * Will only run ones when application is started
     * Define application startup settings here
     *
     * SYSTEM_Screen functions as base JPanel for the application
     */
    @Override
    public void setup() {
        SYSTEM_Widget = new SettingsWidget(); // Instantiate corresponding widget

        SYSTEM_Screen.setBackground(Color.BLACK);
    }

    /**
     * Will be called every time settings applications opens
     */
    @Override
    public void init() {
        SYSTEM_Screen.removeAll();
        focusComponents.removeAll(focusComponents);
        focusComponents.add(SYSTEM_Screen);
        INTERNAL_requestSystemApplications();
        getSettingsPanel(systemApplications);
    }

    /**
     * Define components settings panel
     * Shows all system applications
     *
     * @param apps
     */
    private void getSettingsPanel(Map<String, AbstractSystemApplication> apps)
    {
        JPanel bottom = new JPanel();
        bottom.setLayout(new FlowLayout());

        JButton saveButton = new JButton("Save settings");
        saveButton.addActionListener(e -> SYSTEM_closeScreen());

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> SYSTEM_closeScreen());


        for(Map.Entry<String, AbstractSystemApplication> entry : apps.entrySet())
        {
            if(!entry.getKey().equals("settings"))
            {
                JButton iconButton = new JButton(entry.getValue().SYSTEM_Icon);

                //ADD ACTIONLISTENER SWITCH TO SELECTED APP.

                iconButton.setBackground(Color.BLACK);
                SYSTEM_Screen.add(iconButton);
                focusComponents.add(iconButton);
            }
        }
        SYSTEM_Screen.add(saveButton);
        SYSTEM_Screen.add(exitButton);

        focusComponents.add(saveButton);
        focusComponents.add(exitButton);
    }

    @Override
    public void onBackButton() {
        SYSTEM_closeScreen();
    }
}

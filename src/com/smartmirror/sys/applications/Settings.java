package com.smartmirror.sys.applications;

import com.smartmirror.sys.applications.widgets.SettingsWidget;
import com.smartmirror.sys.view.AbstractSystemApplication;
//import widgets.DefaultWidget;
//import widgets.SettingsWidget;
import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Date;
import java.util.Map;

/**
 * System application - Displays all system applications and settings to change
 */
public class Settings extends AbstractSystemApplication{

    private JLabel title;
    private JPanel container;
    /**
     * Will only run ones when application is started
     * Define application startup settings here
     *
     * SYSTEM_Screen functions as base JPanel for the application
     */
    @Override
    public void setup() {
        setSYSTEM_Icon("img/settings.png");

        SYSTEM_Widget = new SettingsWidget(); // Instantiate corresponding widget
        addWidgetToSystemWindow = true;

        SYSTEM_Screen.setBackground(Color.BLACK);

        SYSTEM_Screen.setLayout(new BorderLayout());

        title = new JLabel("Settings");
        title.setFont(applyFontSize(FontSize.H1));
        title.setForeground(Color.WHITE);

        SYSTEM_Screen.add(title, BorderLayout.PAGE_START);

        container = new JPanel(new FlowLayout());
        container.setBackground(Color.BLACK);

        SYSTEM_Screen.add(container, BorderLayout.CENTER);

        displayApps();
    }

    /**
     * Will be called every time settings applications opens
     */
    @Override
    public void init() {
        container.removeAll();
        displayApps();
    }

    /**
     * Define components settings panel
     * Shows all system applications
     */
    private void displayApps()
    {
        for(Map.Entry<String, AbstractSystemApplication> entry : systemController.getSystemApplications().entrySet())
        {
            if(!(entry.getValue() instanceof Settings))
            {
                JButton iconButton = new JButton(entry.getValue().SYSTEM_Icon);
                iconButton.setBackground(Color.BLACK);
                //iconButton.setBorderPainted(false);
                container.add(iconButton);
                focusManager.addComponent(iconButton);
                iconButton.addActionListener(e -> systemController.startApplication(entry.getKey()));
            }
        }
    }

    @Override
    public void onBackButton() {
        SYSTEM_closeScreen();
    }
}

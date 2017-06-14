package applications;

import com.smartmirror.sys.view.AbstractSystemApplication;
import widgets.DefaultWidget;
import widgets.SettingsWidget;
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

        displayApps();
    }

    /**
     * Will be called every time settings applications opens
     */
    @Override
    public void init() {
//        SYSTEM_Screen.removeAll();
//        focusComponents.removeAll(focusComponents);
//        focusComponents.add(SYSTEM_Screen);
//        INTERNAL_requestSystemApplications();
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
                SYSTEM_Screen.add(iconButton);
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

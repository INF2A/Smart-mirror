package com.smartmirror.sys.applications.widgets;

import com.smartmirror.core.view.AbstractWidget;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;

/**
 * System Widget for displaying settings on screen
 */
public class SettingsWidget extends AbstractWidget {

    public SettingsWidget()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        ImageIcon image = new ImageIcon(classLoader.getResource("img/settings.png"));
        JLabel img = new JLabel(image);
        add(img);
        location = location.TOP_LEFT;
    }

    @Override
    public void update() {

    }

    @Override
    public JSONObject requestJson() {
        return null;
    }
}

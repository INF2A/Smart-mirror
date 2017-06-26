package com.smartmirror.sys.applications.widgets;

import com.smartmirror.core.view.AbstractWidget;
import org.json.simple.JSONObject;

import javax.swing.*;

/**
 * Created by basva on 19-6-2017.
 */
public class WifiWidget extends AbstractWidget {

    public WifiWidget()
    {
        //location = location.TOP_LEFT;

        ClassLoader classLoader = getClass().getClassLoader();
        ImageIcon image = new ImageIcon(classLoader.getResource("img/wifi-icon.png"));
        JLabel img = new JLabel(image);
        add(img);
    }

    @Override
    public void update() {

    }

    @Override
    public JSONObject requestJson() {
        return null;
    }
}

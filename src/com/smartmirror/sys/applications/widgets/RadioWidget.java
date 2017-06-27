package com.smartmirror.sys.applications.widgets;

import com.smartmirror.core.view.AbstractWidget;
import javazoom.jl.player.advanced.AdvancedPlayer;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.net.Socket;

/**
 * Created by basva on 26-6-2017.
 */
public class RadioWidget extends AbstractWidget {

    public RadioWidget()
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(40, 0, 10, 0));
        setAlignmentX(CENTER_ALIGNMENT);

        ClassLoader classLoader = getClass().getClassLoader();
        ImageIcon image = new ImageIcon(classLoader.getResource("img/Radio_w.png"));
        JLabel img = new JLabel(image);
        add(img);

        location = location.CENTER_LEFT;
    }

    @Override
    public void update() {

    }

    @Override
    public JSONObject requestJson() {
        return null;
    }
}

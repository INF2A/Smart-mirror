package com.smartmirror.sys.applications.widgets;

import com.smartmirror.core.view.AbstractWidget;
import org.json.simple.JSONObject;
import system.input.json.JsonParser;

import javax.swing.*;
import java.awt.*;

/**
 * System Widget for displaying time on screen
 */
public class ClockWidget extends AbstractWidget{
    public JSONObject json;
    public JLabel clock;

    public ClockWidget()
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        location = Location.CENTER_RIGHT;

        clock = new JLabel();
        clock.setForeground(Color.WHITE);
        clock.setFont(new Font("Dialog", Font.PLAIN, Toolkit.getDefaultToolkit().getScreenResolution()));
        System.out.println(Toolkit.getDefaultToolkit().getScreenResolution() + " -- " + Toolkit.getDefaultToolkit().getScreenSize());
        add(clock);
    }

    public void init()
    {
        getJSON();

        clock.setText(json.get("hour").toString() +":"+ json.get("minute").toString() +":"+ json.get("second").toString());
    }

    public void getJSON()
    {
        json = (JSONObject) JsonParser.parseURL("http://localhost:8090/time/Asia/Seoul").get("dateTime");
    }
}

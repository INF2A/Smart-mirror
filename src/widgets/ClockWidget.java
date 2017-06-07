package widgets;

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
        location = Location.CENTER_RIGHT;
        dimension = new Dimension(100, 100);

        clock = new JLabel();
        clock.setForeground(Color.WHITE);
        clock.setFont(new Font("Dialog", Font.PLAIN, 40));
        add(clock);
    }

    public void init()
    {
        getJSON();

        clock.setText(json.get("hour").toString() +":"+ json.get("minute").toString() +":"+ json.get("second").toString());
    }

    public void getJSON()
    {
        //Comment setSYSTEM_Widget() in setup method if not running api.
        json = (JSONObject) JsonParser.parseURL("http://localhost:8090/time/Europe/Amsterdam").get("dateTime");
    }
}

package applications;

import com.smartmirror.core.view.AbstractSystemApplication;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import system.input.json.JsonParser;

import javax.swing.*;
import java.awt.*;

/**
 * Created by basva on 31-5-2017.
 */
public class Clock extends AbstractSystemApplication {

    public JSONObject jsonTime;

    public void setup()
    {
        SYSTEM_Widget = new Widget();

        setSYSTEM_Widget();
        setSYSTEM_Icon("img/clock-icon.png");
    }

    @Override
    public void init() {

    }

    public void setSYSTEM_Widget()
    {
        getJSON();

        SYSTEM_Widget_Dimension = new Dimension(50,50);
        SYSTEM_Widget.add(new JLabel(jsonTime.get("hour").toString() +":"+ jsonTime.get("minute").toString() +":"+ jsonTime.get("second").toString())).setForeground(Color.WHITE);
        SYSTEM_Widget_Location = location.CENTER;
    }

    public void getJSON()
    {
        //Comment setSYSTEM_Widget() in setup method if not running api.
        jsonTime = (JSONObject)JsonParser.parseURL("http://localhost:8090/time").get("dateTime");
    }

    @Override
    public void onBackButton() {
        SYSTEM_closeScreen();
    }
}

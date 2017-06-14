package com.smartmirror.sys.applications.widgets;

import com.smartmirror.core.view.AbstractWidget;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import system.input.json.JsonParser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by basva on 9-6-2017.
 */
public class AgendaWidget extends AbstractWidget {
    private JSONArray json;

    private ClockWidget clockWidget;


    public AgendaWidget(ClockWidget clockWidget) {
        this.clockWidget = clockWidget;

        location = location.CENTER_LEFT;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    @Override
    public void init() {
        this.removeAll();

        getJSON();

        JLabel today = new JLabel("Today");
        today.setForeground(Color.WHITE);
        today.setFont(applyFontSize(FontSize.H3));
        today.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
        add(today);

        IterateEvents(clockWidget.getCalendarInstance());

        JLabel tomorrow = new JLabel("Tomorrow");
        tomorrow.setForeground(Color.WHITE);
        tomorrow.setFont(applyFontSize(FontSize.H3));
        tomorrow.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
        add(tomorrow);

        Calendar nextDay = clockWidget.getCalendarInstance();
        nextDay.add(Calendar.DATE, 1);

        IterateEvents(nextDay);
    }

    /**
     * Get agenda data
     * Uses JsonParser for parsing the given URL
     * JsonParser returns a JSONObject
     *
     */
    public void getJSON()
    {
        json = (JSONArray)JsonParser.parseURL("http://localhost:8090/c/calendar").get("calendar");
    }

    private void IterateEvents(Calendar to)
    {
        Iterator<JSONObject> t = json.iterator();
        while(t.hasNext())
        {
            JSONObject child = t.next();

            if(getCalendarInstance(child.get("starttime").toString()).equals(to))
            {
                displayEvents(child);
            }
        }
    }

    public Calendar getCalendarInstance(String timeStamp)
    {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();

        try{
            //Date date = formatter.parse(timeStamp.substring(0, 10) + " " + timeStamp.substring(11,19));
            Date date = formatter.parse(timeStamp.substring(0, 10));
            calendar.setTime(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return calendar;
    }

    private void displayEvents(JSONObject child)
    {
        JPanel event = new JPanel();
        event.setLayout(new BoxLayout(event, BoxLayout.Y_AXIS));
        event.setBorder(new EmptyBorder(10, 10, 10, 10));
        event.setBackground(Color.BLACK);

        JLabel eventLbl = new JLabel(child.get("discription").toString() + "  " + subtractTime(child.get("starttime").toString()) + "-" + subtractTime(child.get("endtime").toString()));
        eventLbl.setForeground(Color.WHITE);
        eventLbl.setFont(applyFontSize(FontSize.H5));

        event.add(eventLbl);

        add(event);
    }

    public String subtractTime(String timeStamp)
    {
        return timeStamp.substring(11, 19);
    }
}

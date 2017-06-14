package com.smartmirror.sys.applications.widgets;

import com.smartmirror.core.view.AbstractWidget;
import org.json.simple.*;
import system.input.json.JsonParser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * System Widget for displaying time on screen
 */
public class ClockWidget extends AbstractWidget{
    public JSONObject json;
    public JLabel clock;
    public JLabel date;

    public ClockWidget()
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 20, 10));

        location = Location.CENTER_RIGHT;

        clock = new JLabel();
        clock.setForeground(Color.WHITE);
        clock.setFont(applyFontSize(FontSize.H1));
        clock.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(clock);

        date = new JLabel();
        date.setForeground(Color.WHITE);
        date.setFont(applyFontSize(FontSize.H2));
        date.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(date);

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                init();
            }
        });

        timer.start();
    }

    public void init()
    {
        getJSON();

        clock.setText(json.get("hour") +":"+ json.get("minute") +":"+ json.get("second"));
        date.setText(json.get("day_of_month") + "-" + json.get("month") + "-" + json.get("year"));
    }

    public void getJSON()
    {
        json = (JSONObject) JsonParser.parseURL("http://localhost:8090/time/Europe/Amsterdam").get("dateTime");
    }

    public Calendar getCalendarInstance()
    {
        getJSON();

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        try
        {
            Date d = formatter.parse(json.get("year").toString() +"-"+ json.get("month").toString() +"-"+ json.get("day_of_month").toString());

            calendar.setTime(d);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return calendar;
    }

    public String getNameOfDay(int addDays)
    {
        Calendar calendar = getCalendarInstance();
        calendar.add(Calendar.DATE, addDays);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String name = "";
        switch (day)
        {
            case 1:
                name = "Sunday";
                break;
            case 2:
                name = "Monday";
                break;
            case 3:
                name = "Tuesday";
                break;
            case 4:
                name = "Wednesday";
                break;
            case 5:
                name = "Thursday";
                break;
            case 6:
                name = "Friday";
                break;
            case 7:
                name = "Saturday";
        }
        return name;
    }
}

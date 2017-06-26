package com.smartmirror.sys.applications.widgets;

import com.smartmirror.core.view.AbstractWidget;
import com.smartmirror.sys.Font;
import com.smartmirror.sys.*;
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
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by basva on 9-6-2017.
 */
public class AgendaWidget extends AbstractWidget {
    private JSONObject json;
    private JSONArray array;

    private ClockWidget clockWidget;

    public final ExecutorService service = Executors.newFixedThreadPool(1);
    public Future<JSONObject> task;

    public AgendaWidget(ClockWidget clockWidget) {
        this.clockWidget = clockWidget;

        location = location.CENTER_LEFT;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    update();
                    try
                    {
                        Thread.sleep(300000);
                    }
                    catch (InterruptedException e)
                    {
                        System.out.println(e);
                    }
                }

            }
        }).start();
    }

    @Override
    public void update() {

        requestJson();

        SwingUtilities.invokeLater(() -> {
            if(json.get("calendar") != null) {
                this.removeAll();
                array = (JSONArray) json.get("calendar");

                if (clockWidget.getCalendarInstance() != null) {
                    JLabel today = new JLabel("Today");
                    today.setForeground(Color.WHITE);
                    today.setFont(Font.applyFontSize(Font.FontSize.H3));
                    today.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
                    today.setAlignmentX(Component.CENTER_ALIGNMENT);
                    add(today);

                    IterateEvents(clockWidget.getCalendarInstance());

                    JLabel tomorrow = new JLabel("Tomorrow");
                    tomorrow.setForeground(Color.WHITE);
                    tomorrow.setFont(Font.applyFontSize(Font.FontSize.H3));
                    tomorrow.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
                    tomorrow.setAlignmentX(Component.CENTER_ALIGNMENT);
                    add(tomorrow);

                    Calendar nextDay = clockWidget.getCalendarInstance();
                    nextDay.add(Calendar.DATE, 1);

                    IterateEvents(nextDay);
                } else {
                    IterateEvents(null);
                }
            }
        });
    }

    /**
     * Get agenda data
     * Uses JsonParser for parsing the given URL
     * JsonParser returns a JSONObject
     *
     */
    public JSONObject requestJson()
    {
//        task = service.submit(new JsonParser("http://localhost:8090/c/calendar"));

        task = service.submit(new JsonParser("http://192.168.1.1:8081/calendarapi/calendar"));

        try
        {
            json = task.get();
        }
        catch (InterruptedException ex)
        {
            // error
        }
        catch (ExecutionException ex)
        {
            // error
        }
        return json;
//        json = JsonParser.parseURL("http://localhost:8090/c//calendar");
    }

    private void IterateEvents(Calendar to)
    {
        if(array.size() == 0)
        {
            Iterator<JSONObject> t = array.iterator();
            while(t.hasNext())
            {
                JSONObject child = t.next();
                if(to != null)
                {
                    if(getCalendarInstance(child.get("starttime").toString()).equals(to))
                    {
                        displayEvents(child);
                    }
                }
                else
                {
                    displayEvents(child);
                }
            }
        }
        else
        {
            JLabel noEvents = new JLabel("No events to display..");
            noEvents.setForeground(Color.WHITE);
            noEvents.setAlignmentX(Component.CENTER_ALIGNMENT);
            noEvents.setFont(Font.applyFontSize(Font.FontSize.H4));
            add(noEvents);
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
        eventLbl.setFont(Font.applyFontSize(Font.FontSize.H5));
        eventLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        event.add(eventLbl);

        add(event);
    }

    public String subtractTime(String timeStamp)
    {
        return timeStamp.substring(11, 16);
    }
}

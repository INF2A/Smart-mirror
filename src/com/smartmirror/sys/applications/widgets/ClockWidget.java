package com.smartmirror.sys.applications.widgets;

import com.smartmirror.core.view.AbstractWidget;
import org.json.simple.JSONObject;
import system.input.json.JsonParser;
import com.smartmirror.sys.Font;
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
import java.util.concurrent.*;

/**
 * System Widget for displaying time on screen
 */
public class ClockWidget extends AbstractWidget{
    public JSONObject json;
    public JLabel clock;
    public JLabel date;

    public final ExecutorService service = Executors.newFixedThreadPool(1);
    public Future<JSONObject> task;

    public ClockWidget()
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 20, 10));

        location = Location.CENTER_RIGHT;

        clock = new JLabel();
        clock.setForeground(Color.WHITE);
        clock.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(clock);

        date = new JLabel();
        date.setForeground(Color.WHITE);
        date.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(date);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    update();
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        System.out.println(e);
                    }
                }

            }
        }).start();
    }

    public void update()
    {
        requestJson();

        SwingUtilities.invokeLater(() -> {
            if (json.get("dateTime") != null) {
                json = (JSONObject) json.get("dateTime");
                clock.setFont(Font.applyFontSize(Font.FontSize.H1));
                clock.setText(json.get("hour") + ":" + json.get("minute") + ":" + json.get("second"));

                date.setFont(Font.applyFontSize(Font.FontSize.H2));
                date.setText(json.get("day_of_month") + "-" + json.get("month") + "-" + json.get("year"));
            } else {
                clock.setFont(Font.applyFontSize(Font.FontSize.H5));
                clock.setText("Connection lost");

                date.setText("");
            }
        });
    }

    public JSONObject requestJson()
    {
//        task = service.submit(new JsonParser("http://localhost:8090/t/time/Europe/Amsterdam"));
        task = service.submit(new JsonParser("http://192.168.1.1:8084/timeapi/time/Europe/Amsterdam"));

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
    }

    public Calendar getCalendarInstance()
    {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        requestJson();

        if(json.get("dateTime") != null)
        {
            json = (JSONObject)json.get("dateTime");
            try
            {
                Date d = formatter.parse(json.get("year").toString() +"-"+ json.get("month").toString() +"-"+ json.get("day_of_month").toString());

                calendar.setTime(d);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            return null;
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

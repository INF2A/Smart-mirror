package com.smartmirror.sys.applications.widgets;

import com.smartmirror.core.view.AbstractWidget;
import com.smartmirror.sys.DB;
import org.json.simple.JSONObject;
import system.input.json.JsonParser;
import com.smartmirror.sys.Font;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Timer;
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

    public String currentTimeZone;

    public static boolean updated = true;

    public ClockWidget()
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 20, 10));

        location = Location.CENTER_RIGHT;

        clock = new JLabel();
        clock.setForeground(Color.WHITE);
        clock.setAlignmentX(Component.CENTER_ALIGNMENT);
        clock.setFont(Font.applyFontSize(Font.FontSize.H1));
        add(clock);

        date = new JLabel();
        date.setForeground(Color.WHITE);
        date.setAlignmentX(Component.CENTER_ALIGNMENT);
        date.setFont(Font.applyFontSize(Font.FontSize.H2));
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

                clock.setText(json.get("hour") + ":" + json.get("minute") + ":" + json.get("second"));
                clock.setForeground(Color.WHITE);

                date.setText(json.get("day_of_month") + "-" + json.get("month") + "-" + json.get("year"));
            }
        });
    }

    public JSONObject requestJson()
    {
        if(updated)
        {
            String timezone = "Europe/Amsterdam";

            DB.getConnection();
            try {
                DB.selectQuery("SELECT time_zone FROM time_zones WHERE ID IN (SELECT Timezone_ID FROM time WHERE User_ID = " + DB.id + ")");
                timezone = DB.feedback.get(1).get(0);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            currentTimeZone = timezone;
        }


        task = service.submit(new JsonParser("http://192.168.1.1:8084/timeapi/time/" + currentTimeZone));

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
        updated = false;
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

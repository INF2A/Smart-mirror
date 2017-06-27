package com.smartmirror.sys.applications.widgets;

import com.smartmirror.core.view.AbstractWidget;
import com.smartmirror.sys.DB;
import org.json.simple.JSONObject;
import system.input.json.JsonParser;
import com.smartmirror.sys.Font;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * System Widget for displaying weather information on screen
 */
public class WeatherWidget extends AbstractWidget {

    public JSONObject json; // Holds JSONObject returned from getJSON() method

    public JLabel city_name = new JLabel();
    public JLabel country = new JLabel();
    public JLabel main = new JLabel();
    public JLabel icon = new JLabel();
    public JLabel temp = new JLabel();

    public final ExecutorService service = Executors.newFixedThreadPool(1);
    public Future<JSONObject> task;

    JPanel forecastPanel = new JPanel();

    public WeatherWidget()
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 20, 10));

        location = location.CENTER_RIGHT;

        city_name.setForeground(Color.WHITE);
        city_name.setFont(Font.applyFontSize(Font.FontSize.H2));
        city_name.setAlignmentX(Component.CENTER_ALIGNMENT);

        country.setForeground(Color.WHITE);
        country.setFont(Font.applyFontSize(Font.FontSize.H2));
        country.setAlignmentX(Component.CENTER_ALIGNMENT);

        main.setForeground(Color.WHITE);
        main.setFont(Font.applyFontSize(Font.FontSize.H3));
        main.setAlignmentX(Component.CENTER_ALIGNMENT);

        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        temp.setForeground(Color.WHITE);
        temp.setFont(Font.applyFontSize(Font.FontSize.H4));
        temp.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(city_name);
        add(main);
        add(icon);
        add(temp);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    update();

                    try
                    {
                        Thread.sleep(7200000);
                    }
                    catch (InterruptedException e)
                    {
                        System.out.println(e);
                    }
                }

            }
        }).start();

    }

    /**
     * Called every update
     */
    @Override
    public void update() {
        requestJson();

        SwingUtilities.invokeLater(() -> {
            if(json.get("current") != null)
            {
                JSONObject current = (JSONObject)json.get("current");

                city_name.setText(current.get("city_name").toString() + " (" + current.get("country").toString() + ")");
                country.setText(current.get("country").toString());
                main.setText(current.get("main").toString());

                icon.setIcon(translateIcon(current.get("icon").toString(), 1.5));
                icon.setText(null);

                temp.setText("Temp: " + current.get("temp").toString());

                this.remove(forecastPanel);

                forecastPanel = new JPanel();
                forecastPanel.setLayout(new BoxLayout(forecastPanel, BoxLayout.X_AXIS));
                forecastPanel.setBackground(Color.BLACK);
                forecastPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

                this.remove(forecastPanel);

                JSONObject forecast = (JSONObject)json.get("forecast");

                for(int i = 2; i < 6; i++)
                {
                    JSONObject days = (JSONObject)forecast.get("day_" + i);

                    JPanel day = new JPanel();
                    day.setLayout(new BoxLayout(day, BoxLayout.Y_AXIS));
                    day.setBackground(Color.BLACK);
                    day.setBorder(new EmptyBorder(10, 10, 10, 10));

                    String dayTxt = " day";
                    if((i-1) >= 2)
                    {
                        dayTxt = " days";
                    }
                    JLabel d = new JLabel("+ " + (i - 1) + dayTxt);
                    d.setForeground(Color.WHITE);
                    d.setAlignmentX(Component.CENTER_ALIGNMENT);
                    d.setFont(Font.applyFontSize(Font.FontSize.H5));
                    day.add(d);

                    JLabel main = new JLabel(days.get("main").toString());
                    main.setForeground(Color.WHITE);
                    main.setFont(Font.applyFontSize(Font.FontSize.H5));
                    main.setAlignmentX(Component.CENTER_ALIGNMENT);
                    day.add(main);

                    JLabel icon = new JLabel();
                    icon.setIcon(translateIcon(days.get("icon").toString(), .8));
                    icon.setAlignmentX(Component.CENTER_ALIGNMENT);
                    icon.setText(null);
                    day.add(icon);

                    JLabel temp = new JLabel("Temp: " + days.get("temp").toString());
                    temp.setAlignmentX(Component.CENTER_ALIGNMENT);
                    temp.setFont(Font.applyFontSize(Font.FontSize.H5));
                    temp.setForeground(Color.WHITE);
                    day.add(temp);

                    forecastPanel.add(day);
                }

                add(forecastPanel);
            }
            else
            {
                main.setText("Connection lost or city not found...");
            }
        });
    }

    public ImageIcon translateIcon(String iconUrl, double sizeMultiplier)
    {
        ImageIcon icon = null;
        try{
            URL url = new URL(iconUrl);
            BufferedImage bufferedImage = ImageIO.read(url);
            icon = new ImageIcon(bufferedImage);
            Image scaledImg = icon.getImage();
            scaledImg = scaledImg.getScaledInstance((int)(Toolkit.getDefaultToolkit().getScreenResolution() * sizeMultiplier), (int)(Toolkit.getDefaultToolkit().getScreenResolution() * sizeMultiplier), Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaledImg);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return icon;
    }

    public JSONObject requestJson()
    {
        String location = "amsterdam";
        String param = "metric";

        DB.getConnection();

        ArrayList<String> fields;
        ArrayList<String> row;

        try {
            DB.selectQuery("SELECT * FROM weather WHERE User_ID = " + DB.id);
            fields = DB.feedback.get(0);
            row = DB.feedback.get(DB.id);

            location = row.get(fields.indexOf("Location"));
            String metricId = row.get(3);

            DB.selectQuery("SELECT Name FROM weather_pref WHERE ID = " + metricId);

            param = DB.feedback.get(1).get(0);


        } catch (SQLException e) {
            e.printStackTrace();
        }

        task = service.submit(new JsonParser("http://192.168.1.1:8085/weatherapi/weather/"+location+"/"+param));

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
}

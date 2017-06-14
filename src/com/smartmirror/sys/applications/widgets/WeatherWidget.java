package com.smartmirror.sys.applications.widgets;

import com.smartmirror.core.view.AbstractWidget;
import org.json.simple.JSONObject;
import system.input.json.JsonParser;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

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

    public WeatherWidget()
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 20, 10));

        location = location.CENTER_RIGHT;

        city_name.setForeground(Color.WHITE);
        city_name.setFont(applyFontSize(FontSize.H2));
        city_name.setAlignmentX(Component.CENTER_ALIGNMENT);

        country.setForeground(Color.WHITE);
        country.setFont(applyFontSize(FontSize.H2));
        country.setAlignmentX(Component.CENTER_ALIGNMENT);

        main.setForeground(Color.WHITE);
        main.setFont(applyFontSize(FontSize.H3));
        main.setAlignmentX(Component.CENTER_ALIGNMENT);

        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        temp.setForeground(Color.WHITE);
        temp.setFont(applyFontSize(FontSize.H4));
        temp.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(city_name);
        add(main);
        add(icon);
        add(temp);
    }

    /**
     * Called every update
     */
    @Override
    public void init() {
        JSONObject weather = getCurrentWeather();

        city_name.setText(weather.get("city_name").toString() + " (" + weather.get("country").toString() + ")");
        country.setText(weather.get("country").toString());
        main.setText(weather.get("main").toString());

        icon.setIcon(translateIcon(weather.get("icon").toString(), 1.5));
        icon.setText(null);

        temp.setText("Temp: " + weather.get("temp").toString());

        JPanel forecastPanel = new JPanel();
        forecastPanel.setLayout(new BoxLayout(forecastPanel, BoxLayout.X_AXIS));
        forecastPanel.setBackground(Color.BLACK);
        forecastPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        for(int i = 2; i < 6; i++)
        {
            weather = getForeCastWeather(i);

            JPanel day = new JPanel();
            day.setLayout(new BoxLayout(day, BoxLayout.Y_AXIS));
            day.setBackground(Color.BLACK);
            day.setBorder(new EmptyBorder(10, 10, 10, 10));

            JLabel d = new JLabel(new ClockWidget().getNameOfDay(i - 1));
            d.setForeground(Color.WHITE);
            d.setAlignmentX(Component.CENTER_ALIGNMENT);
            d.setFont(applyFontSize(FontSize.H5));
            day.add(d);

            JLabel main = new JLabel(weather.get("main").toString());
            main.setForeground(Color.WHITE);
            main.setFont(applyFontSize(FontSize.H5));
            main.setAlignmentX(Component.CENTER_ALIGNMENT);
            day.add(main);

            JLabel icon = new JLabel();
            icon.setIcon(translateIcon(weather.get("icon").toString(), .8));
            icon.setAlignmentX(Component.CENTER_ALIGNMENT);
            icon.setText(null);
            day.add(icon);

            JLabel temp = new JLabel("Temp: " + weather.get("temp").toString());
            temp.setAlignmentX(Component.CENTER_ALIGNMENT);
            temp.setFont(applyFontSize(FontSize.H5));
            temp.setForeground(Color.WHITE);
            day.add(temp);

            forecastPanel.add(day);
        }

        add(forecastPanel);
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

    /**
     * Get current weather from returned getJSON() method
     *
     * @return
     */
    public JSONObject getCurrentWeather()
    {
        return json = (JSONObject)JsonParser.parseURL("http://localhost:8090/w/weather/emmen/metric").get("current");
    }

    public JSONObject getForeCastWeather(int day)
    {
        json = (JSONObject)JsonParser.parseURL("http://localhost:8090/w/weather/emmen/metric").get("forecast");
        return (JSONObject)json.get("day_" + day);
    }
}

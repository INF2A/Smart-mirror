package applications;

import com.smartmirror.core.view.AbstractSystemApplication;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import org.json.simple.JSONObject;
import system.input.json.JsonParser;

/**
 * Created by basva on 25-5-2017.
 */
public class Weather extends AbstractSystemApplication{

    public JSONObject jsonWeather;

    @Override
    public void setup() {
        SYSTEM_Screen.setBackground(Color.black);
        SYSTEM_Widget = new Widget();

        JLabel weatherLbl = new JLabel("Weather options");

        JLabel weatherLocationLbl = new JLabel("Weather location");

        JTextField weatherLocationField = new JTextField();
        weatherLocationField.setPreferredSize(new Dimension(100, 20));

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> SYSTEM_closeScreen());

        SYSTEM_Screen.add(weatherLbl);
        SYSTEM_Screen.add(weatherLocationLbl);
        SYSTEM_Screen.add(weatherLocationField);
        SYSTEM_Screen.add(exitButton);

        focusComponents.add(weatherLocationField);
        focusComponents.add(exitButton);

        setSYSTEM_Widget();

        setSYSTEM_Icon("img/weather-icon.png");
    }

    @Override
    public void init() {

    }

    public void setSYSTEM_Widget(){
        getJSON();

        JSONObject weather = getWeather();

        SYSTEM_Widget.setLayout(new BoxLayout(SYSTEM_Widget, BoxLayout.Y_AXIS));
        SYSTEM_Widget.setForeground(Color.WHITE);

        SYSTEM_Widget.add(new JLabel(weather.get("city_name").toString())).setForeground(Color.WHITE);
        SYSTEM_Widget.add(new JLabel(weather.get("country").toString())).setForeground(Color.WHITE);
        SYSTEM_Widget.add(new JLabel(weather.get("main").toString())).setForeground(Color.WHITE);
        SYSTEM_Widget.add(new JLabel(weather.get("description").toString())).setForeground(Color.WHITE);

        ImageIcon weatherIcon = null;
        try{
            URL url = new URL(weather.get("icon").toString());
            BufferedImage bufferedImage = ImageIO.read(url);
            weatherIcon = new ImageIcon(bufferedImage);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        SYSTEM_Widget.add(new JLabel(weatherIcon));

        SYSTEM_Widget.add(new JLabel("Temp: " + weather.get("temp").toString())).setForeground(Color.WHITE);
        SYSTEM_Widget.add(new JLabel("Max temp: " + weather.get("temp_max").toString())).setForeground(Color.WHITE);
        SYSTEM_Widget.add(new JLabel("Min temp: " + weather.get("temp_min").toString())).setForeground(Color.WHITE);

        //SYSTEM_Widget.add(widgetPanel);
        SYSTEM_Widget_Location = location.CENTER;
        SYSTEM_Widget_Dimension = new Dimension(200,200);
    }

    public void getJSON()
    {
        //Comment setSYSTEM_Widget() in setup method if not running api.
        jsonWeather = JsonParser.parseURL("http://localhost:8090/1/weather/amsterdam/metric");
    }

    public JSONObject getWeather()
    {
        return (JSONObject)jsonWeather.get("current");
    }

    @Override
    public void onBackButton() {
        SYSTEM_closeScreen();
    }
}

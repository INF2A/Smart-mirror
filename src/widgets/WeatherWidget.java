package widgets;

import com.smartmirror.core.view.AbstractWidget;
import org.json.simple.JSONObject;
import system.input.json.JsonParser;

import javax.imageio.ImageIO;
import javax.swing.*;
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
    public JLabel description = new JLabel();
    public JLabel icon = new JLabel();
    public JLabel temp = new JLabel();
    public JLabel minTemp = new JLabel();
    public JLabel maxTemp = new JLabel();

    public WeatherWidget()
    {
        location = location.CENTER_RIGHT;
        dimension = new Dimension(300, 300);

        city_name.setForeground(Color.WHITE);
        country.setForeground(Color.WHITE);
        main.setForeground(Color.WHITE);
        description.setForeground(Color.WHITE);
        temp.setForeground(Color.WHITE);
        minTemp.setForeground(Color.WHITE);
        maxTemp.setForeground(Color.WHITE);

        add(city_name);
        add(country);
        add(main);
        add(description);
        add(icon);
        add(temp);
        add(minTemp);
        add(maxTemp);
    }

    /**
     * Called every update
     */
    @Override
    public void init() {
        getJSON();
        JSONObject weather = getCurrentWeather();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        city_name.setText(weather.get("city_name").toString());
        country.setText(weather.get("country").toString());
        main.setText(weather.get("main").toString());
        description.setText(weather.get("description").toString());

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
        icon.setIcon(weatherIcon);
        icon.setText(null);

        temp.setText("Temp: " + weather.get("temp").toString());
        minTemp.setText("Min temp: " + weather.get("temp_min").toString());
        maxTemp.setText("Max temp: " + weather.get("temp_max").toString());
    }

    /**
     * Get weather data
     * Uses JsonParser for parsing the given URL
     * JsonParser returns a JSONObject
     *
     */
    public void getJSON()
    {
        //Comment setSYSTEM_Widget() in setup method if not running api.
        json = JsonParser.parseURL("http://localhost:8090/1/weather/amsterdam/metric");
    }

    /**
     * Get current weather from returned getJSON() method
     *
     * @return
     */
    public JSONObject getCurrentWeather()
    {
        return (JSONObject)json.get("current");
    }
}

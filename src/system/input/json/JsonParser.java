package system.input.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.parser.JSONParser;
import org.json.simple.*;

/**
 * Created by basva on 24-5-2017.
 */
public class JsonParser {
    public static JSONObject parseURL(String apiURL)
    {
        JSONObject jsonObject = null;
        try
        {
            URL url = new URL(apiURL);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            JSONParser parser = new JSONParser();
            try
            {
                Object obj = parser.parse((new InputStreamReader((InputStream)request.getContent(), "UTF-8")));
                jsonObject = (JSONObject) obj;
            }
            catch (org.json.simple.parser.ParseException pe)
            {
                pe.printStackTrace();
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        return jsonObject;
    }
}

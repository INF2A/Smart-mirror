package system.input.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;

import org.json.simple.parser.JSONParser;
import org.json.simple.*;

/**
 * Created by basva on 24-5-2017.
 */
public class JsonParser implements Callable<JSONObject>{
    public String apiURL;

    public JsonParser(String apiURL)
    {
        this.apiURL = apiURL;
    }

    @Override
    public JSONObject call() throws Exception {
        JSONObject jsonObject = new JSONObject();

        if(testURL(apiURL))
        {
            HttpURLConnection request;
            try
            {
                URL url = new URL(apiURL);
                request = (HttpURLConnection) url.openConnection();
                request.connect();
            }
            catch (Exception ex)
            {
                return jsonObject;
            }

            JSONParser parser = new JSONParser();
            try
            {
                Object obj = parser.parse((new InputStreamReader((InputStream)request.getContent(), "UTF-8")));
                jsonObject = (JSONObject) obj;
            }
            catch (org.json.simple.parser.ParseException pe)
            {
                return jsonObject;
            }
            catch (IOException e)
            {
                return jsonObject;
            }
        }

        return jsonObject;
    }

    private static boolean testURL(String apiURL)
    {
        boolean succes = false;
        try
        {
            URL url = new URL(apiURL);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.setRequestMethod("HEAD");
            request.setConnectTimeout(100);
            int response = request.getResponseCode();
            if(response == 200)
            {
                succes = true;
            }
        }
        catch (IOException e)
        {
            return false;
        }

        return succes;
    }
}

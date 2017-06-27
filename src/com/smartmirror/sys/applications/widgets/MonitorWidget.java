package com.smartmirror.sys.applications.widgets;

import com.smartmirror.core.view.AbstractWidget;
import com.smartmirror.sys.applications.Monitor;
import org.json.simple.JSONObject;
import system.input.json.JsonParser;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by basva on 16-6-2017.
 */
public class MonitorWidget extends AbstractWidget {
    private JSONObject json;
    private JSONObject temp;

    private JLabel amsterdam = new JLabel();
    private JLabel chicago = new JLabel();
    private JLabel melbourne = new JLabel();
    private JLabel johannesburg = new JLabel();
    private JLabel santiago = new JLabel();
    private JLabel marie = new JLabel();
    private JLabel shanghai = new JLabel();

    public HashMap<String, JLabel> labels = new LinkedHashMap<>();

    public final ExecutorService service = Executors.newScheduledThreadPool(1);
    public Future<JSONObject> task;

    public MonitorWidget()
    {
        labels.put("amsterdam", amsterdam);
        labels.put("shanghai", shanghai);
        labels.put("chicago", chicago);
        labels.put("santiago", santiago);
        labels.put("marie", marie);
        labels.put("melbourne", melbourne);

        location = location.TOP_RIGHT;
        setLayout(new FlowLayout());

        for(JLabel label : labels.values())
        {
            label.setForeground(Color.WHITE);
            label.setVerticalTextPosition(JLabel.BOTTOM);
            label.setHorizontalTextPosition(JLabel.CENTER);
            add(label);
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    update();
                    try
                    {
                        Thread.sleep(10000);
                    }
                    catch (InterruptedException e)
                    {
                        System.out.println(e);
                    }
                }
            }
        }).start();
    }

    public void setLabels(JSONObject json)
    {
        if(json.size() != 0)
        {
            JSONObject temp;
            for(Map.Entry<String, JLabel> entry : labels.entrySet())
            {
                temp = (JSONObject)json.get(entry.getKey());
                Set<String> key = temp.keySet();
                String ip = key.toString().substring(1,12);
                entry.getValue().setIcon(translateToIcon(Boolean.parseBoolean(temp.get(ip).toString())));
                if(entry.getValue() == amsterdam)
                {
                    entry.getValue().setBorder(BorderFactory.createLineBorder(Color.YELLOW, 4));
                }
            }
        }
        else
        {
            for(Map.Entry<String, JLabel> entry : labels.entrySet())
            {
                entry.getValue().setIcon(translateToIcon(Boolean.parseBoolean("false")));
                if(entry.getValue() == amsterdam)
                {
                    entry.getValue().setBorder(BorderFactory.createLineBorder(Color.YELLOW, 4));
                }
            }
        }
    }


    @Override
    public void update() {
        requestJson();

        SwingUtilities.invokeLater(() -> {
            if(temp == null)
            {
                setLabels(json);
                temp = json;
            }

            if(!temp.equals(json))
            {
//            super.resync();
                setLabels(json);
                temp = json;
            }
        });
    }

    public ImageIcon translateToIcon(boolean connected)
    {
        ImageIcon icon = null;

        ClassLoader classLoader = getClass().getClassLoader();

        if(connected)
        {
            icon = new ImageIcon(classLoader.getResource("icon/cluster/online_.png"));
        }
        else
        {
            icon = new ImageIcon(classLoader.getResource("icon/cluster/offline_.png"));
        }

        return icon;
    }

    public JSONObject requestJson()
    {
        task = service.submit(new JsonParser("http://192.168.1.1:8086/monitorapi/monitor"));

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

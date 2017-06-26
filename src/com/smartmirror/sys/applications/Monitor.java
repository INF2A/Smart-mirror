package com.smartmirror.sys.applications;

import com.smartmirror.sys.applications.widgets.MonitorWidget;
import com.smartmirror.sys.view.AbstractSystemApplication;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import system.input.json.JsonParser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.Year;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by basva on 16-6-2017.
 */
public class Monitor extends AbstractSystemApplication {
    private JLabel title;
    private JLabel icon;

    private JPanel monitor;

    private boolean updateFinished = false;

    private Map<String, JSONObject> piDetailsMap = new LinkedHashMap<>();

    public final ExecutorService service = Executors.newScheduledThreadPool(1);
    public Future<JSONObject> task;

    private boolean currentConnectionState = false;
    private boolean previousConnectionState = false;

    @Override
    protected void setup() {
        piDetailsMap.put("amsterdam", null);
        piDetailsMap.put("shanghai", null);
        piDetailsMap.put("chicago", null);
        piDetailsMap.put("santiago", null);
        piDetailsMap.put("marie", null);
        piDetailsMap.put("melbourne", null);

        SYSTEM_Widget = new MonitorWidget();
        addWidgetToSystemWindow = true;
        setSYSTEM_Icon("img/monitor-icon.png");

        SYSTEM_Screen.setLayout(new BorderLayout());

        SYSTEM_Screen.setBackground(Color.BLACK);

        title = new JLabel("Monitor");
        title.setFont(applyFontSize(FontSize.H1));
        title.setForeground(Color.WHITE);

        icon = new JLabel(SYSTEM_Icon);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        SYSTEM_Screen.add(title, BorderLayout.PAGE_START);

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.setBackground(Color.BLACK);

        JPanel iconPanel = new JPanel();
        iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.Y_AXIS));
        iconPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconPanel.add(icon);
        iconPanel.setBackground(Color.BLACK);
        container.add(iconPanel, BorderLayout.PAGE_START);

        monitor = new JPanel();
        monitor.setLayout(new GridLayout(1,0,10,0));
        monitor.setBorder(new EmptyBorder(50, 100, 400, 100));
        monitor.setBackground(Color.BLACK);

        container.add(monitor, BorderLayout.CENTER);

        SYSTEM_Screen.add(container, BorderLayout.CENTER);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    updateInfo();

                    if(updateFinished)
                    {
                        updateMonitor();
                    }

                    if(!previousConnectionState && currentConnectionState)
                    {
                        updateAll();
                    }

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

    @Override
    public void init() {

    }

    private void updateAll()
    {
        for(Map.Entry<String, AbstractSystemApplication> entry : systemApplications.entrySet())
        {
            entry.getValue().update();
        }
    }


    @Override
    public void onBackButton() {
        SYSTEM_closeScreen();
    }

    public void updateMonitor()
    {
        SwingUtilities.invokeLater(() -> {
            monitor.removeAll();

            for (Map.Entry<String, JSONObject> entry : piDetailsMap.entrySet()) {
                if(entry.getValue() != null)
                {
                    JSONObject temp = entry.getValue();
                    Set<String> key = temp.keySet();
                    String city = key.toString().substring(1, key.toString().length()-1);

                    JPanel panel = new JPanel();
                    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                    panel.setBackground(Color.BLACK);

                    JLabel name = new JLabel(city.toUpperCase());
                    name.setFont(applyFontSize(FontSize.H3));
                    name.setAlignmentX(Component.CENTER_ALIGNMENT);
                    panel.add(name);

                    JLabel online = new JLabel("ONLINE");
                    online.setFont(applyFontSize(FontSize.H4));
                    online.setForeground(Color.WHITE);
                    online.setAlignmentX(Component.CENTER_ALIGNMENT);
                    panel.add(online);

                    JSONArray arr = (JSONArray) temp.get(city);

                    for (int i = 0; i < arr.size(); i++) {
                        JLabel api = new JLabel(arr.get(i).toString());
                        api.setAlignmentX(Component.CENTER_ALIGNMENT);
                        api.setForeground(Color.WHITE);
                        api.setFont(applyFontSize(FontSize.H6));
                        panel.add(api);
                    }

                    if (city.equals("amsterdam")) {
                        panel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 5));
                        name.setForeground(Color.YELLOW);
                    } else {
                        panel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
                        name.setForeground(Color.WHITE);
                    }

                    monitor.add(panel);

                }
            }
        });
        updateFinished = false;
    }

    public void updateInfo()
    {
        JSONObject json = SYSTEM_Widget.requestJson();

        for(Map.Entry<String, JSONObject> entry : piDetailsMap.entrySet())
        {
            piDetailsMap.replace(entry.getKey(), null);
        }

        if(json.size() != 0) {
            JSONObject temp;
            for (Map.Entry<String, JSONObject> entry : piDetailsMap.entrySet()) {
                temp = (JSONObject) json.get(entry.getKey());

                Set<String> key = temp.keySet();
                String ip = key.toString().substring(1, 12);

                JSONObject details = null;

                if (Boolean.parseBoolean(temp.get(ip).toString())) {
                    task = service.submit(new JsonParser("http://192.168.1.1:8086/monitorapi/monitor/" + entry.getKey()));

                    try
                    {
                        details = task.get();
                    }
                    catch (InterruptedException ex)
                    {
                        // error
                    }
                    catch (ExecutionException ex)
                    {
                        // error
                    }

                    entry.setValue(details);
                }
            }
            updateFinished = true;

            previousConnectionState = currentConnectionState;
            currentConnectionState = true;
        }
        else
        {
            previousConnectionState = currentConnectionState;
            currentConnectionState = false;
        }
    }
}

package com.smartmirror.sys.applications.widgets;

import com.smartmirror.core.view.AbstractWidget;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import system.input.json.JsonParser;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * System Widget for displaying news on screen
 */
public class NewsWidget extends AbstractWidget {

    private JSONObject json;

    private volatile boolean isUpdating = false;

    private List<String> titles;
    private List<String> descriptions;
    private List<BufferedImage> images;
    private List<String> publishes;

    private volatile int x = Toolkit.getDefaultToolkit().getScreenSize().width;

    public final ExecutorService service = Executors.newFixedThreadPool(1);
    public Future<JSONObject> task;

    public NewsWidget() {
        setLayout(new FlowLayout());
        this.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, (Toolkit.getDefaultToolkit().getScreenSize().height / 100) * 20));
        this.setForeground(Color.WHITE);

        location = location.BOTTOM;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    isUpdating = true;
                    update();
                    isUpdating = false;
                    try
                    {
                        Thread.sleep(1800000);
                    }
                    catch (InterruptedException e)
                    {
                        System.out.println(e);
                    }
                }
            }
        }).start();

        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePosition();
            }
        });
        timer.start();
    }


    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if(!isUpdating) {
            int totalWidth = 0;

            if (titles != null && descriptions != null && images != null && publishes != null) {
                for (int i = 0; i < titles.size(); i++) {
                    if (i == titles.size() - 1) {
                        if (x <= -totalWidth) {
                            x = Toolkit.getDefaultToolkit().getScreenSize().width;
                            System.out.println(i);
                        }
                    }

                    int xPos = x + (i * ((Toolkit.getDefaultToolkit().getScreenSize().width / 100) * 60));

                    g.setFont(applyFontSize(FontSize.H3));

                    List<String> substrTitles = splitString(titles.get(i), 50);

                    for (int t = 0; t < substrTitles.size(); t++) {
                        g.drawString(substrTitles.get(t),xPos , 30 + (t * 30));
                    }

                    g.setFont(applyFontSize(FontSize.H6));
                    g.drawString(publishes.get(i), xPos, (substrTitles.size() * 30) + 20);

                    List<String> subtrDescription = splitString(descriptions.get(i), 40);

                    g.setFont(applyFontSize(FontSize.H4));

                    for (int j = 0; j < subtrDescription.size(); j++) {
                        if (80 + (j + 1) * 30 > this.getHeight()) {
                            g.drawString(subtrDescription.get(j) + " ...", xPos, ((substrTitles.size() * 30) + 50) + j * 30);
                        } else {
                            g.drawString(subtrDescription.get(j), xPos, ((substrTitles.size() * 30) + 50) + j * 30);
                        }
                    }

                    BufferedImage bufferedImage = images.get(i);
                    int imgWidth = bufferedImage.getWidth();
                    int imgHeight = bufferedImage.getHeight();

                    Dimension imgSize = new Dimension(imgWidth, imgHeight);
                    Dimension boundary = new Dimension(200, 200);
                    Dimension scaled = getScaledDimension(imgSize, boundary);

                    g.drawImage(bufferedImage, xPos + (g.getFontMetrics().stringWidth(subtrDescription.get(0)) + 50), (substrTitles.size() * 30) + 10, scaled.width, scaled.height, null);

                    totalWidth += g.getFontMetrics().stringWidth(substrTitles.get(0)) + scaled.width;
                    totalWidth += (Toolkit.getDefaultToolkit().getScreenSize().width / 100) * 30;
                }
            }
        }
    }

    public List<String> splitString(String input, int size)
    {
        List<String> stringList = new ArrayList<>();
        int index = 0;
        while(index < input.length())
        {
            stringList.add(input.substring(index, Math.min(index + size, input.length())));
            index += size;
        }

        return stringList;
    }

    public void updatePosition(){
        x -= 6;
        this.repaint();
    }


    /**
     * Called every update
     * Removes all news items and re-adds updated news items
     */
    @Override
    public void update() {


        titles = new ArrayList<>();
        descriptions = new ArrayList<>();
        images = new ArrayList<>();
        publishes = new ArrayList<>();

        requestJson();

        if(json != null)
        {
            if(json.get("feed") != null)
            {
                JSONArray news = (JSONArray)json.get("feed");
                Iterator<JSONObject> t = news.iterator();
                while (t.hasNext()) {
                    JSONObject child = t.next();

                    titles.add(child.get("title").toString());

                    descriptions.add(child.get("description").toString());

                    BufferedImage bufferedImage = null;

                    try {
                        URL url = new URL(child.get("picture").toString());
                        bufferedImage = ImageIO.read(url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    images.add(bufferedImage);
                    publishes.add(child.get("pubDate").toString());
                }
            }
        }
    }

    public Dimension getScaledDimension(Dimension imgSize, Dimension boundary)
    {
        int original_width = imgSize.width;
        int original_height = imgSize.height;

        int bound_width = boundary.width;
        int bound_height = boundary.height;

        int new_width = original_width;
        int new_height = original_height;

        if(original_width > bound_width)
        {
            new_width = bound_width;
            new_height = (new_width * original_height) / original_width;
        }

        if(new_height > bound_height)
        {
            new_height = bound_height;
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }

    /**
     * Get news data
     * Uses JsonParser for parsing the given URL
     * JsonParser returns a JSONObject
     *
     */
    public JSONObject requestJson()
    {
//        task = service.submit(new JsonParser("http://localhost:8090/n/news/"));
        task = service.submit(new JsonParser("http://192.168.1.1:8080/newsapi/news/bbc/"));

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
//        json = JsonParser.parseURL("http://localhost:8090/n/news/Europe/Amsterdam");
    }
}

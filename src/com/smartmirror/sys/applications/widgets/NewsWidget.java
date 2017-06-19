package com.smartmirror.sys.applications.widgets;

import com.smartmirror.core.view.AbstractWidget;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import system.input.json.JsonParser;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * System Widget for displaying news on screen
 */
public class NewsWidget extends AbstractWidget {
    private JSONArray json;
    private int totalWidth = 0;
    private double width;
//    private int[] original_xpos;
//    private int[] new_xpos;
    private List<Component> components;
    int x = 200;

    public NewsWidget()
    {
        setLayout(new FlowLayout());

        Component[] components = NewsWidget.this.getComponents();

        int x;
//        original_xpos = new int[5];

        JLabel tmpLabel = null;

        for (x = 0; x < 5; ++x) {
            tmpLabel = new JLabel("NIEUWS" + (x + 1));
            this.totalWidth = (int) ((double) this.totalWidth + tmpLabel.getPreferredSize().getWidth());
            this.width = (double) tmpLabel.getPreferredSize().getWidth();
            System.out.println(totalWidth);
            NewsWidget.this.add(tmpLabel);
//            original_xpos[x] = tmpLabel.getLocation().x;
//            new_xpos = original_xpos;
        }


        location = location.BOTTOM;

        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                init();
            }
        });

        timer.start();
    }


    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.drawString("testafafaefawfawf", x, 5);
    }

    /**
     * Called every update
     * Removes all news items and re-adds updated news items
     */
    @Override
    public void init() {
//        int i = 0;
        x -= 3;
        this.repaint();
    }

//        NewsWidget.this.removeAll();
//
//        getJSON();
//        Iterator<JSONObject> t = json.iterator();
//        while(t.hasNext())
//        {
//            JSONObject child = t.next();
//
//            JPanel itemPanel = new JPanel();
//            itemPanel.setPreferredSize(new Dimension((Toolkit.getDefaultToolkit().getScreenResolution() * 5), (Toolkit.getDefaultToolkit().getScreenResolution() * 2)));
//            itemPanel.setLayout(new BorderLayout(10,10));
//            itemPanel.setBackground(Color.BLACK);
//            itemPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
//            add(itemPanel);
//
//            JTextArea title = new JTextArea(child.get("title").toString());
//            title.setFont(applyFontSize(FontSize.H5));
//            title.setForeground(Color.WHITE);
//            title.setLineWrap(true);
//            title.setWrapStyleWord(true);
//            title.setOpaque(false);
//            title.setEditable(false);
//            itemPanel.add(title, BorderLayout.NORTH);
//
//            JTextArea description = new JTextArea(child.get("description").toString());
//            description.setFont(applyFontSize(FontSize.H6));
//            description.setForeground(Color.WHITE);
//            description.setLineWrap(true);
//            description.setWrapStyleWord(true);
//            description.setOpaque(false);
//            description.setEditable(false);
//            itemPanel.add(description, BorderLayout.CENTER);
//
//            JLabel newsImage = new JLabel();
//            ImageIcon image = null;
//            try{
//                URL url = new URL(child.get("picture").toString());
//                BufferedImage bufferedImage = ImageIO.read(url);
//                int height = bufferedImage.getHeight();
//                int width = bufferedImage.getWidth();
//                image = new ImageIcon(bufferedImage);
//                Image scaledImg = image.getImage();
//                scaledImg = scaledImg.getScaledInstance((int)(Toolkit.getDefaultToolkit().getScreenResolution() * 1.5), (int)(Toolkit.getDefaultToolkit().getScreenResolution() * 1.5), Image.SCALE_SMOOTH);
//                image = new ImageIcon(scaledImg);
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//            newsImage.setIcon(image);
//            newsImage.setText(null);
//            itemPanel.add(newsImage, BorderLayout.LINE_END);
//
//            JLabel publish = new JLabel(child.get("pubDate").toString());
//            publish.setFont(applyFontSize(FontSize.H7));
//            publish.setForeground(Color.WHITE);
//            itemPanel.add(publish, BorderLayout.SOUTH);
//        }



    /**
     * Get news data
     * Uses JsonParser for parsing the given URL
     * JsonParser returns a JSONObject
     *
     */
    public void getJSON()
    {
        json = (JSONArray) JsonParser.parseURL("http://localhost:8090/a/news").get("feed");
    }

}

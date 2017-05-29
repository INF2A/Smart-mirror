package application;

import com.smartmirror.core.view.AbstractApplication;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by basva on 25-5-2017.
 */
public class Settings extends AbstractApplication{
    public Settings(Map<String, AbstractApplication> apps)
    {
        getWidget();

        getPanel(apps);
    }

    private void getWidget()
    {
        SYSTEM_Widget.setOpaque(false);
        SYSTEM_Widget.setPreferredSize(new Dimension(25,25));
        BufferedImage icon = null;
        try
        {
            icon = ImageIO.read(new File("D:\\Gebruikers\\basva\\OneDrive\\STENDEN\\Leerjaar2\\Periode 4\\PROJECT\\SlimPiV2\\resources\\img\\settings.png"));
        }
        catch (IOException e)
        {

        }
        JLabel img = new JLabel(new ImageIcon(icon));
        SYSTEM_Widget.add(img);
        SYSTEM_Widget_Location = location.TOP;
    }

    private void getPanel(Map<String, AbstractApplication> apps)
    {
        SYSTEM_Screen.setLayout(new BorderLayout());

        JPanel bottom = new JPanel();
        bottom.setLayout(new FlowLayout());

        JButton saveButton = new JButton("Save settings");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SYSTEM_closeScreen();
            }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SYSTEM_closeScreen();
            }
        });

        bottom.add(saveButton);
        bottom.add(exitButton);

        JPanel center = new JPanel();
        center.setLayout(new GridLayout(0,1));

        for(Map.Entry<String, AbstractApplication> entry : apps.entrySet())
        {
            if(!entry.getKey().equals("settings"))
            {
                center.add(entry.getValue().SYSTEM_Screen, BorderLayout.CENTER);
                focusComponents.addAll(entry.getValue().focusComponents);
            }
        }

        //center.add(apps.get("weather").SYSTEM_Screen, BorderLayout.CENTER);

        SYSTEM_Screen.add(center, BorderLayout.CENTER);
        SYSTEM_Screen.add(bottom, BorderLayout.SOUTH);

        focusComponents.add(saveButton);
        focusComponents.add(exitButton);
    }
}

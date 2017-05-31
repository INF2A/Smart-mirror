package applications;

import com.smartmirror.core.view.AbstractSystemApplication;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by basva on 25-5-2017.
 */
public class Settings extends AbstractSystemApplication{

    //
//    /**
//     * Will be called only once.
//     * Call will be made when the application starts
//     *
    @Override
    public void setup() {
        SYSTEM_Widget.setBackground(Color.BLACK);
        SYSTEM_Screen.setLayout(new BorderLayout());
    }

    @Override
    public void init() {
        SYSTEM_Screen.removeAll();
        focusComponents.removeAll(focusComponents);
        focusComponents.add(SYSTEM_Screen);
        INTERNAL_requestSystemApplications();
        getPanel(systemApplications);
    }

    private void getWidget()
    {
        SYSTEM_Widget.setOpaque(false);
        SYSTEM_Widget.setPreferredSize(new Dimension(25,25));
        BufferedImage icon;
        JLabel img = new JLabel();
        try
        {
            icon = ImageIO.read(getClass().getResource("img/settings.png"));
            img.setIcon(new ImageIcon(icon));
        }
        catch (IOException e)
        {

        }
        SYSTEM_Widget.add(img);
        SYSTEM_Widget_Location = location.TOP;
    }

    private void getPanel(Map<String, AbstractSystemApplication> apps)
    {
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

        for(Map.Entry<String, AbstractSystemApplication> entry : apps.entrySet())
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

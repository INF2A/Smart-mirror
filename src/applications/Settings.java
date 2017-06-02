package applications;

import com.smartmirror.core.view.AbstractSystemApplication;
import com.sun.scenario.effect.impl.sw.java.JSWBlend_COLOR_BURNPeer;

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
        SYSTEM_Widget = new Widget();

        SYSTEM_Screen.setBackground(Color.BLACK);
        setWidget();
    }

    @Override
    public void init() {
        SYSTEM_Screen.removeAll();
        focusComponents.removeAll(focusComponents);
        focusComponents.add(SYSTEM_Screen);
        INTERNAL_requestSystemApplications();
        getPanel(systemApplications);
    }

    private void setWidget()
    {
        SYSTEM_Widget.setPreferredSize(new Dimension(25,25));
        ClassLoader classLoader = getClass().getClassLoader();
        ImageIcon image = new ImageIcon(classLoader.getResource("img/settings.png"));
        JLabel img = new JLabel(image);
        SYSTEM_Widget.add(img);
        SYSTEM_Widget_Location = location.TOP;
    }

    private void getPanel(Map<String, AbstractSystemApplication> apps)
    {
        JPanel bottom = new JPanel();
        bottom.setLayout(new FlowLayout());

        JButton saveButton = new JButton("Save settings");
        saveButton.addActionListener(e -> SYSTEM_closeScreen());

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> SYSTEM_closeScreen());


        for(Map.Entry<String, AbstractSystemApplication> entry : apps.entrySet())
        {
            if(!entry.getKey().equals("settings"))
            {
                JButton iconButton = new JButton(entry.getValue().SYSTEM_Icon);

                //ADD ACTIONLISTENER SWITCH TO SELECTED APP.

                iconButton.setBackground(Color.BLACK);
                SYSTEM_Screen.add(iconButton);
                focusComponents.add(iconButton);
            }
        }
        SYSTEM_Screen.add(saveButton);
        SYSTEM_Screen.add(exitButton);

        focusComponents.add(saveButton);
        focusComponents.add(exitButton);
    }

    @Override
    public void onBackButton() {
        SYSTEM_closeScreen();
    }
}

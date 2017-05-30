package applications;

import com.smartmirror.core.view.AbstractSystemApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by basva on 25-5-2017.
 */
public class Weather extends AbstractSystemApplication{

    @Override
    public void setup() {
        SYSTEM_Screen.setBackground(Color.black);

        JLabel weatherLbl = new JLabel("Weather options");

        JLabel weatherLocationLbl = new JLabel("Weather location");

        JTextField weatherLocationField = new JTextField();
        weatherLocationField.setPreferredSize(new Dimension(100, 20));

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SYSTEM_closeScreen();
            }
        });

        SYSTEM_Screen.add(weatherLbl);
        SYSTEM_Screen.add(weatherLocationLbl);
        SYSTEM_Screen.add(weatherLocationField);
        SYSTEM_Screen.add(exitButton);

        focusComponents.add(weatherLocationField);
        focusComponents.add(exitButton);
        SYSTEM_Widget.add(new JLabel("weather"));

    }

    @Override
    public void init() {

    }
}

package application;

import system.AbstractApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Erwin on 5/22/2017.
 */
public class Clock extends AbstractApplication {

    JLabel tijdLabel;
    String tijd = "10:00";
    public Clock()
    {
        tijdLabel = new JLabel(tijd);

        JTextField tijdAanpas = new JTextField();
        tijdAanpas.setPreferredSize(new Dimension(100, 20));

        JButton tijdOke = new JButton("Pas het aan!");
        tijdOke.addActionListener( e -> tijdLabel.setText(tijdAanpas.getText()));

        focusComponents.add(tijdAanpas);
        focusComponents.add(tijdOke);

        SYSTEM_Screen.add(tijdLabel);
        SYSTEM_Screen.add(tijdAanpas);
        SYSTEM_Screen.add(tijdOke);
    }

    @Override
    public void onBackButton()
    {
        super.onBackButton();
        SYSTEM_closeScreen();
    }

}

package application;

import system.AbstractApplication;
import system.view.AbstractWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Erwin on 5/16/2017.
 */
public class WindowPluginTest extends AbstractApplication {

    private JLabel label;
    private JButton exitButton;

    public WindowPluginTest(String name)
    {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(200, 20));
        JTextField field2 = new JTextField();
        field2.setPreferredSize(new Dimension(200, 20));
        JTextField field3 = new JTextField();
        field3.setPreferredSize(new Dimension(200, 20));
        label = new JLabel(name);

        exitButton = new JButton("exit app");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SYSTEM_closeScreen();
            }
        });

        SYSTEM_Screen.add(label);

        SYSTEM_Screen.add(field);
        SYSTEM_Screen.add(field2);
        SYSTEM_Screen.add(field3);

        SYSTEM_Screen.add(exitButton);

        focusComponents.add(field);
        focusComponents.add(field2);
        focusComponents.add(field3);
        focusComponents.add(exitButton);


        // SYSTEM_FocusManager.addFocusableComponent(field);

    }

    @Override
    public void onButtonPressed() {
        super.onButtonPressed();
        label.setText("Plugin press test");
    }

    @Override
    public void onButtonReleased() {
        super.onButtonReleased();
        label.setText("Other ubtton");
    }

}

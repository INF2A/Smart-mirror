package system.view;

import system.AbstractApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Erwin on 5/15/2017.
 */
public class SystemWindow extends AbstractSystemWindow {

    private JLabel label;

    public SystemWindow()
    {
        label = new JLabel("System window - view widgets and open apps from here");

        SYSTEM_Screen.add(label);
    }

    public void addApplication(String appName, AbstractApplication app){
        apps.put(appName, app);

        JPanel t = new JPanel();
        t.setName(appName);
        t.setPreferredSize(new Dimension(50,50));
        t.setBackground(Color.GREEN);
        focusComponents.add(t);
        SYSTEM_Screen.add(t);
    }

    @Override
    public void onButtonPressed() {
        super.onButtonPressed();
        label.setText("press button");
    }

    @Override
    public void onButtonReleased() {
        super.onButtonReleased();
        label.setText("release button");
    }

    @Override
    public void onMenuButton(){
        super.onMenuButton();
        if(SYSTEM_Screen.hasFocus()) {
            //open menu
        }
    }

    @Override
    public void onBackButton(){
        super.onBackButton();
    }


}

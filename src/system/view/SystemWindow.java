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

    Map<String, AbstractApplication> apps;
    public AbstractApplication selectedApp;
    Map<String, Widget> widgets;

    private JLabel label;


    public SystemWindow()
    {
        apps = new HashMap<>();
        label = new JLabel("System window - view widgets and open apps from here");

        SYSTEM_Screen.add(label);
    }

    private JButton INTERNAL_WindowChangeHandle = new JButton();
    public void INTERNAL_addWindowChangeListener(ActionListener al)
    {
        INTERNAL_WindowChangeHandle.addActionListener(al);
    }

    public void INTERNAL_closeKeyboard()
    {
        INTERNAL_keyboardActive = false;
        INTERNAL_KeyboardCloseHandle.doClick();
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
        label.setText("press button");
    }

    @Override
    public void onButtonReleased() {
        label.setText("release button");
    }

    @Override
    public void onMenuButton(){
        if(SYSTEM_Screen.hasFocus()) {
            //open menu
        } else {
            if(focusComponents.get(currentComponent) instanceof JPanel) {
                if((focusComponents.get(currentComponent)).getName().equals("test1")) {
                    selectedApp = apps.get("test1");
                    INTERNAL_WindowChangeHandle.doClick();
                }
                if((focusComponents.get(currentComponent)).getName().equals("test2")) {
                    selectedApp = apps.get("test2");
                    INTERNAL_WindowChangeHandle.doClick();
                }
            }
        }
    }

    @Override
    public void onBackButton(){
    }


}

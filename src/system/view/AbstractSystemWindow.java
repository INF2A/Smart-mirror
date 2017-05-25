package system.view;

import system.AbstractApplication;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Erwin on 5/22/2017.
 */
public abstract class AbstractSystemWindow extends AbstractWindow {

    Map<String, AbstractApplication> apps = new HashMap<>();
    public AbstractApplication selectedApp;
    Map<String, Widget> widgets;

    private JButton INTERNAL_WindowChangeHandle = new JButton();
    public void INTERNAL_addWindowChangeListener(ActionListener al)
    {
        INTERNAL_WindowChangeHandle.addActionListener(al);
    }

    @Override
    public void onMenuButton(){
        if(focusComponents.get(currentComponent) instanceof JPanel) {
            if((focusComponents.get(currentComponent)).getName().equals("test1")) {
                selectedApp = apps.get("test1");
                INTERNAL_WindowChangeHandle.doClick();
            }
            if((focusComponents.get(currentComponent)).getName().equals("test2")) {
                selectedApp = apps.get("test2");
                INTERNAL_WindowChangeHandle.doClick();
            }

            if((focusComponents.get(currentComponent)).getName().equals("clock")) {
                selectedApp = apps.get("clock");
                INTERNAL_WindowChangeHandle.doClick();
            }
        }
    }
}

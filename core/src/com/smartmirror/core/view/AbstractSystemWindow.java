package com.smartmirror.core.view;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Erwin on 5/22/2017.
 */
public abstract class AbstractSystemWindow extends AbstractWindow {

    public Map<String, AbstractApplication> apps = new HashMap<>();
    public AbstractApplication selectedApp;

    private JButton INTERNAL_WindowChangeHandle = new JButton();
    public void INTERNAL_addWindowChangeListener(ActionListener al)
    {
        INTERNAL_WindowChangeHandle.addActionListener(al);
    }


    @Override
    public void onMenuButton(){
        if(focusComponents.get(currentComponent) instanceof JPanel) {
            for (String s : apps.keySet()){
                if((focusComponents.get(currentComponent)).getName().equals(s)) {
                    selectedApp = apps.get(s);
                    INTERNAL_WindowChangeHandle.doClick();
                }
            }
        }
    }
}

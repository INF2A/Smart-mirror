package com.smartmirror.sys.view;

import com.smartmirror.core.view.AbstractApplication;
import com.smartmirror.core.view.AbstractWindow;
import com.smartmirror.core.view.IFocusManager;
import com.smartmirror.sys.MainSystemController;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Erwin on 5/22/2017.
 */
public abstract class AbstractSystemWindow extends AbstractWindow {
    volatile public Map<String, AbstractApplication> apps = new HashMap<>();
    volatile public AbstractApplication selectedApp;

    final private JButton INTERNAL_WindowChangeHandle = new JButton();

    public void INTERNAL_addWindowChangeListener(ActionListener al)
    {
        INTERNAL_WindowChangeHandle.addActionListener(al);
    }


    @Override
    public void onMenuButton(){
        super.onMenuButton();
        if(focusManager.getSelectedComponentType().equals(IFocusManager.TYPE.PANEL)) {
            for (String s : apps.keySet()) {
                if(((JPanel) focusManager.Selected()).getName().equals((s))) {
                    selectedApp = apps.get(s);
                    INTERNAL_WindowChangeHandle.doClick();
                }
            }
        }
    }
}

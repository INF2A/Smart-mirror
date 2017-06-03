package com.smartmirror.sys.view;

import com.smartmirror.core.view.AbstractApplication;
import com.smartmirror.sys.MainSystemController;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Erwin on 5/29/2017.
 */
public abstract class AbstractSystemApplication extends AbstractApplication {

    public Map<String, AbstractSystemApplication> systemApplications = new HashMap<>();

    public JButton INTERNAL_requestSystemApplicationListHandle = new JButton();

    public MainSystemController systemController;
    public void setSystemController(MainSystemController systemController) {
        this.systemController = systemController;
    }

    @Deprecated
    public void INTERNAL_setSystemApplicationList(Map<String, AbstractSystemApplication> map){
        systemApplications = map;
    }

    @Deprecated
    public void INTERNAL_addRequestSystemApplicationListActionListener(ActionListener al) {
        INTERNAL_requestSystemApplicationListHandle.addActionListener(al);
    }

    @Deprecated
    public void INTERNAL_requestSystemApplications() {
        INTERNAL_requestSystemApplicationListHandle.doClick();
    }
}

package com.smartmirror.sys;

import applications.Widget;
import com.smartmirror.core.view.AbstractApplication;
import com.smartmirror.sys.view.*;
import com.smartmirror.sys.view.FocusManager;
import widgets.ClockWidget;
import widgets.DefaultWidget;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by Erwin on 5/15/2017.
 */
public class SystemWindow extends AbstractSystemWindow {

    private JLabel label;

    private JPanel top;
    private JPanel center;
    private JPanel centerLeft;
    private JPanel centerRight;
    private JPanel bottom;

    public SystemWindow()
    {
        focusManager = new FocusManager();

        SYSTEM_Screen.setBackground(Color.BLACK);

        SYSTEM_Screen.setLayout(new BorderLayout());
        top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(Color.BLACK);
        SYSTEM_Screen.add(top, BorderLayout.NORTH);

        center = new JPanel();
        center.setBackground(Color.BLACK);
        SYSTEM_Screen.add(center, BorderLayout.CENTER);

        centerLeft = new JPanel();
        centerLeft.setBackground(Color.BLACK);
        centerLeft.setLayout(new BoxLayout(centerLeft, BoxLayout.Y_AXIS));
        SYSTEM_Screen.add(centerLeft, BorderLayout.LINE_START);

        centerRight = new JPanel();
        centerRight.setBackground(Color.BLACK);
        centerRight.setLayout(new BoxLayout(centerRight, BoxLayout.Y_AXIS));
        SYSTEM_Screen.add(centerRight, BorderLayout.LINE_END);

        bottom = new JPanel();
        bottom.setBackground(Color.BLACK);
        SYSTEM_Screen.add(bottom, BorderLayout.SOUTH);
    }

    public void addApplicationToWindow(String appName, AbstractApplication app){
        apps.put(appName, app);

        if(app.SYSTEM_Widget == null)
        {
            app.SYSTEM_Widget = new DefaultWidget();
        }

        app.SYSTEM_Widget.setName(appName);

        focusManager.addComponent(app.SYSTEM_Widget);

        switch (app.SYSTEM_Widget.location)
        {
            case TOP:
                top.add(app.SYSTEM_Widget);
                break;
            case BOTTOM:
                bottom.add(app.SYSTEM_Widget);
                break;
            case CENTER_LEFT:
                centerLeft.add(app.SYSTEM_Widget);
                break;
            case CENTER_RIGHT:
                centerRight.add(app.SYSTEM_Widget);
                break;
            case CENTER:
                default:
                center.add(app.SYSTEM_Widget);
                break;
        }
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

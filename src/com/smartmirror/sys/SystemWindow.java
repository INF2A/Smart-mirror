package com.smartmirror.sys;

import applications.Widget;
import com.smartmirror.core.view.AbstractApplication;
import com.smartmirror.core.view.AbstractSystemWindow;

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
    private JPanel bottom;
    //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();


    public SystemWindow()
    {
        // = new JLabel("System window - view widgets and open apps from here");
        //enter.add(label);
        //SYSTEM_Screen.setPreferredSize(new Dimension(screenSize));

        SYSTEM_Screen.setBackground(Color.BLACK);

        SYSTEM_Screen.setLayout(new BorderLayout());
        top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(Color.BLACK);
        SYSTEM_Screen.add(top, BorderLayout.NORTH);

        center = new JPanel();
        center.setBackground(Color.BLACK);
        center.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;


        SYSTEM_Screen.add(center, BorderLayout.CENTER);

        bottom = new JPanel();
        bottom.setBackground(Color.BLACK);
        SYSTEM_Screen.add(bottom, BorderLayout.SOUTH);
    }

    public void addApplicationToWindow(String appName, AbstractApplication app, Dimension dimension){
        apps.put(appName, app);
        //app.SYSTEM_Widget = new Widget();
        if(app.SYSTEM_Widget == null)
        {
            app.SYSTEM_Widget = new Widget();
        }

        app.SYSTEM_Widget.setName(appName);
        app.SYSTEM_Widget.setPreferredSize(dimension);

//        JPanel t = new JPanel();
//        t.setOpaque(false);
//        t.setName(appName);
//        t.setPreferredSize(app.SYSTEM_Widget.getPreferredSize());
//        t.setBackground(Color.GREEN);
//        t.add(app.SYSTEM_Widget);
        focusComponents.add(app.SYSTEM_Widget);
        if(app.SYSTEM_Widget_Location == AbstractApplication.location.TOP)
        {
            top.add(app.SYSTEM_Widget);
        }
        else if (app.SYSTEM_Widget_Location == AbstractApplication.location.CENTER)
        {
            center.add(app.SYSTEM_Widget);
        }
        else if (app.SYSTEM_Widget_Location == AbstractApplication.location.BOTTOM)
        {
            bottom.add(app.SYSTEM_Widget);
        }
        else
        {
            center.add(app.SYSTEM_Widget);
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

package com.smartmirror.sys.applications;

import com.smartmirror.sys.applications.widgets.ClockWidget;
import com.smartmirror.sys.view.AbstractSystemApplication;
//import widgets.ClockWidget;

import javax.swing.*;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * System application - used for displaying time
 */
public class Clock extends AbstractSystemApplication{
    private ScheduledExecutorService scheduledExecutorService; // Used as timer

    /**
     * Will only run ones when application is started
     * Define application startup settings here
     *
     * SYSTEM_Screen functions as base JPanel for the application
     */
    public void setup()
    {
        setSYSTEM_Icon("img/clock-icon.png");   // Set application icon - will be displayed in settings

        SYSTEM_Widget = new ClockWidget();  // Instantiate corresponding widget
    }

    @Override
    public void init() {

    }

    @Override
    public void onBackButton() {
        SYSTEM_closeScreen();
    }
}

package com.smartmirror.sys.applications;

import com.smartmirror.sys.view.AbstractSystemApplication;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.smartmirror.sys.applications.widgets.WeatherWidget;

/**
 * System application - used for displaying current and forecast weather
 */
public class Weather extends AbstractSystemApplication{
    private ScheduledExecutorService scheduledExecutorService; // Used as timer

    /**
     * Will only run ones when application is started
     * Define application startup settings here
     *
     * SYSTEM_Screen functions as base JPanel for the application
     */
    @Override
    public void setup() {
        SYSTEM_Screen.setBackground(Color.black);

        JLabel weatherLbl = new JLabel("Weather options");

        JLabel weatherLocationLbl = new JLabel("Weather location");

        JTextField weatherLocationField = new JTextField();
        weatherLocationField.setPreferredSize(new Dimension(100, 20));

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> SYSTEM_closeScreen());

        SYSTEM_Screen.add(weatherLbl);
        SYSTEM_Screen.add(weatherLocationLbl);
        SYSTEM_Screen.add(weatherLocationField);
        SYSTEM_Screen.add(exitButton);

        focusManager.addComponent(weatherLocationField);
        focusManager.addComponent(exitButton);

        setSYSTEM_Icon("img/weather-icon.png");

        SYSTEM_Widget = new WeatherWidget(); // Instantiate corresponding widget

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() { // Define new Runnable
            @Override
            public void run() {
                // Define stuff to update
                SYSTEM_Widget.init();
            }
        }, 0,1, TimeUnit.HOURS); // set timer
    }

    @Override
    public void init() {

    }


    @Override
    public void onBackButton() {
        SYSTEM_closeScreen();
    }
}

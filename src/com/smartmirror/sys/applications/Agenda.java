package com.smartmirror.sys.applications;

import com.smartmirror.sys.view.AbstractSystemApplication;
import com.smartmirror.sys.applications.widgets.AgendaWidget;
import com.smartmirror.sys.applications.widgets.ClockWidget;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by basva on 9-6-2017.
 */
public class Agenda extends AbstractSystemApplication {
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    protected void setup() {
        SYSTEM_Widget = new AgendaWidget(new ClockWidget());
        setSYSTEM_Icon("img/weather-icon.png");

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                SYSTEM_Widget.init();
            }
        }, 0, 1, TimeUnit.HOURS);
    }

    @Override
    public void init() {
        System.out.println("Agenda");
    }

    @Override
    public void onBackButton() {
        super.onBackButton();
        SYSTEM_closeScreen();
    }
}

package com.smartmirror.sys.applications;

import com.smartmirror.sys.view.AbstractSystemApplication;
import widgets.NewsWidget;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by basva on 9-6-2017.
 */
public class News extends AbstractSystemApplication {

    private ScheduledExecutorService scheduledExecutorService;

    @Override
    protected void setup() {
        SYSTEM_Widget = new NewsWidget();


    }

    @Override
    public void init() {

    }
}

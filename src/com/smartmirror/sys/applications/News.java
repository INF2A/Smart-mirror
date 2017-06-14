package com.smartmirror.sys.applications;

import com.smartmirror.sys.view.AbstractSystemApplication;
import com.smartmirror.sys.applications.widgets.NewsWidget;

import java.util.concurrent.ScheduledExecutorService;

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

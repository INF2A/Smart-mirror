package com.smartmirror.sys.applications;

import com.smartmirror.sys.applications.widgets.AudioClient;
import com.smartmirror.sys.view.AbstractSystemApplication;

/**
 * Created by basva on 20-6-2017.
 */
public class Radio extends AbstractSystemApplication {
    @Override
    protected void setup() {
        SYSTEM_Widget = new AudioClient();
        addWidgetToSystemWindow = true;
    }

    @Override
    public void init() {

    }
}

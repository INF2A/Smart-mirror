package com.smartmirror.sys.view.window;

import com.smartmirror.core.view.AbstractWindow;

import java.util.List;

/**
 * Created by Erwin on 6/3/2017.
 */
public class WindowManager {
    List<AbstractWindow> windowList;


    public void addWindow(AbstractWindow window) {
        windowList.add(window);
    }
}

package com.smartmirror.sys.view.window;

import com.smartmirror.core.view.AbstractWindow;

import javax.swing.*;
import java.util.List;

/**
 * Created by Erwin on 6/3/2017.
 */
public class WindowManager {
    List<AbstractWindow> windowList;

    JPanel mainContainer;

    public void addWindow(AbstractWindow window) {
        windowList.add(window);
    }

    public void setMainContainer(JPanel mainContainer) {
        this.mainContainer = mainContainer;
    }
}

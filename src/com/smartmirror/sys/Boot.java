package com.smartmirror.sys;

import applications.ProfileCreator;
import com.smartmirror.core.view.AbstractApplication;
import com.smartmirror.core.view.AbstractWindow;
import com.smartmirror.sys.view.window.BootWindow;
import com.smartmirror.sys.view.window.FirstBootWindow;
import com.smartmirror.sys.view.window.ProfileSelectionWindow;
import com.smartmirror.sys.view.window.WindowManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Erwin on 5/31/2017.
 */
public class Boot {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Boot());
    }

    public Boot() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame();
        frame.setSize(screenSize);
        //frame.setUndecorated(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        frame.setLayout(new BorderLayout());
        JPanel windowHolder = new JPanel();
        windowHolder.setLayout(new BorderLayout());

        frame.add(windowHolder, BorderLayout.CENTER);
        frame.setVisible(true);

        new MainSystem(windowHolder);

    }
}
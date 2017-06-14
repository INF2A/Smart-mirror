package com.smartmirror.sys;

import com.pi4j.platform.PlatformAlreadyAssignedException;

import javax.swing.*;
import java.awt.*;

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

        try {
            new MainSystem(windowHolder);
        } catch (PlatformAlreadyAssignedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
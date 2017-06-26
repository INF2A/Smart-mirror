package com.smartmirror.sys.applications;

import com.smartmirror.core.view.AbstractWidget;
import com.smartmirror.sys.applications.widgets.ClockWidget;
import com.smartmirror.sys.view.AbstractSystemApplication;
//import widgets.ClockWidget;

import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * System application - used for displaying time
 */
public class Clock extends AbstractSystemApplication{
    private JLabel title;
    private JLabel icon;
    private JLabel timezoneLabel;
    private JLabel exampleTimezone;
    private JTextField timezone;
    private JButton save;
    private JButton exit;

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
        addWidgetToSystemWindow = true;

        SYSTEM_Screen.setLayout(new BorderLayout());

        title = new JLabel("Clock");
        title.setFont(applyFontSize(FontSize.H1));
        title.setForeground(Color.WHITE);
        icon = new JLabel(SYSTEM_Icon);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        timezoneLabel = new JLabel("Set desired timezone");
        timezoneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timezoneLabel.setFont(applyFontSize(FontSize.H2));
        timezoneLabel.setForeground(Color.WHITE);
        exampleTimezone = new JLabel("Europe/Amsterdam");
        exampleTimezone.setAlignmentX(Component.CENTER_ALIGNMENT);
        exampleTimezone.setFont(applyFontSize(FontSize.H3));
        exampleTimezone.setForeground(Color.WHITE);
        timezone = new JTextField(45);
        timezone.setAlignmentX(Component.CENTER_ALIGNMENT);
        timezone.setMaximumSize(new Dimension(timezone.getPreferredSize().width, timezone.getPreferredSize().height));

        save = new JButton("Save");
        exit = new JButton("Exit");

        exit.addActionListener(e -> {
            SYSTEM_closeScreen();
        });

        SYSTEM_Screen.add(title, BorderLayout.PAGE_START);

        JPanel container = new JPanel();
        container.setBackground(Color.BLACK);
        container.setForeground(Color.WHITE);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        container.add(icon);
        container.add(timezoneLabel);
        container.add(exampleTimezone);
        container.add(timezone);

        SYSTEM_Screen.add(container, BorderLayout.CENTER);

        JPanel buttonContainer = new JPanel(new FlowLayout());
        buttonContainer.setBackground(Color.BLACK);

        buttonContainer.add(save);
        buttonContainer.add(exit);

        SYSTEM_Screen.add(buttonContainer, BorderLayout.PAGE_END);

        focusManager.addComponent(timezone);
        focusManager.addComponent(save);
        focusManager.addComponent(exit);
    }

    @Override
    public void init() {

    }

    @Override
    public void onBackButton() {
        SYSTEM_closeScreen();
    }
}

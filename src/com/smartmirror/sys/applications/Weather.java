package com.smartmirror.sys.applications;

import com.smartmirror.sys.applications.widgets.WeatherWidget;
import com.smartmirror.sys.view.AbstractSystemApplication;
import com.smartmirror.sys.Font;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * System application - used for displaying current and forecast weather
 */
public class Weather extends AbstractSystemApplication{
    private JLabel title;
    private JLabel icon;

    private JLabel locationLbl;
    private JLabel exampleLocation;
    private JTextField location;

    private JLabel unitsLbl;
    private ButtonGroup radioButtonGroup;
    private JRadioButton metric;
    private JRadioButton imperial;

    private JButton exit;
    private JButton save;

    /**
     * Will only run ones when application is started
     * Define application startup settings here
     *
     * SYSTEM_Screen functions as base JPanel for the application
     */
    @Override
    public void setup() {
        setSYSTEM_Icon("img/weather-icon.png");
        SYSTEM_Widget = new WeatherWidget(); // Instantiate corresponding widget
        addWidgetToSystemWindow = true;

        SYSTEM_Screen.setBackground(Color.black);
        SYSTEM_Screen.setLayout(new BorderLayout());

        title = new JLabel("Weather");
        title.setFont(Font.applyFontSize(Font.FontSize.H1));
        title.setForeground(Color.WHITE);
        icon = new JLabel(SYSTEM_Icon);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        locationLbl = new JLabel("Set location");
        locationLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        locationLbl.setFont(Font.applyFontSize(Font.FontSize.H2));
        locationLbl.setForeground(Color.WHITE);
        exampleLocation = new JLabel("Example: Amsterdam");
        exampleLocation.setAlignmentX(Component.CENTER_ALIGNMENT);
        exampleLocation.setFont(Font.applyFontSize(Font.FontSize.H5));
        exampleLocation.setForeground(Color.WHITE);
        location = new JTextField(45);
        location.setAlignmentX(Component.CENTER_ALIGNMENT);
        location.setMaximumSize(new Dimension(location.getPreferredSize().width, location.getPreferredSize().height));

        metric = new JRadioButton("Celcius");
        metric.setSelected(true);
        metric.setBackground(Color.BLACK);
        metric.setForeground(Color.WHITE);
        metric.setFont(Font.applyFontSize(Font.FontSize.H4));
        metric.setAlignmentX(Component.CENTER_ALIGNMENT);
        metric.setActionCommand("metric");

        imperial = new JRadioButton("Fahrenheit");
        imperial.setActionCommand("imperial");
        imperial.setBackground(Color.BLACK);
        imperial.setForeground(Color.WHITE);
        imperial.setAlignmentX(Component.CENTER_ALIGNMENT);
        imperial.setFont(Font.applyFontSize(Font.FontSize.H4));

        radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(metric);
        radioButtonGroup.add(imperial);

        unitsLbl = new JLabel("Set units");
        unitsLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        unitsLbl.setFont(Font.applyFontSize(Font.FontSize.H2));
        unitsLbl.setForeground(Color.WHITE);

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
        container.add(locationLbl);
        container.add(exampleLocation);
        container.add(location);
        container.add(unitsLbl);
        container.add(metric);
        container.add(imperial);

        SYSTEM_Screen.add(container, BorderLayout.CENTER);

        JPanel buttonContainer = new JPanel(new FlowLayout());
        buttonContainer.setBackground(Color.BLACK);

        buttonContainer.add(save);
        buttonContainer.add(exit);

        SYSTEM_Screen.add(buttonContainer, BorderLayout.PAGE_END);

        focusManager.addComponent(location);
        focusManager.addComponent(metric);
        focusManager.addComponent(imperial);
        focusManager.addComponent(save);
        focusManager.addComponent(exit);
    }

    @Override
    public void init() {

        System.out.println("Weather");
    }


    @Override
    public void onBackButton() {

        super.onBackButton();
        SYSTEM_closeScreen();
    }
}

package com.smartmirror.sys.applications;

import com.smartmirror.core.view.AbstractWidget;
import com.smartmirror.sys.DB;
import com.smartmirror.sys.applications.widgets.ClockWidget;
import com.smartmirror.sys.view.AbstractSystemApplication;
import com.smartmirror.sys.Font;

import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
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
    private JPanel container;
    private JLabel saveSettings;
    private JPanel timeZones;

    private JRadioButton amsterdam;
    private JRadioButton shangai;
    private JRadioButton santiago;
    private JRadioButton chicago;
    private JRadioButton melbourne;
    private JRadioButton moscow;
    private List<JRadioButton> radioButtons;
    private ButtonGroup options;

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

        container = new JPanel();
        container.setBackground(Color.BLACK);
        container.setForeground(Color.WHITE);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        title = new JLabel("Clock");
        title.setFont(Font.applyFontSize(Font.FontSize.H1));
        title.setForeground(Color.WHITE);

        icon = new JLabel(SYSTEM_Icon);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(icon);

        timezoneLabel = new JLabel("Set desired timezone");
        timezoneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timezoneLabel.setFont(Font.applyFontSize(Font.FontSize.H2));
        timezoneLabel.setForeground(Color.WHITE);
        container.add(timezoneLabel);

        options = new ButtonGroup();

        radioButtons = new ArrayList<>();
        amsterdam = new JRadioButton("Europe/Amsterdam");
        amsterdam.setActionCommand("425");
        santiago = new JRadioButton("America/Santiago");
        santiago.setActionCommand("196");
        shangai = new JRadioButton("Asia/Shanghai");
        shangai.setActionCommand("309");
        moscow = new JRadioButton("Europe/Moscow");
        moscow.setActionCommand("458");
        melbourne = new JRadioButton("Australia/Melbourne");
        melbourne.setActionCommand("356");
        chicago = new JRadioButton("America/Chicago");
        chicago.setActionCommand("94");

        radioButtons.add(amsterdam);
        radioButtons.add(santiago);
        radioButtons.add(shangai);
        radioButtons.add(moscow);
        radioButtons.add(melbourne);
        radioButtons.add(chicago);

        for (JRadioButton radioButton : radioButtons) {
            radioButton.setBackground(Color.BLACK);
            radioButton.setForeground(Color.WHITE);
            radioButton.setFont(Font.applyFontSize(Font.FontSize.H3));
            radioButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            options.add(radioButton);
            container.add(radioButton);
            focusManager.addComponent(radioButton);
        }

        save = new JButton("Save");
        exit = new JButton("Exit");

        exit.addActionListener(e -> {
            SYSTEM_closeScreen();
        });

        save.addActionListener(e -> {
            saveSettings();
        });

        SYSTEM_Screen.add(title, BorderLayout.PAGE_START);

        SYSTEM_Screen.add(container, BorderLayout.CENTER);

        JPanel buttonContainer = new JPanel(new FlowLayout());
        buttonContainer.setBackground(Color.BLACK);

        buttonContainer.add(save);
        buttonContainer.add(exit);

        SYSTEM_Screen.add(buttonContainer, BorderLayout.PAGE_END);

        focusManager.addComponent(save);
        focusManager.addComponent(exit);
    }

    private void saveSettings() {
        DB.getConnection();

        try {
            DB.query("UPDATE time SET Timezone_ID = " + options.getSelection().getActionCommand() + " WHERE User_ID = " + DB.id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        saveSettings = new JLabel("Clock settings saved.");
        saveSettings.setForeground(Color.WHITE);
        saveSettings.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveSettings.setFont(Font.applyFontSize(Font.FontSize.H5));

        container.add(saveSettings);

        ClockWidget.updated = true;
    }

    @Override
    public void init() {

        System.out.println("Clock");
    }

    @Override
    public void onBackButton() {

        super.onBackButton();
        SYSTEM_closeScreen();
    }
}

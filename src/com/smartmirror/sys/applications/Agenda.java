package com.smartmirror.sys.applications;

import com.smartmirror.sys.view.AbstractSystemApplication;
import com.smartmirror.sys.applications.widgets.AgendaWidget;
import com.smartmirror.sys.applications.widgets.ClockWidget;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by basva on 9-6-2017.
 */
public class Agenda extends AbstractSystemApplication {
    private JLabel title;
    private JLabel icon;

    @Override
    protected void setup() {
        SYSTEM_Widget = new AgendaWidget(new ClockWidget());
        addWidgetToSystemWindow = true;

        setSYSTEM_Icon("img/calendar-icon.png");

        SYSTEM_Screen.setLayout(new BorderLayout());

        title = new JLabel("Agenda");
        title.setFont(applyFontSize(FontSize.H1));
        title.setForeground(Color.WHITE);

        icon = new JLabel(SYSTEM_Icon);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        SYSTEM_Screen.add(title, BorderLayout.PAGE_START);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.BLACK);
        container.add(icon);

        SYSTEM_Screen.add(container, BorderLayout.CENTER);
    }

    @Override
    public void init() {

    }

    @Override
    public void onBackButton() {
        SYSTEM_closeScreen();
    }
}

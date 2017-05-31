package com.smartmirror.sys;

import applications.Settings;
import applications.Weather;
import applications.WindowPluginTest;

import com.smartmirror.core.view.AbstractApplication;
import com.smartmirror.core.view.AbstractSystemApplication;
import com.smartmirror.core.view.AbstractUserApplication;
import com.smartmirror.core.view.AbstractWindow;
import com.smartmirror.sys.input.keyboard.KeyboardController;
import org.omg.CORBA.INTERNAL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Erwin on 5/15/2017.
 */
public class Main{

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        // SwingUtilities.invokeLater(() -> { new Main().buildGui(); });
        new Main();
    }


    JFrame frame;
    JPanel container;

    SystemWindow systemWindow;
    AbstractWindow currentWindow;

    final KeyboardController kbc = new KeyboardController();
    final InputHandler inputHandler = new InputHandler();
    //   final GpioListener gpio = new GpioListener(inputHandler);

    Map<String, AbstractApplication> userApps;
    Map<String, AbstractSystemApplication> systemApps;
    ApplicationParser appParser;

    public Main() {
        buildGui();

        kbc.setKeyBoardDimensions(frame.getWidth(), frame.getHeight());
        inputHandler.attachKeyboardController(kbc);

        systemWindow = new SystemWindow();
        systemWindow.INTERNAL_addWindowChangeListener(e -> setCurrentWindow(systemWindow.selectedApp));

        appParser = new ApplicationParser();
        userApps = new HashMap<>();
        systemApps = appParser.getSystemApplications();

        loadPlugins();

        startSystemWindow();
        test_AttachButtonSimulator();
    }

    public void buildGui() {
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        //frame.setUndecorated(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        container = new JPanel();
        container.setLayout(new BorderLayout());
        //  container.setSize(screenSize);
        frame.add(container, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    /// TEMPORARY
    // simulates the remote buttons
    private void test_AttachButtonSimulator() {
        Object rightKey = new Object();
        Object leftKey = new Object();
        Object backKey = new Object();
        Object menuKey = new Object();
        //  Object buttonPressed = new Object();
        //  Object buttonReleased = new Object();

        container.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), rightKey);
        container.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), leftKey);
        container.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), backKey);
        container.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), menuKey);
        //  container.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed"), buttonPressed);
        //  container.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released"), buttonReleased);

        container.getActionMap().put(rightKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputHandler.onRightButton();
                System.out.println("right -> ");
            }
        });
        container.getActionMap().put(leftKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputHandler.onLeftButton();
                System.out.println("left <- ");
            }
        });
        container.getActionMap().put(backKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputHandler.onBackButton();
                System.out.println("Back");
            }
        });
        container.getActionMap().put(menuKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputHandler.onMenuButton();
                System.out.println("Menu");
            }
        });
    }

    private void setCurrentWindow(AbstractApplication window)
    {
        System.out.println(
                Thread.currentThread().getName() + " - setCurrentW - Alive: " +
                        Thread.currentThread().isAlive());
        currentWindow = window;
        inputHandler.attachWindow(window);

        container.removeAll();
        container.add(window.INTERNAL_getScreen(), BorderLayout.CENTER);
        window.INTERNAL_init();
        window.init();

        update();

    }

    private void openKeyboard() {
        container.add(inputHandler.kbc.getKeyboardview(), BorderLayout.SOUTH);
        update();
    }

    private void closeKeyboard() {
        container.remove(inputHandler.kbc.getKeyboardview());
        update();
    }

    private void startSystemWindow() {
        System.out.println(
                Thread.currentThread().getName() + " - startSiyW - Alive: " +
                        Thread.currentThread().isAlive());
        currentWindow = systemWindow;
        container.add(systemWindow.INTERNAL_getScreen(), BorderLayout.CENTER);
        inputHandler.attachWindow(systemWindow);
        update();

    }

    private void changeToSystemWindow()
    {
        System.out.println(
                Thread.currentThread().getName() + " - Change2SysW - Alive: " +
                        Thread.currentThread().isAlive());
        container.remove(currentWindow.INTERNAL_getScreen());
        startSystemWindow();
    }

    // refreshes the screen
    private void update()
    {
        System.out.println(
                Thread.currentThread().getName() + " - Main update - Alive: " +
                        Thread.currentThread().isAlive());
        container.revalidate();
        container.repaint();
    }

    private void destroyApplication() {
        Class<? extends AbstractWindow> c = currentWindow.getClass();
        String name = getApplicationName(currentWindow);
        try {
            AbstractApplication t = (AbstractApplication) c.newInstance();
            userApps.put(name, t);
            t.INTERNAL_addDestroyActionListener(e -> destroyApplication());
            t.INTERNAL_addExitActionListener(e -> changeToSystemWindow());
            t.INTERNAL_addKeyBoardRequestActionListener(e -> openKeyboard());
            t.INTERNAL_addKeyboardCloseHandleActionListener(e -> closeKeyboard());
            systemWindow.apps.put(name, t);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        System.out.println(
                Thread.currentThread().getName() + " - Destroy app - Alive: " +
                        Thread.currentThread().isAlive());
        changeToSystemWindow();
    }


    private String getApplicationName(AbstractWindow app) {
        for(Map.Entry<String, AbstractApplication> entry : userApps.entrySet()) {
            if (entry.getValue().equals(app)){
                return entry.getKey();
            }
        }
        return null;
    }
    // testing purposes - use this to load an app
    private void loadSystemApplications() {
        AbstractSystemApplication weather = new Weather();
        weather.setup();
        systemApps.put("weather", weather);

        AbstractSystemApplication settings = new Settings();
        settings.setup();
        systemApps.put("settings", settings);

        for(Map.Entry<String, AbstractSystemApplication> entry : systemApps.entrySet()) {
            entry.getValue().INTERNAL_addRequestSystemApplicationListActionListener(e -> entry.getValue().INTERNAL_setSystemApplicationList(systemApps));
            entry.getValue().INTERNAL_addDestroyActionListener(e -> destroyApplication());
            entry.getValue().INTERNAL_addExitActionListener(e -> changeToSystemWindow());
            entry.getValue().INTERNAL_addKeyBoardRequestActionListener(e -> openKeyboard());
            entry.getValue().INTERNAL_addKeyboardCloseHandleActionListener(e -> closeKeyboard());
            systemWindow.addApplicationToWindow(entry.getKey(), entry.getValue());
        }

        AbstractUserApplication test = new WindowPluginTest();
        test.setup();
        userApps.put("test", test);
        systemWindow.addApplicationToWindow("test", test);
        test.INTERNAL_addDestroyActionListener(e -> destroyApplication());
        test.INTERNAL_addExitActionListener(e -> changeToSystemWindow());
        test.INTERNAL_addKeyBoardRequestActionListener(e -> openKeyboard());
        test.INTERNAL_addKeyboardCloseHandleActionListener(e -> closeKeyboard());
    }

    private void loadPlugins() {
        loadSystemApplications();
    }

}

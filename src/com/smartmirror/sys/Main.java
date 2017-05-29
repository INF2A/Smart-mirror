package com.smartmirror.sys;

import application.Settings;
import application.Weather;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import application.WindowPluginTest;

import com.smartmirror.core.view.AbstractApplication;
import com.smartmirror.core.view.AbstractWindow;
import com.smartmirror.sys.input.keyboard.KeyboardController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
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

    Map<String, AbstractApplication> apps;

    public Main() {
        buildGui();

        System.out.println(frame.getWidth() + " " + frame.getHeight());
        kbc.setKeyBoardDimensions(frame.getWidth(), frame.getHeight());
        inputHandler.attachKeyboardController(kbc);

        systemWindow = new SystemWindow();
        systemWindow.INTERNAL_addWindowChangeListener(e -> setCurrentWindow(systemWindow.selectedApp));

        apps = new HashMap<>();
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



    private void loadTest() throws Exception {
        URL url = new File("plugins\\clock.jar").toURI().toURL();
        URLClassLoader ClassLoader = URLClassLoader.newInstance(new URL[] {url});

        Class<?> module = Class.forName("com.smartmirror.clock.Application", true, ClassLoader);

        AbstractApplication window = (AbstractApplication) module.newInstance();
        window.INTERNAL_addExitActionListener(e -> changeToSystemWindow());
        window.INTERNAL_addKeyBoardRequestActionListener(e -> openKeyboard());
        window.INTERNAL_addKeyboardCloseHandleActionListener(e -> closeKeyboard());

        apps.put("clock", window);
        systemWindow.addApplicationToWindow("clock", window);
    }

    private void loadPlugins() {

        AbstractApplication a = new WindowPluginTest();
        a.INTERNAL_addExitActionListener(e -> changeToSystemWindow());
        a.INTERNAL_addKeyBoardRequestActionListener(e -> openKeyboard());
        a.INTERNAL_addKeyboardCloseHandleActionListener(e -> closeKeyboard());

        AbstractApplication weather = new Weather();
        weather.INTERNAL_addExitActionListener(e -> changeToSystemWindow());
        weather.INTERNAL_addKeyBoardRequestActionListener(e -> openKeyboard());
        weather.INTERNAL_addKeyboardCloseHandleActionListener(e -> closeKeyboard());

        apps.put("weather", weather);
        systemWindow.addApplicationToWindow("weather", weather);

        try {
            loadTest();
        } catch (Exception e) {
            e.printStackTrace();
        }


        AbstractApplication settings = new Settings(apps);
        settings.INTERNAL_addExitActionListener(e -> changeToSystemWindow());
        settings.INTERNAL_addKeyBoardRequestActionListener(e -> openKeyboard());
        settings.INTERNAL_addKeyboardCloseHandleActionListener(e -> closeKeyboard());

        apps.put("settings", settings);
        systemWindow.addApplicationToWindow("settings", settings);
        apps.put("test1", a);
        systemWindow.addApplicationToWindow("test1", a);
    }


}

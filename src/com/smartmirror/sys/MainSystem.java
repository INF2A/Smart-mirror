package com.smartmirror.sys;

import applications.Settings;
import applications.Weather;
import applications.Wifi;
import applications.WindowPluginTest;

import com.smartmirror.core.view.AbstractApplication;
import com.smartmirror.sys.view.AbstractSystemApplication;
import com.smartmirror.core.view.AbstractUserApplication;
import com.smartmirror.core.view.AbstractWindow;
import com.smartmirror.sys.input.keyboard.KeyboardController;
import com.smartmirror.sys.view.FocusManager;
import com.smartmirror.sys.view.window.WindowManager;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;


/**
 * Created by Erwin on 5/15/2017.
 */
public class MainSystem {

    final MainSystemController sc = new MainSystemController(this);

    SystemWindow systemWindow;

    final KeyboardController kbc = new KeyboardController();
    final InputHandler inputHandler = new InputHandler();
    //   final GpioListener gpio = new GpioListener(inputHandler);

    @Deprecated
    Map<String, AbstractApplication> userApps;

    @Deprecated
    Map<String, AbstractSystemApplication> systemApps;

    ApplicationParser appParser;

    public MainSystem() {

        buildGui();

        kbc.setKeyBoardDimensions(frame.getWidth(), frame.getHeight());
        inputHandler.attachKeyboardController(kbc);

        systemWindow = new SystemWindow();

        systemWindow.INTERNAL_addWindowChangeListener(e -> {
            String name = getApplicationName(systemWindow.selectedApp);
            startApplication(name);
        });

       // appParser = new ApplicationParser();
        userApps = new HashMap<>();
        systemApps = new HashMap<>();
       // systemApps = appParser.getSystemApplications();

        loadPlugins();

        startSystemWindow();
        test_AttachButtonSimulator();
    }

    private void buildGui() {
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

        container.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), rightKey);
        container.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), leftKey);
        container.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), backKey);
        container.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), menuKey);
        //  container.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed"), buttonPressed);
        //  container.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released"), buttonReleased);

        container.getActionMap().put(rightKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputHandler.onRightButton();
                java.lang.System.out.println("right -> ");
            }
        });
        container.getActionMap().put(leftKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputHandler.onLeftButton();
                java.lang.System.out.println("left <- ");
            }
        });
        container.getActionMap().put(backKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputHandler.onBackButton();
                java.lang.System.out.println("Back");
            }
        });
        container.getActionMap().put(menuKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputHandler.onMenuButton();
                java.lang.System.out.println("Menu");
            }
        });
    }

    @Deprecated // use startApplication(string name)
    private void setCurrentWindow(AbstractApplication window)
    {
        java.lang.System.out.println(
                Thread.currentThread().getName() + " - setCurrentW - Alive: " +
                        Thread.currentThread().isAlive());
        currentWindow = window;
        inputHandler.attachWindow(window);

        container.removeAll();
        container.add(window.INTERNAL_getScreen(), BorderLayout.CENTER);
      //  window.INTERNAL_init();
        window.init();
        update();

    }

    private void startSystemWindow() {
        java.lang.System.out.println(
                Thread.currentThread().getName() + " - startSiyW - Alive: " +
                        Thread.currentThread().isAlive());
        currentWindow = systemWindow;
        container.add(systemWindow.INTERNAL_getScreen(), BorderLayout.CENTER);
        inputHandler.attachWindow(systemWindow);
        update();

    }

    private void changeToSystemWindow()
    {
        java.lang.System.out.println(
                Thread.currentThread().getName() + " - Change2SysW - Alive: " +
                        Thread.currentThread().isAlive());
        container.remove(currentWindow.INTERNAL_getScreen());
        startSystemWindow();
    }

    @Deprecated
    private void destroyApplication() {
       Class<? extends AbstractWindow> c = currentWindow.getClass();

        String name = getApplicationName((AbstractApplication)currentWindow);

        try {
            AbstractApplication t = (AbstractApplication) c.newInstance();
            t.INTERNAL_addDestroyActionListener(e -> destroyApplication());
            t.INTERNAL_addExitActionListener(e -> changeToSystemWindow());
            t.INTERNAL_addKeyBoardRequestActionListener(e -> openKeyboard());
            t.INTERNAL_addKeyboardCloseHandleActionListener(e -> closeKeyboard());
            applications.put(name, t);
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

    // testing purposes - use this to load an app
    private void loadSystemApplications() {
        applications = new HashMap<>();

        AbstractSystemApplication weather = new Weather();
        setupApplication(weather);
        systemApps.put("weather", weather);
        applications.put("weather", weather);

        AbstractSystemApplication wifi = new Wifi();
        setupApplication(wifi);
        systemApps.put("wifi", wifi);
        applications.put("wifi", wifi);

        AbstractSystemApplication settings = new Settings();
        settings.setSystemController(sc);
        setupApplication(settings);
        systemApps.put("settings", settings);
        applications.put("settings", settings);

        for(Map.Entry<String, AbstractApplication> entry : applications.entrySet()) {
            systemWindow.addApplicationToWindow(entry.getKey(), entry.getValue());
        }

        AbstractUserApplication test = new WindowPluginTest();
        //userApps.put("test", test);
        systemWindow.addApplicationToWindow("test", test);
        applications.put("test", test);


    }

    private void loadPlugins() {
        loadSystemApplications();
    }




   /////////////////////////////////////////////

    // A frame to hold our system
    private JFrame frame;

    // The container that is held by the frame
    private JPanel container;


    public final WindowManager windowManager = new WindowManager();



    // The window that is currently showing in the container.
    private AbstractWindow currentWindow;

    // Holds all applications, system and user
    private Map<String, AbstractApplication> applications;


    /**
     * Returns a Map with the name and application
     * of all applications currently known by the system.
     *
     * @return The Map of applications with their names
     */
    public Map<String, AbstractApplication> getApplications() {
        return applications;
    }

    /**
     * Returns a Map with the name and application
     * of all known system applications
     *
     * @return A Map of User Applications with their names
     */
    public Map<String, AbstractUserApplication> getUserApplications() {
        // create empty map to return
        Map<String, AbstractUserApplication> userApplications = new HashMap<>();

        // Check in all application if it is a user applications
        // if it is we add it to the return map
        for (Map.Entry<String, AbstractApplication> application : applications.entrySet() ) {
            if(application.getValue() instanceof AbstractUserApplication) {
                userApplications.put(application.getKey(), (AbstractUserApplication) application.getValue());
            }
        }
        return userApplications;
    }

    /**
     * Returns a Map with the name and application
     * of all known system applications
     *
     * @return A Map of System Applications with their names
     */
    public Map<String, AbstractSystemApplication> getSystemApplications() {
        // create empty map to return
        Map<String, AbstractSystemApplication> systemApplications = new HashMap<>();

        // Check in all application if it is a system applications
        // if it is we add it to the return map
        for (Map.Entry<String, AbstractApplication> application : applications.entrySet() ) {
            if(application.getValue() instanceof AbstractSystemApplication) {
                systemApplications.put(application.getKey(), (AbstractSystemApplication) application.getValue());
            }
        }
        return systemApplications;
    }

    /**
     * Starts an application with the given name
     * if it exists in our application Map.
     *
     * @param name The name of the application to start
     */
    public void startApplication(String name) {
        // Check if the application exists, return false otherwise
        if(applications.containsKey(name)) {
            // Get the application
            AbstractApplication app = applications.get(name);

            // setup the application
            setupApplication(app);

            // show the application
            setWindow(app);

            // initialize the application
            app.init();
        }
    }

    /**
     * This will set the displaying window of the system.
     * The current display window will be removed and the
     * given window will be shown.
     *
     * Input will be passed to the given window.
     *
     * Our container uses a BorderLayout and the given
     * window will be added to BorderLayout.Center
     *
     * @param window The AbstractWindow to display
     */
    public void setWindow(AbstractWindow window) {
        // remove the current window
        container.remove(currentWindow.INTERNAL_getScreen());
        // set the new window
        container.add(window.INTERNAL_getScreen());
        // set controls to pass though the new window
        inputHandler.attachWindow(window);
        // store the new window as the current window
        currentWindow = window;
        // update display
        update();
    }

    /**
     * Searches through the list of current applications
     * and returns the name based on the given application
     *
     * @param app The Application to find the name for
     * @return The found name based of the given application, otherwise null
     */
    private String getApplicationName(AbstractApplication app) {
        // Search for the name that matches the application in the map of applications
        for(Map.Entry<String, AbstractApplication> entry : applications.entrySet()) {
            if (entry.getValue().equals(app)){
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * This method will setup the given AbstractApplication.
     *
     * It will check if the setup has already run for the given
     * application. If it has not it will run the setup and add
     * a new focusManager and the action listeners for;
     *  - Opening the keyboard.
     *      This will call a JPanel and add it to our container field.
     *      The keyboard will be added to BorderLayout.SOUTH.
     *  - closing the keyboard.
     *      This removes the keyboard from BorderLayout.SOUTH of our container
     *  - closing the Application.
     *      This will tell the system to go back to the SystemWindow
     *  - destroying the Application.
     *      This will destroy the entire Application with all its code
     *      and re-initializes it. Meaning that the setup will be called
     *      again when the application is started.
     *
     * @param app The application to setup
     */
    private void setupApplication(AbstractApplication app) {
        // Check if setup already started. If not started, it means first start of the application.
        // This wil run the internal setup of the application and add the action listeners
        if(!app.INTERNAL_setupRun) {
            app.setFocusManager(new FocusManager());
            app.INTERNAL_setup();
            app.INTERNAL_addKeyBoardRequestActionListener(e -> openKeyboard());
            app.INTERNAL_addKeyboardCloseHandleActionListener(e -> closeKeyboard());
            app.INTERNAL_addExitActionListener(e -> changeToSystemWindow());
        }
    }

    /**
     * Opens the Keyboard for the current window.
     * The window has a focus component to which the Keyboard writes.
     *
     * This will call a JPanel which houses a keyboard
     * and add it to our container field. The keyboard
     * will be added to BorderLayout.SOUTH.
     * The keyboard takes about 1/3 of the screen size.
     *
     * Once added, it will update our display to actually show the Keyboard.
     */
    private void openKeyboard() {
        container.add(inputHandler.kbc.getKeyboardview(), BorderLayout.SOUTH);
        update();
    }

    /**
     * Closes the Keyboard for the current window.
     * The focus component does no longer need the Keyboard.
     *
     * This removes the keyboard from BorderLayout.SOUTH of our container.
     *
     * Once removed, it will update our display so the window can go back
     * to full screen. The keyboard took 1/3 of the screen.
     */
    private void closeKeyboard() {
        container.remove(inputHandler.kbc.getKeyboardview());
        update();
    }

    /**
     * Updates the display with the latest visual changes.
     * Makes a call to the system container to update itself.
     * This will draw any new window, or changes to the current
     * window, to the display
     */
    private void update()
    {
        // repaints all children of the panel
        container.revalidate();
        // repaint the panel
        container.repaint();
    }
}

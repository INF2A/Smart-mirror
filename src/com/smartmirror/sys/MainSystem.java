package com.smartmirror.sys;

import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.smartmirror.core.view.AbstractApplication;
import com.smartmirror.sys.applications.*;
import com.smartmirror.sys.input.key.KeyInput;
import com.smartmirror.sys.view.AbstractSystemApplication;
import com.smartmirror.core.view.AbstractUserApplication;
import com.smartmirror.sys.input.keyboard.KeyboardController;
import com.smartmirror.sys.view.FocusManager;
import com.smartmirror.sys.view.window.BootWindow;
import com.smartmirror.sys.view.window.FirstBootWindow;
import com.smartmirror.sys.view.window.WindowManager;
import com.smartmirror.sys.InputHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.util.*;


/**
 * Created by Erwin on 5/15/2017.
 */
public class MainSystem {
    ApplicationParser appParser;



    /// TEMPORARY
    // simulates the remote buttons
    private void test_AttachButtonSimulator() {
        Object rightKey = new Object();
        Object leftKey = new Object();
        Object backKey = new Object();
        Object menuKey = new Object();
        //  Object buttonPressed = new Object();
        //  Object buttonReleased = new Object();

        windowManager.getWindowHolder().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), rightKey);
        windowManager.getWindowHolder().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), leftKey);
        windowManager.getWindowHolder().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), backKey);
        windowManager.getWindowHolder().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), menuKey);
        //  container.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed"), buttonPressed);
        //  container.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released"), buttonReleased);

        windowManager.getWindowHolder().getActionMap().put(rightKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputHandler.onRightButton();
                System.out.println("right -> ");
            }
        });
        windowManager.getWindowHolder().getActionMap().put(leftKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputHandler.onLeftButton();
                System.out.println("left <- ");
            }
        });
        windowManager.getWindowHolder().getActionMap().put(backKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputHandler.onBackButton();
                System.out.println("Back");
            }
        });
        windowManager.getWindowHolder().getActionMap().put(menuKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputHandler.onMenuButton();
                System.out.println("Menu");
            }
        });
    }


    public void startSystemWindow() {
        java.lang.System.out.println(
                Thread.currentThread().getName() + " - startSiyW - Alive: " +
                        Thread.currentThread().isAlive());
        windowManager.setWindow("system");
        inputHandler.attachWindow(windowManager.getCurrentWindow());
    }

    private void changeToSystemWindow()
    {
        startSystemWindow();
    }

    // testing purposes - use this to load an app
    public void loadSystemApplications() {
        applications = new LinkedHashMap<>();

//        AbstractSystemApplication profileCreator = new ProfileCreator();
//        setupApplication(profileCreator);
//        applications.put("profileCreator", profileCreator);
//
        AbstractSystemApplication settings = new Settings();
        settings.setSystemController(systemController);
        setupApplication(settings);
        applications.put("settings", settings);

        AbstractSystemApplication monitor = new Monitor();
        setupApplication(monitor);
        applications.put("monitor", monitor);

        AbstractSystemApplication agenda = new Agenda();
        setupApplication(agenda);
        applications.put("agenda", agenda);

        AbstractSystemApplication clock = new Clock();
        setupApplication(clock);
        applications.put("clock", clock);

        AbstractSystemApplication weather = new Weather();
        setupApplication(weather);
        applications.put("weather", weather);

        AbstractSystemApplication news = new News();
        setupApplication(news);
        applications.put("news", news);

        AbstractSystemApplication wifi = new Wifi();
        setupApplication(wifi);
        applications.put("wifi", wifi);

        for(Map.Entry<String, AbstractApplication> entry : applications.entrySet()) {
            systemWindow.addApplicationToWindow(entry.getKey(), entry.getValue());
        }

//        AbstractUserApplication test = new WindowPluginTest();
//        //userApps.put("test", test);
//        systemWindow.addApplicationToWindow("test", test);
//        applications.put("test", test);


    }

    private void loadPlugins() {
        loadSystemApplications();
    }




   /////////////////////////////////////////////
    final MainSystemController systemController = new MainSystemController(this);

    final public KeyboardController kbc = new KeyboardController();
    final public InputHandler inputHandler = new InputHandler(kbc);

    public WindowManager windowManager;
//    final KeyInput keyInput = new KeyInput(inputHandler);
//    final GpioListener gpio = new GpioListener(inputHandler);


    private SystemWindow systemWindow;

    // Holds all applications, system and user
    private Map<String, AbstractApplication> applications;

    public MainSystem(JPanel windowHolder) throws PlatformAlreadyAssignedException, InterruptedException {
        // create a new window manager for display
        windowManager = new WindowManager(windowHolder);

        // testing controls
        test_AttachButtonSimulator();

        // setup the loading window
        windowManager.setWindow(new BootWindow());

        // load all system requirements
        new Thread(() -> setup()).start();
    }

    /**
     * Loads all that is necessary for the system
     */
    private void setup() {
//        setupApplications();
//        handleSystemStart();
        systemWindow = new SystemWindow();
        systemWindow.INTERNAL_addWindowChangeListener(e -> {
            String name = getApplicationName(systemWindow.selectedApp);
            startApplication(name);
        });
        windowManager.addWindow("system", systemWindow);

        // load system applications here
        loadPlugins();

        for (Map.Entry<String, AbstractApplication> app : applications.entrySet()) {
            windowManager.addWindow(app.getKey(), app.getValue());
        }

        // simulate loading... and give 5 seconds to find access points for wifi
        SwingUtilities.invokeLater(() -> applications.get("wifi").init());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check it's the first system start
        if(isFirstBoot()) {
            startFirstBoot();
        } else {
            startSystem();
        }
    }

    /**
     * Starts the system normally
     */
    private void startSystem() {
        SwingUtilities.invokeLater(() -> startSystemWindow());
    }

    /**
     * Responsible for opening the first boot screen
     * After the first boot, the system wil its normal start
     */
    private void startFirstBoot() {
        SwingUtilities.invokeLater(() -> {
            windowManager.addWindow("firstBoot", new FirstBootWindow(systemController));
            windowManager.setWindow("firstBoot");
            inputHandler.attachWindow(windowManager.getCurrentWindow());
            ((FirstBootWindow)windowManager.getWindow("firstBoot")).start();
            new Thread(() -> {
                while(!((FirstBootWindow)windowManager.getWindow("firstBoot")).isFinished) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                startSystem();
            }).start();
        });
    }


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
            windowManager.setWindow(app);

            // set controls to pass though the new window
            inputHandler.attachWindow(app);

            // initialize the application
            app.init();
        }
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
        kbc.setKeyBoardDimensions(windowManager.getWindowHolder().getWidth(), windowManager.getWindowHolder().getHeight());
        windowManager.getWindowHolder().add(kbc.getKeyboardview(), BorderLayout.SOUTH);
        windowManager.update();
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
        windowManager.getWindowHolder().remove(kbc.getKeyboardview());
        windowManager.update();
    }


    /**
     * Calls a script to check if the file "FirstBoot" is present
     * on the system. If the file is present it means that
     * this is the first boot.
     * The script returns either true or false;
     *
     * @return boolean to check if this is the first boot
     */
    private boolean isFirstBoot() {

        // testing purposes
        if(true) return false;

        // Create an empty array to store the commands to run on the system
        java.util.List<String> commands = new ArrayList<>();

        // This calls the script we want to run
        commands.add("/mnt/mmcblk0p2/smartmirror/tce/scripts/FirstBootCheck.sh");

        // Runs our commands and gives the output back to us
        java.util.List<String> output = Shell.getInstance().runCommand(commands, false);

        // Check if the file is present or not
        if(output.get(0).equals("false")) {
            java.lang.System.out.println("did not find, no first boot");
            return false;
        } else {
            java.lang.System.out.println("we found the firstBoot file, should delete now and run first boot stuff");
            Shell.getInstance().runCommand(Arrays.asList("rm", "/mnt/mmcblk0p2/tce/smartmirror/system/firstboot"), false);
        }

        return true;
    }
}

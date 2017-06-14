package com.smartmirror.sys;

import com.smartmirror.core.view.AbstractApplication;
import com.smartmirror.core.view.AbstractWindow;
import com.smartmirror.sys.view.AbstractSystemApplication;
import com.smartmirror.sys.view.window.WindowManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Erwin on 5/31/2017.
 *
 * This is just a go-between-layer for applications and windows that need to communicate
 * with the system.
 *
 * It abstracts from all that is the system and defines simple methods.
 */
public class MainSystemController {

    private MainSystem system;  // The main system


    public MainSystemController(MainSystem system) {
        this.system = system;
    }

    public WindowManager getWindowManager() {
        return system.windowManager;
    }

    /**
     * This method returns a list of user applications names
     * which are installed on the main system
     * @return A list of user application names or an empty list if there are no application names
     */
    public List<String> getUserApplicationNameList() {
        // create empty list to return
        List<String> returnList = new ArrayList<>();

        // Grab the user applications from systems and store the name in the returning list
        for (String name : system.getUserApplications().keySet()) {
            returnList.add(name);
        }
        return returnList;
    }

    /**
     * This method returns a list of system applications names
     * which are installed on the main system
     * @return A list of system application names
     */
    public List<String> getSystemApplicationNameList() {
        // create empty list to return
        List<String> returnList = new ArrayList<>();

        // Grab the user applications from systems and store the name in the returning list
        for (String name : system.getSystemApplications().keySet()) {
            returnList.add(name);
        }
        return returnList;
    }

    /**
     * Will start the application with the given name.
     * @param name The name of the application to start
     */
    public void startApplication(String name)
    {
        system.startApplication(name);
    }


    /**
     * Will start the application with the given name.
     * @param name The name of the application to start
     */
    public AbstractApplication getApplication(String name)
    {
        return system.getApplications().get(name);
    }

}

package com.smartmirror.sys;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erwin on 5/31/2017.
 *
 * This is a controller that is used by the system applications
 * to talk wil the main system.
 * In actuality, this class is used as a give through window, it abstracts
 * from the main system and gives only the necessary methods.
 */
public class MainSystemController {
    public boolean TESTING = true;

    private MainSystem system;  // The main system

    public MainSystemController(MainSystem system) {
        this.system = system;
    }


    /**
     * This method returns a list of user applications names
     * which are installed on the main system
     * @return A list of user application names or an empty list if there are no application names
     */
    public List<String> requestUserApplicationNameList() {
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
    public List<String> requestSystemApplicationsNameList() {
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



}

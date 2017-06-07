package com.smartmirror.sys;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Erwin on 5/31/2017.
 */
public class Boot {

    public static void main(String[] args) {
        new Boot();
    }

    final MainSystem system = new MainSystem();
    final MainSystemController systemController = new MainSystemController(system);


    public Boot() {
     //   setup();

        if(isFirstBoot()) {
       //    system.setWindow(new ProfileSelectionWindow());

            //system.startFirstBoot();
            // create account
            // setup network
        } else {

        }
    }

    private void setup() {
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        //frame.setUndecorated(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        //  container.setSize(screenSize);
        frame.add(container, BorderLayout.CENTER);
        frame.setVisible(true);

        system.windowManager.setMainContainer(container);
        // loading stuff add here...
    }

    /**
     * Calls a script to check if the file "FirstBoot" is present
     * on the system. If the file is present it means that
     * this is the first boot.
     * The script returns either true or false;
     *
     * @return boolean to check if this is the first boot
     */
    public boolean isFirstBoot() {

       // testing purposes
        if(systemController.TESTING) return true;

        // Create an empty array to store the commands to run on the system
        java.util.List<String> commands = new ArrayList<>();

        // This calls the script we want to run
        commands.add("/home/tc/FirstBootCheck.sh");

        // Runs our commands and gives the output back to us
        java.util.List<String> output = Shell.getInstance().runCommand(commands, false);

        // Check if the file is present or not
        if(output.get(0).equals("false")) {
            java.lang.System.out.println("did not find, no first boot");
            return false;
        } else {
            java.lang.System.out.println("we found the firstBoot file, should delete now and run first boot stuff");
        }

        return true;
    }

}

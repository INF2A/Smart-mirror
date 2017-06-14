package com.smartmirror.sys.view.window;

import com.smartmirror.core.view.AbstractWindow;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erwin on 6/2/2017.
 */
public class ProfileSelectionWindow extends AbstractWindow {
    // Location of the profiles
    final File profileFolder = new File("profiles");

    // Holds all the profiles
    List<File> profiles;


    public ProfileSelectionWindow() {
        profiles = loadProfiles();
        setupScreen();
    }

    /**
     * Looks up all the available profiles and stores them in a list
     * This method stores the File's of the underlying system.
     *
     * @return A list of File's wwhich are the profiles of the system
     */
    private List<File> loadProfiles() {
        // Create empty profile list;
        List<File> profileList = new ArrayList<>();

        // Check if there are profiles in the profiles folder and add to the list if there are.
        for (final File fileEntry : profileFolder.listFiles()) {
            if(fileEntry.isDirectory()) {
                profileList.add(fileEntry);
            }
        }
        return profileList;
    }

    private void setupScreen() {
        SYSTEM_Screen.setLayout(new BoxLayout(SYSTEM_Screen, BoxLayout.Y_AXIS));

        JLabel message = new JLabel("Select your profile");
        message.setAlignmentX(Component.CENTER_ALIGNMENT);
        SYSTEM_Screen.add(message);

        for (final File profile : profiles ) {
            JButton t = new JButton(profile.getName());
            t.setAlignmentX(Component.CENTER_ALIGNMENT);
            SYSTEM_Screen.add(t);
            focusManager.addComponent(t);
        }
    }
}

package com.smartmirror.sys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Erwin on 5/30/2017.
 */
public class Shell {
    private static Shell ourInstance = new Shell();

    public static Shell getInstance() {
        return ourInstance;
    }

    /**
     * Will save all changes made to the underlying system
     * Generally a good idea to use when creating and editing items
     * In short, use all the time when making a change.
     *
     * The use is necessary for the underlying OS (piCore)
     * loses all changes on reboot unless they are saved.
     * Filetool.sh will create a backup of these changes and
     * restores them on boot.
     */
    public void saveChanges() {
        runCommand(Arrays.asList("filetool.sh", "-b"), false);
    }

    /**
     * Runs commands on the underlying system
     * @param commands
     * @param showShellOutputBox
     * @return
     */
    public List<String> runCommand(List<String> commands, boolean showShellOutputBox)  {
        // Store the shell output
        List<String> returnValue = new ArrayList<>();

        // call the script by creating a new process and starting it
        ProcessBuilder pb2 = new ProcessBuilder(commands);
        pb2.redirectError();

        try {
            Process shellScript = pb2.start();

            // Create a reader for java to get the output of the script
            InputStreamReader isr = new InputStreamReader(shellScript.getInputStream());
            BufferedReader stdInput = new BufferedReader(isr);

            String s;
            while ((s = stdInput.readLine()) != null) {
                returnValue.add(s);
            }
            shellScript.destroy();
            stdInput.close();
            isr.close();

        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return returnValue;
    }
}

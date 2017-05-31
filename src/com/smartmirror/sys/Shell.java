package com.smartmirror.sys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erwin on 5/30/2017.
 */
public class Shell {
    private static Shell ourInstance = new Shell();

    public static Shell getInstance() {
        return ourInstance;
    }


    public List<String> runCommand(List<String> commands, boolean showShellOutputBox)  {
        // Store the shell output
        List<String> returnValue = new ArrayList<String>();

        // call the script by creating a new process and starting it
        ProcessBuilder pb2 = new ProcessBuilder(commands);
        pb2.redirectError();

        try {
            Process shellScript = pb2.start();

            // Create a reader for java to get the output of the script
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(shellScript.getInputStream()));
            //        BufferedWriter stdOutput = new BufferedWriter(new OutputStreamWriter(wifiScript.getOutputStream()));
            //        BufferedReader stdError = new BufferedReader(new InputStreamReader(wifiScript.getErrorStream()));

            String s = null;
            while ((s = stdInput.readLine()) != null) {
                returnValue.add(s);
            }

            stdInput.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return returnValue;

    }
}

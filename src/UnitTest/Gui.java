package UnitTest;// Version 2

//import com.hopding.jrpicam.RPiCamera;
//import com.hopding.jrpicam.enums.Exposure;
//import com.hopding.jrpicam.exceptions.FailedToRunRaspistillException;

import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

class Gui  {

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { new Gui().createAndShowGUI(); }
        });
    }

    private JFrame frame;
    private Test panel;
    private JLabel label;
    private JTextArea text;

//    private RPiCamera piCamera;
    private BufferedImage image;

    public Gui() {
//        try {
//            piCamera = new RPiCamera("/home/pi/Pictures");
//        } catch (FailedToRunRaspistillException e) {
//            e.printStackTrace();
//        }

//        piCamera.setWidth(500).setHeight(500) // Set Camera to produce 500x500 images.
//                .setBrightness(75)                // Adjust Camera's brightness setting.
//                .setExposure(Exposure.AUTO)       // Set Camera's exposure.
//                .setTimeout(2);                   // Set Camera's timeout.
    }

    private void runThread() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                getAP();
            }
        });

        t.start();
    }


    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private void createAndShowGUI() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        //Create and set up the WindowPluginTest.
        frame = new JFrame("HelloWorldSwing");
        frame.setSize(screenSize);
        //frame.setUndecorated(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        panel = new Test();
        panel.setSize(screenSize);
        panel.setBackground(Color.BLACK);

        //Add the ubiquitous "Hello World" label.
        label = new JLabel("Hello World, press Caps Lock to exit");
        label.setForeground(Color.WHITE);

        text = new JTextArea();
        text.setBackground(Color.BLACK);
        text.setForeground(Color.WHITE);

        JButton t = new JButton("show picture");
        t.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadUsingReflectionPluginTest();
            }
        });

//        JPanel bluePanel = new JPanel();
//        bluePanel.setBackground(Color.cyan);
//        bluePanel.add(new JLabel("test panel!"));
//        panel.add(bluePanel);

        panel.add(t);
        panel.add(label);
        panel.add(text);
        frame.getContentPane().add(panel);

        //Display the WindowPluginTest.
        frame.setVisible(true);

        closeOnKey();

        //     runThread();
    }
    private void loadArchiveWithLibraryLoader() {
        LibraryLoader pl = new LibraryLoader();

        // get the jar file
        File jar = new File("plugins\\clock.jar");
        try {
            pl.loadLibrary(jar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // pluginloader(returns jpanel view)
    private void loadUsingReflectionPluginTest() {

        // get the plugin
        File yourJarFile = new File("plugins\\clock.jar");

        // create classloader from main class (this class is main class (Gui)
        URLClassLoader child = null;
        try {
            child = new URLClassLoader(new URL[]{yourJarFile.toURI().toURL()}, Gui.class.getClassLoader());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // fine the class and store in clazz
        Class<?> clazz = null;
        try {
            clazz = Class.forName("com.slimpi.clock.Clock", true, child);
            // clazz = child.loadClass("com.slimpi.clock.Clock");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // grab method we want (gives jpnanel)
        Method getWidget = null;
        try {
            getWidget = clazz.getMethod("getWidget");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        // initiate the class and get the object that results from it
        Object obj = null;
        try {
            obj = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // invoke the method we previously set from the class we just inititated and store the resulting jpanel
        JPanel clockPanel = null;
        try {
            clockPanel = (JPanel) getWidget.invoke(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // add jpanel to main panel
        panel.add(clockPanel);

//        try {
//            image = piCamera.takeBufferedStill();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //panel.add(new JLabel(new ImageIcon(image)));
        panel.updateBG("img/cube.jpg");
        panel.repaint();
        frame.pack();

    }

    private void closeOnKey() {
        KeyboardFocusManager keyManager;

        keyManager=KeyboardFocusManager.getCurrentKeyboardFocusManager();
        keyManager.addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if(e.getID() == KeyEvent.KEY_PRESSED){
                    if(e.getKeyCode() == KeyEvent.VK_CAPS_LOCK) {
                        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                        System.exit(0);
                    }
                    return true;
                }
                return false;
            }

        });
    }

    private enum AP {
        NR, MAC, ESSID, QUALITY, CHANNEL, ENC, TYPE;
        public static final AP[] VALUES = values();
    }
    private void getAP() {
        Map<AP, ArrayList<String>> aps = new LinkedHashMap<>();
        // initialize the map
        for (AP ap : AP.VALUES) {
            aps.put(ap, new ArrayList<>());
        }

        List<String> commands = new ArrayList<>();
        commands.add("/home/tc/listAP.sh");
        List<String> output = runShellCommand(commands, false);

        String s = null;
        System.out.println("NR | MAC | ESSID | QUALITY | CHANNEL | ENC | TYPE");

        for (String line : output) {
            String[] tokens = line.split("\\|");
            System.out.println(line);
            if (tokens.length > 0) {
                for (int i = 0; i < tokens.length; i++) {
                    aps.get(AP.VALUES[i]).add(tokens[i].trim());
                }
            }
        }

        JPanel wifiButtons = new JPanel();
        wifiButtons.setLayout(new BoxLayout(wifiButtons, BoxLayout.PAGE_AXIS));

        for (int i = 0; i < aps.get(AP.NR).size(); i++) {
            JButton b = new JButton(aps.get(AP.ESSID).get(i) + " \t - Encrypted: " + aps.get(AP.ENC).get(i));
            b.setName(aps.get(AP.MAC).get(i));

            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String ap_MAC = b.getName();
                    for (int j = 0; j < aps.get(AP.NR).size(); j++) {
                        if(aps.get(AP.MAC).get(j).equals(ap_MAC)) {

                            String ap_ESSID = aps.get(AP.ESSID).get(j);
                            String ap_CHANNEL = aps.get(AP.CHANNEL).get(j);
                            String ap_TYPE = aps.get(AP.TYPE).get(j);
                            String ap_ENC = aps.get(AP.ENC).get(j);

                            if(ap_ENC.equals("on")) {
                                JPasswordField pf = new JPasswordField();
                                int okCxl = JOptionPane.showConfirmDialog(null, pf, "Enter network Key", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                                if (okCxl == JOptionPane.OK_OPTION) {
                                    String password = new String(pf.getPassword());
                                    connectToSelectedAP(ap_TYPE, ap_ESSID, password, ap_CHANNEL, ap_ENC);
                                    System.out.println("Selected AP: " + ap_ESSID + " ww: " + password + " Type: WPA");
                                }

                            } else {
                                System.out.println("Selected AP: " + ap_ESSID + " No encryption");
                                connectToSelectedAP(ap_TYPE, ap_ESSID, "wpa.1234", ap_CHANNEL, ap_ENC);
                            }
                            break;
                        }
                    }
                }
            });
            wifiButtons.add(b);
        }

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                panel.add(wifiButtons);
                frame.validate();
            }
        });
    }

    private void connectToSelectedAP(String type, String ESSID, String ww, String channel, String enc) {
        List<String> commands = new ArrayList<>();
        commands.add("/home/tc/connectWifi.sh");
        commands.add(type);
        commands.add(ESSID);
        commands.add(ww);
        commands.add(channel);
        commands.add(enc);

        List<String> output = runShellCommand(commands, true);
    }

    private List<String> runShellCommand(List<String> commands, boolean showShellOutputBox)  {
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
                if(showShellOutputBox) changeJLabel(label, s);
                returnValue.add(s);
            }

            stdInput.close();
        } catch (IOException e) {
            changeJLabel(label, e.toString());
        }
        return returnValue;

    }

    private void changeJLabel(final JLabel flabel, final String text) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                flabel.setText(text);
            }
        });
    }
}

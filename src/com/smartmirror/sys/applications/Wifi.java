package com.smartmirror.sys.applications;

import com.smartmirror.sys.Shell;
import com.smartmirror.sys.view.AbstractSystemApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * Created by Erwin on 5/30/2017.
 */
public class Wifi extends AbstractSystemApplication {

    private List<JComponent> wifiButtons = new ArrayList<>();
    private JPanel wifiButtonsHolder = new JPanel();
    private volatile Map<AP, ArrayList<String>> aps;

    private AccessPoint currentAP;

    /**
     * Holds the data to which the system maps scanned access points
     */
    private enum AP {
        NR, MAC, ESSID, QUALITY, CHANNEL, ENC, TYPE;
        public static final AP[] VALUES = values();
    }

    @Override
    public void setup() {
        wifiButtonsHolder.setLayout(new BoxLayout(wifiButtonsHolder, BoxLayout.PAGE_AXIS));

        JButton exit = new JButton("exit");
        exit.addActionListener(e -> SYSTEM_closeScreen());

        JButton scanWifi = new JButton("Scan");
        scanWifi.addActionListener(e -> {
            init();
        });
        SYSTEM_Screen.add(scanWifi);
        SYSTEM_Screen.add(wifiButtonsHolder);

        SYSTEM_Screen.add(exit);

        focusManager.addComponent(exit);
    }

    @Override
    public void init() {
        wifiButtonsHolder.removeAll();
        focusManager.removeComponents(wifiButtons);
        update();
        start();

    }

    private synchronized void start() {
        new Thread(() -> scanForAccessPoints() ).start();
    }


    private synchronized Map<AP, ArrayList<String>> getAPs() {
        return aps;
    }


    /**
     * Scans for access points.
     * Every access point found will create a new AccessPoint object and pass it
     * through the method addAPButton()
     * This method will call a shell script to execute on the underlying OS.
     *
     * This method is run on a different thread as to not block the U.I.
     * For every access point it finds it will pass the method adAPButton() over to
     * the EDT with the newly created AccessPoint as parameter
     */
    private void scanForAccessPoints() {
//        Thread.currentThread().setName("new thread");
//        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
//        System.out.println(threadSet.toString());

        // Run the script to scan for AP's
        List<String> commands = new ArrayList<>();
        commands.add("/home/tc/listAP.sh");
        List<String> output = Shell.getInstance().runCommand(commands, false);

        // Simulate found access points
        System.out.println("NR | MAC | ESSID | QUALITY | CHANNEL | ENC | TYPE");
        output.add("0|23e242|Secure!|35|1|on|WEP");
        output.add("1|2ahha2|wesert|50|2|off|none");
        output.add("3|2aadwdha2|eesteet|80|2|off|none");
        output.add("4|2aadwdha2|eesteet|3|2|off|none");

        // Output is given per line in the form of:
        //  "NR | MAC | ESSID | QUALITY | CHANNEL | ENC | TYPE"
        for (String data : output) {
            // simulate scanning
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            AccessPoint ap = new AccessPoint();
            ap.setWifiData(data);

            // invokes the EDT to add a button to wifiButtonsHolder panel
            // it passes the access point we created to make it
            // visible to the EDT.
            SwingUtilities.invokeLater(new Runnable() {
                AccessPoint ap;
                public Runnable init (AccessPoint ap) {
                    this.ap = ap;
                    return this;
                }
                @Override
                public void run() {
                    addAccessPoint(ap);
                }
            }.init(ap));
        }
    }


    /**
     * Adds the accesspoint to the wifiPanel
     * @param ap the accesspoint to add
     */
    private void addAccessPoint(AccessPoint ap) {

        JButton b = new JButton(ap.ESSID);
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ap.ENC.equals("on")) {
                    JPasswordField pf = new JPasswordField();
                    int okCxl = JOptionPane.showConfirmDialog(null, pf, "Enter network Key", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (okCxl == JOptionPane.OK_OPTION) {
                        String password = new String(pf.getPassword());
                        // dont change this
                        connectToSelectedAP(ap.TYPE, ap.ESSID, password, ap.CHANNEL, ap.ENC);
                        System.out.println("Selected AP: " + ap.ESSID + " ww: " + password + " Type: WPA");
                    }
                } else {
                    System.out.println("Selected AP: " + ap.ESSID + " No encryption");
                    //dont change this
                    connectToSelectedAP(ap.TYPE, ap.ESSID, "wpa.1234", ap.CHANNEL, ap.ENC);
                    }
                }
            });
        wifiButtons.add(b);
        ap.add(b);
        focusManager.addComponent(b);
        wifiButtonsHolder.add(ap);
        update();
    }


    /**
     * Tries to connect to the given wireless network
     * Talks directly with the underlying OS.
     * Calls a script on the OS that runs a shell script
     * which connects to the provided Wifi
     *
     *
     * @param TYPE Type of encryption used, if used
     * @param ESSID The name of the wifi
     * @param ww Password of the wifi, optional
     * @param CHANNEL Which channel the wifi is on
     * @param ENC If the wifi is encrypted or not
     */
    private void connectToSelectedAP(String TYPE, String ESSID, String ww, String CHANNEL, String ENC) {
        System.out.println(ESSID);
        Thread t = new Thread(() -> {
            List<String> commands = new ArrayList<>();
            commands.add("/home/tc/connectWifi.sh");
            commands.add(TYPE);
            commands.add(ESSID);
            commands.add(ww);
            commands.add(CHANNEL);
            commands.add(ENC);
            List<String> output = Shell.getInstance().runCommand(commands, true);
        });
        t.start();
    }


    /**
     * This class defines an access point
     * It holds all that is necessary to connect to it.
     *
     */
    private class AccessPoint extends JPanel {
        public boolean Connected = false;
        public String NR;
        public String MAC;
        public String ESSID;
        public String QUALITY;
        public String CHANNEL;
        public String ENC;
        public String TYPE;

        public JLabel WIFI_ICON;

        public AccessPoint() {
            this.setAlignmentY(Component.CENTER_ALIGNMENT);
            this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        }

        /**
         * returns the wifi code
         */
        private int calculateWifi(String QUALITY) {
            int q = Integer.parseInt(QUALITY);
            if(q >= 25 && q < 50) {
                return 1;
            } else if(q >= 50 && q < 75) {
                return 2;
            } else if(q >= 75) {
                return 3;
            } else {
                return 0;
            }
        }

        /**
         * returns the path to the resource of the wifi icon
         * depending on the quality
         * @return
         */
        private String getWifiIcon(String QUALITY) {
            int q = calculateWifi(QUALITY);
            String returnString = ENC.equals("on") ? "icon/wifi/wifi_lock_" : "icon/wifi/wifi_";
            switch (q) {
                case 1:
                    returnString += "1.png";
                    break;
                case 2:
                    returnString += "2.png";
                    break;
                case 3:
                    returnString += "3.png";
                    break;
                default:
                    returnString += "0.png";
                    break;
            }
            return returnString;
        }

      //  System.out.println("NR | MAC | ESSID | QUALITY | CHANNEL | ENC | TYPE");
        public void setWifiData(String data) {
            System.out.println(data);
            String[] tokens = data.split("\\|");
            if (tokens.length > 0) {
                NR = tokens[0];
                MAC = tokens[1];
                ESSID = tokens[2];
                QUALITY = tokens[3];
                CHANNEL = tokens[4];
                ENC = tokens[5];
                TYPE = tokens[6];
            }


            ImageIcon icon;
            ClassLoader cl = getClass().getClassLoader();
            icon = new ImageIcon(cl.getResource(getWifiIcon(QUALITY)));
//                icon.getScaledInstance(50, 50, 0);
            icon.setImage(icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            WIFI_ICON = new JLabel(icon);
            WIFI_ICON.setAlignmentY(Component.CENTER_ALIGNMENT);

            this.add(WIFI_ICON);

            JLabel t = new JLabel(ESSID);
            t.setAlignmentY(Component.CENTER_ALIGNMENT);
            this.add(t);
        }
    }


}
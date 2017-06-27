package com.smartmirror.sys.applications;

import com.smartmirror.core.view.IFocusManager;
import com.smartmirror.sys.*;
import com.smartmirror.sys.view.*;
import com.smartmirror.sys.view.FocusManager;
import com.smartmirror.sys.Font;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Erwin on 5/30/2017.
 */
public class Wifi extends AbstractSystemApplication {

    public List<JComponent> wifiButtons = new ArrayList<>();
    public JPanel wifiButtonsHolder = new JPanel();
    public AccessPoint selectedAP = null;

    private JButton scanWifi;
    private JPanel wifiInfoScreen;
    private JLabel connectMsg;

    private volatile boolean scanning = false;
    private volatile boolean isConnecting = false;

    private JLabel title;
    private JLabel icon;

    @Override
    public void setup() {
        wifiInfoScreen = new JPanel();
        setSYSTEM_Icon("img/wifi-icon.png");

        SYSTEM_Screen.setLayout(new BorderLayout());

        title = new JLabel("WiFi");
        title.setFont(Font.applyFontSize(Font.FontSize.H1));
        title.setForeground(Color.WHITE);

        SYSTEM_Screen.add(title, BorderLayout.PAGE_START);

        SYSTEM_Screen.setBackground(Color.BLACK);
        wifiButtonsHolder.setBackground(Color.BLACK);
        wifiButtonsHolder.setLayout(new BoxLayout(wifiButtonsHolder, BoxLayout.PAGE_AXIS));

   //     JButton exit = new JButton("exit");
//        exit.addActionListener(e -> SYSTEM_closeScreen());

        scanWifi = new JButton("Scan");
        scanWifi.addActionListener(e -> {
            init();
        });
        SYSTEM_Screen.add(scanWifi);

//        SYSTEM_Screen.add(exit);

        focusManager.addComponent(scanWifi);
        connectMsg = new JLabel("");
        connectMsg.setForeground(Color.WHITE);
  //      focusManager.addComponent(exit);

    }

    @Override
    public void init() {

        System.out.println("Wifi");
        SYSTEM_Screen.add(wifiButtonsHolder);
        wifiButtonsHolder.removeAll();
        focusManager.removeComponents(wifiButtons);
        update();
        start();
    }

    private synchronized void start() {
        if(!scanning) {
            scanning = true;
            new Thread(() -> scanForAccessPoints()).start();
        }
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
    public synchronized void scanForAccessPoints() {
//        Thread.currentThread().setName("new thread");
//        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
//        System.out.println(threadSet.toString());

        // Run the script to scan for AP's
        List<String> commands = new ArrayList<>();
        commands.add("/mnt/mmcblk0p2/tce/smartmirror/scripts/listAP.sh");

        List<String> output = Shell.getInstance().runCommand(commands, false);

        // Simulate found access points
        System.out.println("NR | MAC | ESSID | QUALITY | CHANNEL | ENC | TYPE");
        if(output.size() == 0) {
            output.add("0|2aadwdha2 |TEST-eesteet   |70/70  |2|off  |none");
            output.add("1|2ahha2    |TEST-wesert    |50/70  |2|off  |none");
            output.add("2|2ahha2    |TEST-ee        |50/70  |2|off  |none");
            output.add("3|23e242    |TEST-Secure!   |35/70  |1|on   |WEP");
            output.add("4|2aadwdha2 |TEST-eesteet   |3/70   |2|off  |none");
        }
        // Output is given per line in the form of:
        //  "NR | MAC | ESSID | QUALITY | CHANNEL | ENC | TYPE"
        for (String data : output) {
            // simulate scanning
            try {
                Thread.sleep(100);
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
        scanning = false;
    }


    /**
     * Adds the accesspoint to the wifiPanel
     * @param ap the accesspoint to add
     */
    private void addAccessPoint(AccessPoint ap) {
        wifiButtonsHolder.add(ap);
        wifiButtons.add(ap);
        focusManager.addComponent(ap);
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
            commands.add("/mnt/mmcblk0p2/tce/smartmirror/scripts/connectWifi.sh");
            commands.add(TYPE);
            commands.add(ESSID);
            commands.add(ww);
            commands.add(CHANNEL);
            commands.add(ENC);
            List<String> output = Shell.getInstance().runCommand(commands, true);
            //output.add("connected");
            SwingUtilities.invokeLater(() -> connectMsg.setText(output.get(output.size() - 1)) );
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

        public JPanel activePanel;
        JButton connectButton;
        JTextField passwordField;

        public List<JComponent> focusComponents;

        public AccessPoint() {
            this.setBackground(Color.BLACK);
            this.setAlignmentY(Component.CENTER_ALIGNMENT);
            this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        }

        /**
         * returns the wifi code
         */
        private int calculateWifi(String QUALITY) {
            String[] qu = QUALITY.split("\\/");
            int q = Integer.parseInt(qu[0]);
            if(q >= 20 && q < 30) {
                return 1;
            } else if(q >= 30 && q < 50) {
                return 2;
            } else if(q >= 50) {
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
                NR = tokens[0].trim();
                MAC = tokens[1].trim();
                ESSID = tokens[2].trim();
                QUALITY = tokens[3].trim();
                CHANNEL = tokens[4].trim();
                ENC = tokens[5].trim();
                TYPE = tokens[6].trim();
            }


            ImageIcon icon;
            icon = new ImageIcon(getClass().getClassLoader().getResource(getWifiIcon(QUALITY)));
            icon.setImage(icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            WIFI_ICON = new JLabel(icon);
            WIFI_ICON.setAlignmentY(Component.CENTER_ALIGNMENT);

            this.add(WIFI_ICON);

            JLabel t = new JLabel(ESSID);
            t.setForeground(Color.WHITE);
            t.setAlignmentY(Component.CENTER_ALIGNMENT);
            this.add(t);

            setupPanel();

        }

        private void connect() {
            connectToSelectedAP(TYPE, ESSID, "wpa.1234", CHANNEL, ENC);
            connectMsg.setText("Connecting...");
        }

        private void connectPassword(String password) {
            connectToSelectedAP(TYPE, ESSID, password, CHANNEL, ENC);
            System.out.println("Selected AP: " + ESSID + " ww: " + password + " Type: WPA");
            connectMsg.setText("Connecting...");
        }

        public void setupPanel() {
            focusComponents = new ArrayList<>();
            activePanel = new JPanel();
            activePanel.setLayout(new BorderLayout());
            connectButton = new JButton("connect");

            activePanel.add(new JLabel(ESSID), BorderLayout.NORTH);
            if(ENC.equals("on")) {
                activePanel.add(new JLabel("Encrpytion: " + TYPE), BorderLayout.WEST);
                passwordField = new JTextField("", 20);
                activePanel.add(passwordField, BorderLayout.CENTER);
                focusComponents.add(passwordField);
                connectButton.addActionListener(e -> connectPassword(passwordField.getText()));
            } else {
                connectButton.addActionListener(e -> connect());
            }
            activePanel.add(connectButton, BorderLayout.SOUTH);
            focusComponents.add(connectButton);
         }
    }


    private void showWifiInfoScreen() {
        SYSTEM_Screen.remove(wifiButtonsHolder);
        SYSTEM_Screen.add(selectedAP.activePanel);
        SYSTEM_Screen.add(connectMsg);
        SYSTEM_Screen.remove(scanWifi);
        focusManager.removeComponents(wifiButtons);
        focusManager.removeComponent(scanWifi);
        focusManager.addComponents(selectedAP.focusComponents);
        focusManager.Reset();
        if(selectedAP.ENC.equals("on")) onMenuButton();
        update();
    }

    private void hideWifiInfoScreen()
    {
        connectMsg.setText("");
        SYSTEM_Screen.remove(connectMsg);
        SYSTEM_Screen.remove(selectedAP.activePanel);
        focusManager.removeComponents(selectedAP.focusComponents);
        SYSTEM_Screen.add(scanWifi);
        focusManager.addComponent(scanWifi);
        init();
    }

    @Override
    public void onBackButton() {
        super.onBackButton();
        if(selectedAP != null) {
            hideWifiInfoScreen();
            selectedAP = null;
        } else {
            SYSTEM_closeScreen();
        }
    }

    @Override
    public void onMenuButton() {
        super.onMenuButton();
        if(focusManager.Current() instanceof AccessPoint) {
            System.out.println("clicked on AP");
            selectedAP = (AccessPoint)focusManager.Current();
            showWifiInfoScreen();
        }
    }

}

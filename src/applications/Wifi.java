package applications;

import com.smartmirror.sys.Shell;
import com.smartmirror.sys.view.AbstractSystemApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Erwin on 5/30/2017.
 */
public class Wifi extends AbstractSystemApplication {

    private JPanel wifiButtons = new JPanel();

    private enum AP {
        NR, MAC, ESSID, QUALITY, CHANNEL, ENC, TYPE;
        public static final AP[] VALUES = values();
    }

    @Override
    public void setup() {
        JButton t = new JButton("exit");
        t.addActionListener(e -> SYSTEM_closeScreen());
        SYSTEM_Screen.add(t);
        focusComponents.add(t);
        start();
    }

    @Override
    public void init() {
    }


    private void start() {
        Thread t = new Thread(this::getAP);
        t.start();
    }

    private void getAP() {
        Map<AP, ArrayList<String>> aps = new LinkedHashMap<>();
        // initialize the map
        for (AP ap : AP.VALUES) {
            aps.put(ap, new ArrayList<>());
        }

        List<String> commands = new ArrayList<>();
        commands.add("/home/tc/listAP.sh");
        List<String> output = Shell.getInstance().runCommand(commands, false);

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

        EventQueue.invokeLater(() -> SYSTEM_Screen.add(wifiButtons));
    }


    private void connectToSelectedAP(String type, String ESSID, String ww, String channel, String enc) {
        Thread t = new Thread(() -> {
            List<String> commands = new ArrayList<>();
            commands.add("/home/tc/connectWifi.sh");
            commands.add(type);
            commands.add(ESSID);
            commands.add(ww);
            commands.add(channel);
            commands.add(enc);
            List<String> output = Shell.getInstance().runCommand(commands, true);
        });
        t.start();
    }
}

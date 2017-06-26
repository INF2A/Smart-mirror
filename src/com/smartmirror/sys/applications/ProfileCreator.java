package com.smartmirror.sys.applications;

import com.smartmirror.sys.DB;
import com.smartmirror.sys.view.AbstractSystemApplication;

import javax.swing.*;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Erwin on 5/31/2017.
 */
public class ProfileCreator extends AbstractSystemApplication {
    String profile_name;
    String profile_ww;
    String email_ww;

    JTextField nameInput;
    JTextField emailInput;
    JPasswordField wwInput;

    public JPanel accountCreatorPanel;

    @Override
    public void setup() {
        accountCreatorPanel = new JPanel();
        accountCreatorPanel.setLayout(new BoxLayout(accountCreatorPanel, BoxLayout.X_AXIS));
        SYSTEM_Screen.add(accountCreatorPanel);

        nameInput = new JTextField();
        emailInput = new JTextField();
        wwInput = new JPasswordField();
        accountCreatorPanel.add(new JLabel("Account name"));
        accountCreatorPanel.add(nameInput);
        accountCreatorPanel.add(new JLabel("Email"));
        accountCreatorPanel.add(emailInput);
        accountCreatorPanel.add(new JLabel("Password"));
        accountCreatorPanel.add(wwInput);

        JButton t = new JButton("reg");
        t.addActionListener(e -> createAccount());
        accountCreatorPanel.add(t);
    }

    @Override
    public void init() {

        System.out.println("ProfileCreator");
        reset();
    }

    private void reset() {
        SYSTEM_Screen.removeAll();
        nameInput.setText("");
        emailInput.setText("");
        wwInput.setText("");
        profile_name = "";
        profile_ww = "";
        email_ww = "";
        SYSTEM_Screen.add(accountCreatorPanel);
    }

    private void createAccount() {
        Statement s = null;
        try {
            s = DB.getConnection().createStatement();
            String sql = "INSERT INTO test " +
                    "(Name, Email, Password) " +
                    "VALUES ('karel', 'karel@test.com', '3rko93f')";
            s.execute(sql);
            s.close();
            DB.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkExistingAccount() {

    }

    @Override
    public void onBackButton() {
        super.onBackButton();
        SYSTEM_closeScreen();
    }

}

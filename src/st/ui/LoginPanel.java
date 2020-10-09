package st.ui;

import st.Account;
import st.Satellite;
import st.SatelliteManager;

import javax.swing.*;
import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static javax.swing.SpringLayout.NORTH;
import static javax.swing.SpringLayout.EAST;
import static javax.swing.SpringLayout.SOUTH;
import static javax.swing.SpringLayout.WEST;

public class LoginPanel extends JPanel {


    private JLabel usernameLabel, passwordLabel, error;
    private JTextField usernameField, passwordField;
    private JButton login, create;

    private ControlPanel parent;

    public LoginPanel(ControlPanel parent) {
        this.parent = parent;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        setPreferredSize(new Dimension(300, 150));

        SpringLayout sl = new SpringLayout();
        this.setLayout(sl);

        usernameLabel = new JLabel("Username:");
        sl.putConstraint(WEST, usernameLabel, 10, WEST, this);
        sl.putConstraint(NORTH, usernameLabel, 10, NORTH, this);
        this.add(usernameLabel);

        usernameField = new JTextField();
        sl.putConstraint(WEST, usernameField, 10, EAST, usernameLabel);
        sl.putConstraint(EAST, usernameField, -10, EAST, this);
        sl.putConstraint(NORTH, usernameField, 10, NORTH, this);
        this.add(usernameField);

        passwordLabel = new JLabel("Password:");
        sl.putConstraint(WEST, passwordLabel, 10, WEST, this);
        sl.putConstraint(NORTH, passwordLabel, 10, SOUTH, usernameField);
        this.add(passwordLabel);

        passwordField = new JPasswordField();
        sl.putConstraint(WEST, passwordField, 0, WEST, usernameField);
        sl.putConstraint(EAST, passwordField, -10, EAST, this);
        sl.putConstraint(NORTH, passwordField, 10, SOUTH, usernameField);
        this.add(passwordField);

        create = new JButton("Sign up");
        sl.putConstraint(WEST, create, 10, WEST, this);
        sl.putConstraint(NORTH, create, 20, SOUTH, passwordField);
        this.add(create);

        login = new JButton("Sign in");
        sl.putConstraint(WEST, login, 20, EAST, create);
        sl.putConstraint(NORTH, login, 20, SOUTH, passwordField);
        this.add(login);

        error = new JLabel("");
        sl.putConstraint(WEST, error, 10, WEST, this);
        sl.putConstraint(NORTH, error, 20, SOUTH, create);
        sl.putConstraint(EAST, error, -10, EAST, this);
        sl.putConstraint(SOUTH, error, -10, SOUTH, this);
        this.add(error);

        create.addActionListener(e -> {
            try {
                parent.setAccount(Account.createAccount(usernameField.getText(), passwordField.getText()));
            } catch (Exception e1) {
                error.setText(e1.getMessage());
            }
        });

        login.addActionListener(e -> {
            try {
                parent.setAccount(new Account(usernameField.getText(), passwordField.getText()));
            } catch (Exception e1) {
                error.setText(e1.getMessage());
            }
        });


    }

}

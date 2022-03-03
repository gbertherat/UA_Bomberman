package client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ViewConnection extends Frame{
    private JFrame frame;

    private JPanel getServerPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel ipLabel = new JLabel("IP:");
        ipLabel.setBounds(50,125,200,20);
        panel.add(ipLabel);

        JTextField ipField = new JTextField();
        ipField.setBounds(200,120,220,30);
        panel.add(ipField);

        JLabel portLabel = new JLabel("Port:");
        portLabel.setBounds(50,185,200,20);
        panel.add(portLabel);

        JTextField portField = new JTextField();
        portField.setBounds(200,180,220,30);
        panel.add(portField);

        JButton cfrmButton = new JButton("Valider");
        cfrmButton.setBounds(200,250,100,30);
        panel.add(cfrmButton);

        return panel;
    }

    @Override
    public void init(int width, int height, int yoffset) {
        super.init(width,height, yoffset);
        frame = super.getJFrame();

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Login:");
        userLabel.setBounds(50,125,200,20);
        panel.add(userLabel);

        JTextField userTextField = new JTextField();
        userTextField.setBounds(200,120,220,30);
        panel.add(userTextField);

        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordLabel.setBounds(50,185,200,20);
        panel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(200,180,220,30);
        panel.add(passwordField);

        JButton cfrmButton = new JButton("Valider");
        cfrmButton.setBounds(200,250,100,30);
        panel.add(cfrmButton);

        cfrmButton.addActionListener(e -> {
            frame.setContentPane(getServerPanel());
            frame.repaint();
            frame.setVisible(true);
        });

        JLabel accLabel = new JLabel("Pour créer un compte, allez sur le site.");
        accLabel.setBounds(100,300,300,30);
        panel.add(accLabel);

        JTextField accField = new JTextField("127.0.0.1:8080/BombermanJEE/Register");
        accField.setEditable(false);
        accField.setFont(new Font("Calibri", Font.PLAIN, 12));
        accField.setAlignmentX(JTextField.CENTER);
        accField.setBounds(100,350,300,30);
        panel.add(accField);

        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}

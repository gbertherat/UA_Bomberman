package client.view;

import javax.swing.*;

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

        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}

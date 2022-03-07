package client.view;

import client.Client;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ViewConnection extends Frame{
    private JFrame frame;

    @Override
    public void init(int width, int height, int yoffset) {
        super.init(width,height, yoffset);
        frame = super.getJFrame();
        frame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel titleLabel = new JLabel("Bomberman");
        titleLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        titleLabel.setBounds(175,50,200,24);
        panel.add(titleLabel);

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

        JLabel ipLabel = new JLabel("IP:");
        ipLabel.setBounds(50,245,200,20);
        panel.add(ipLabel);

        JTextField ipField = new JTextField("127.0.0.1");
        ipField.setBounds(200,240,220,30);
        panel.add(ipField);

        JLabel portLabel = new JLabel("Port:");
        portLabel.setBounds(50,305,200,20);
        panel.add(portLabel);

        JTextField portField = new JTextField();
        portField.setBounds(200,300,220,30);
        panel.add(portField);

        JLabel accLabel = new JLabel("Pour créer un compte, allez sur le site.");
        accLabel.setBounds(100,350,300,30);
        panel.add(accLabel);

        JTextField accField = new JTextField("127.0.0.1:8080/BombermanJEE/Register");
        accField.setEditable(false);
        accField.setFont(new Font("Calibri", Font.PLAIN, 12));
        accField.setAlignmentX(JTextField.CENTER);
        accField.setBounds(100,400,300,30);
        panel.add(accField);

        JButton cfrmButton = new JButton("Valider");
        cfrmButton.setBounds(200,450,100,30);
        panel.add(cfrmButton);

        JLabel errLabel = new JLabel();
        errLabel.setForeground(Color.RED);
        errLabel.setFont(new Font("Calibri", Font.PLAIN, 12));
        errLabel.setBounds(50,500,300,30);
        panel.add(errLabel);

        cfrmButton.addActionListener(e -> {
            try {
                if(userTextField.getText().isEmpty() || new String(passwordField.getPassword()).isEmpty() || ipField.getText().isEmpty() || portField.getText().isEmpty()){
                    errLabel.setText("Veuillez remplir tous les champs!");
                    return;
                }

                Socket socket = new Socket(ipField.getText(), Integer.parseInt(portField.getText()));

                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println("username="+userTextField.getText()+"&password="+new String(passwordField.getPassword()));

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String line = null;
                while((line = reader.readLine()) == null){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

                if(line.equals("-1")){
                    errLabel.setText("Vous êtes déjà connecté à ce serveur!");
                    return;
                } else if(line.equals("-2")){
                    errLabel.setText("Identifiants incorrects");
                    return;
                }

                int id = Integer.parseInt(line);
                Client.startClient(socket, id);

                frame.setVisible(false);
                frame.dispose();
            } catch (UnknownHostException | ConnectException el) {
                errLabel.setText("Le serveur n'est pas joignable.");
            } catch (NumberFormatException el){
                errLabel.setText("Veuillez entrer des valeurs valides!");
            } catch (IOException el) {
                el.printStackTrace();
                errLabel.setText("Une erreur est survenue.");
            }
        });

        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}

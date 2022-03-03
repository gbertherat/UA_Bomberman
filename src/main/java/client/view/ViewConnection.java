package client.view;

import client.Client;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ViewConnection extends Frame{
    private JFrame frame;

    private JPanel getServerPanel(int id){
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel titleLabel = new JLabel("Bomberman");
        titleLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        titleLabel.setBounds(175,50,200,24);
        panel.add(titleLabel);

        JLabel ipLabel = new JLabel("IP:");
        ipLabel.setBounds(50,125,200,20);
        panel.add(ipLabel);

        JTextField ipField = new JTextField("127.0.0.1");
        ipField.setBounds(200,120,220,30);
        panel.add(ipField);

        JLabel portLabel = new JLabel("Port:");
        portLabel.setBounds(50,185,200,20);
        panel.add(portLabel);

        JTextField portField = new JTextField();
        portField.setBounds(200,180,220,30);
        panel.add(portField);

        JLabel errLabel = new JLabel();
        errLabel.setForeground(Color.RED);
        errLabel.setFont(new Font("Calibri", Font.PLAIN, 12));
        errLabel.setBounds(50,300,300,30);
        panel.add(errLabel);

        JButton cfrmButton = new JButton("Valider");
        cfrmButton.setBounds(200,250,100,30);
        panel.add(cfrmButton);

        cfrmButton.addActionListener(e -> {
            try {
                Socket socket = new Socket(ipField.getText(), Integer.parseInt(portField.getText()));
                frame.setVisible(false);
                frame.dispose();

                Client.startClient(socket, id);
            } catch (UnknownHostException el) {
                errLabel.setText("Le serveur n'est pas joignable.");
            } catch (NumberFormatException el){
                errLabel.setText("Veuillez entrer des valeurs valides!");
            } catch (IOException el) {
                errLabel.setText("Une erreur est survenue.");
            }
        });

        return panel;
    }

    @Override
    public void init(int width, int height, int yoffset) {
        super.init(width,height, yoffset);
        frame = super.getJFrame();

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

        JLabel accLabel = new JLabel("Pour créer un compte, allez sur le site.");
        accLabel.setBounds(100,300,300,30);
        panel.add(accLabel);

        JTextField accField = new JTextField("127.0.0.1:8080/BombermanJEE/Register");
        accField.setEditable(false);
        accField.setFont(new Font("Calibri", Font.PLAIN, 12));
        accField.setAlignmentX(JTextField.CENTER);
        accField.setBounds(100,350,300,30);
        panel.add(accField);

        JButton cfrmButton = new JButton("Valider");
        cfrmButton.setBounds(200,250,100,30);
        panel.add(cfrmButton);

        JLabel errLabel = new JLabel();
        errLabel.setForeground(Color.RED);
        errLabel.setFont(new Font("Calibri", Font.PLAIN, 12));
        errLabel.setBounds(50,400,300,30);
        panel.add(errLabel);

        cfrmButton.addActionListener(e -> {
            try {
                CloseableHttpClient client = HttpClients.createDefault();
                HttpPost request = new HttpPost("http://127.0.0.1:8080/BombermanJEE/user/login");

                ArrayList<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("login", userTextField.getText()));
                params.add(new BasicNameValuePair("password", new String(passwordField.getPassword())));
                request.setEntity(new UrlEncodedFormEntity(params));

                try (CloseableHttpResponse response = client.execute(request)) {
                    HttpEntity entity = response.getEntity();

                    if (entity != null) {
                        String result = EntityUtils.toString(entity).trim();
                        if(!result.equals("null")){
                            frame.setContentPane(getServerPanel(Integer.parseInt(result)));
                            frame.setVisible(true);
                            frame.repaint();
                        }
                    }
                }

            } catch (IOException el) {
                errLabel.setText("Une erreur est survenue.");
            }
        });

        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}

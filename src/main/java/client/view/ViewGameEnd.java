package client.view;

import javax.swing.*;
import java.awt.*;

public class ViewGameEnd extends Frame{
    private JFrame frame;
    private final String reason;

    public ViewGameEnd(String reason){
        this.reason = reason;
    }

    @Override
    public void init(int width, int height, int yoffset) {
        super.init(width, height, yoffset);
        frame = super.getJFrame();
        frame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel gameOverLabel = new JLabel("Game Over!");
        gameOverLabel.setFont(new Font("Calibri", Font.BOLD, 28));
        gameOverLabel.setBounds(150, 50, 300, 50);
        panel.add(gameOverLabel);

        JLabel gameOverReasonLabel = new JLabel(reason);
        gameOverReasonLabel.setFont(new Font("Calibri", Font.PLAIN, 16));
        gameOverReasonLabel.setBounds(175, 150, 300, 50);
        panel.add(gameOverReasonLabel);

        JButton continueButton = new JButton("Continuer");
        continueButton.setBounds(150, 250, 200, 50);
        continueButton.addActionListener(e -> {
            ViewConnection connection = new ViewConnection();
            connection.init(500,600,-100);
            frame.setVisible(false);
            frame.dispose();
        });
        panel.add(continueButton);

        JButton closeButton = new JButton("Quitter");
        closeButton.setBounds(150, 350, 200, 50);
        closeButton.addActionListener(e -> System.exit(0));
        panel.add(closeButton);

        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}

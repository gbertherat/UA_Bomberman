package view;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;

public class ViewSimpleGame extends Frame {
    private JFrame frame;
    private JLabel turnLabel = new JLabel("Tour: 0", JLabel.CENTER);

    @Override
    public void init(int width, int height, int yoffset) {
        super.init(width, height, yoffset);
        frame = super.getJFrame();
        JPanel mainPanel = new JPanel(new GridLayout(1,1));
        frame.setContentPane(mainPanel);
        mainPanel.add(turnLabel);
        frame.setVisible(true);
    }

    private void setTurnLabel(String turnNo){
        turnLabel.setText("Turn : " + turnNo);
    }

    @Override
    public void update(Observable o, Object arg) {
        setTurnLabel(String.valueOf(arg));
    }
}

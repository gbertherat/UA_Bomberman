package view;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

public class ViewBombermanGame extends Frame implements Observer {
    private JFrame frame;

    @Override
    public void init(int width, int height, int yoffset) {
        super.init(width, height, yoffset);
        frame = super.getJFrame();

        frame.setVisible(true);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}

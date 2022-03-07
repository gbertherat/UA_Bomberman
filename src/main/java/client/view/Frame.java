package client.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Observer;

public abstract class Frame {
    private JFrame jFrame;

    public JFrame getJFrame(){
        return jFrame;
    }

    public void updateSize(int width, int height, int yoffset){
        jFrame.setSize(new Dimension(width, height));
        Dimension windowSize = jFrame.getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int dx = centerPoint.x - windowSize.width/2;
        int dy = centerPoint.y - windowSize.height/2 + yoffset;
        jFrame.setLocation(dx, dy);
    }

    public void init(int width, int height, int yoffset){
        jFrame = new JFrame();
        jFrame.setTitle("Game");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        updateSize(width, height, yoffset);
    }

    public Image getImage(String foldername, String imagename){
        try {
            return ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource(foldername + "/" + imagename)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

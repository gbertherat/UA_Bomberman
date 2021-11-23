package view;

import controller.AbstractController;
import model.BombermanGame;
import view.strategy.CommandStrategy;
import view.strategy.EtatCreated;
import view.strategy.EtatFinished;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;

public class ViewCommand extends Frame {
    private JFrame frame;

    private JButton playButton;
    private JButton stepButton;
    private JButton pauseButton;
    private JButton restartButton;

    private JLabel turnLabel;
    private JLabel gameOverLabel;
    private CommandStrategy etat;
    private AbstractController controller;

    public ViewCommand(AbstractController controller){
        this.controller = controller;
        playButton = new JButton(new ImageIcon(getImage("icons", "icon_play.png")));
        stepButton = new JButton(new ImageIcon(getImage("icons", "icon_step.png")));
        pauseButton = new JButton(new ImageIcon(getImage("icons", "icon_pause.png")));
        restartButton = new JButton(new ImageIcon(getImage("icons", "icon_restart.png")));
        turnLabel = new JLabel("Tour: 1", JLabel.CENTER);
        gameOverLabel = new JLabel("", JLabel.CENTER);
        gameOverLabel.setForeground(Color.red);
        gameOverLabel.setFont(new Font("Arial", Font.PLAIN, 18));
    }

    public void setEtat(CommandStrategy etat) {
        this.etat = etat;
    }

    public void setPlayButtonEnabled(boolean value){
        playButton.setEnabled(value);
    }

    public void setStepButtonEnabled(boolean value){
        stepButton.setEnabled(value);
    }

    public void setPauseButtonEnabled(boolean value){
        pauseButton.setEnabled(value);
    }

    public void setRestartButtonEnabled(boolean value){
        restartButton.setEnabled(value);
    }


    @Override
    public void init(int width, int height, int yoffset) {
        super.init(width, height, yoffset);
        frame = super.getJFrame();

        JPanel mainPanel = new JPanel(new GridLayout(2,1));
        frame.setContentPane(mainPanel);

        JPanel topPanel = new JPanel(new GridLayout(1,4));
        mainPanel.add(topPanel);

        playButton.addActionListener(e -> {
            etat.play();
            controller.play();
        });

        pauseButton.addActionListener(e -> {
            etat.pause();
            controller.pause();
        });

        stepButton.addActionListener(e -> {
            controller.step();
        });

        restartButton.addActionListener(e -> {
            etat.restart();
            controller.restart();
        });

        topPanel.add(playButton);
        topPanel.add(pauseButton);
        topPanel.add(stepButton);
        topPanel.add(restartButton);

        JPanel bottomPanel = new JPanel(new GridLayout(1,2));
        mainPanel.add(bottomPanel);

        JPanel bottomLeftPanel = new JPanel(new GridLayout(2,1));
        bottomPanel.add(bottomLeftPanel);

        JLabel sliderLabel = new JLabel("Temps entre les tours (s)", JLabel.CENTER);
        bottomLeftPanel.add(sliderLabel);

        JSlider slider = new JSlider(0,1,10,1);
        slider.setMinorTickSpacing(1);
        slider.setMajorTickSpacing(1);
        slider.setSnapToTicks(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(e -> controller.setSpeed(slider.getValue()));
        bottomLeftPanel.add(slider);

        JPanel bottomRightPanel = new JPanel(new GridLayout(2, 1));

        bottomRightPanel.add(turnLabel);
        bottomRightPanel.add(gameOverLabel);
        bottomPanel.add(bottomRightPanel);

        frame.setVisible(true);
        setEtat(new EtatCreated(this));
    }

    public void setTurnLabel(String turnNo){
        this.turnLabel.setText("Tour: " + turnNo);
    }

    public void setGameOverLabel(String label) {
        this.gameOverLabel.setText(label);
    }

    @Override
    public void update(Observable o, Object arg) {
        BombermanGame game = (BombermanGame) arg;
        setTurnLabel(String.valueOf(game.getTurn()));
        setGameOverLabel(game.getGameOverReason());

        if(game.isFinished()){
            setEtat(new EtatFinished(this));
        }
    }
}

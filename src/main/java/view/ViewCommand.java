package view;

import controller.AbstractController;
import controller.ControllerBombermanGame;
import model.BombermanGame;
import view.strategy.CommandStrategy;
import view.strategy.EtatCreated;
import view.strategy.EtatFinished;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.util.Locale;
import java.util.Observable;

public class ViewCommand extends Frame {

    private final JButton playButton;
    private final JButton stepButton;
    private final JButton pauseButton;
    private final JButton restartButton;

    private final JLabel turnLabel;
    private final JLabel gameOverLabel;
    private CommandStrategy etat;
    private final AbstractController controller;

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
        JFrame frame = super.getJFrame();

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openFileItem = new JMenuItem("Load layout");
        openFileItem.addActionListener(e -> {
            JFileChooser layoutChooser = new JFileChooser(new File("."));
            layoutChooser.setDialogTitle("Load map layout");
            layoutChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileFilter filter = new FileFilter() {
                @Override
                public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return true;
                    } else {
                        return f.getName().toLowerCase().endsWith(".lay");
                    }
                }

                @Override
                public String getDescription() {
                    return "Map layout (*.lay)";
                }
            };
            layoutChooser.setFileFilter(filter);
            layoutChooser.setAcceptAllFileFilterUsed(false);
            int result = layoutChooser.showOpenDialog(null);
            if(result == JFileChooser.APPROVE_OPTION){
                File selectedFile = layoutChooser.getSelectedFile();
                ((ControllerBombermanGame) controller).changeMap(selectedFile.getAbsolutePath());
            }
        });
        fileMenu.add(openFileItem);

        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

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

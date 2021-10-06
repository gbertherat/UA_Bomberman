package view;

import model.BombermanGame;
import model.Game;
import model.InputMap;
import utils.InfoAgent;
import utils.InfoBomb;
import utils.InfoItem;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Observable;

public class ViewBombermanGame extends Frame{
    private JFrame frame;
    private PanelBomberman mainPanel;
    private InputMap map;

    public void setMap(InputMap map){
        this.map = map;
    }

    public void setMainPanel(PanelBomberman mainPanel) {
        this.mainPanel = mainPanel;
    }

    public void restart(){
        mainPanel.updateInfoGame(
                map.getStart_breakable_walls(),
                map.getStart_agents(),
                new ArrayList<>(),
                new ArrayList<>());
        mainPanel.repaint();
    }

    @Override
    public void init(int width, int height, int yoffset) {
        super.init(width, height, yoffset);
        frame = super.getJFrame();
        int cols = map.get_walls()[0].length;
        int rows = map.get_walls().length;
        mainPanel = new PanelBomberman(rows,
                cols,
                map.get_walls(),
                map.getStart_breakable_walls(),
                map.getStart_agents());
        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        BombermanGame game = (BombermanGame) o;
        ArrayList<InfoAgent> infoAgents = new ArrayList<>();
        game.getCharacterList().forEach(e -> infoAgents.add(e.getInfo()));

        mainPanel.updateInfoGame(game.getBreakableWalls(), infoAgents, game.getItemList(), game.getBombList());
        mainPanel.repaint();
    }
}

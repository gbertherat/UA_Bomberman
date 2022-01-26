package client.view;

import model.BombermanGame;
import model.InputMap;
import utils.InfoAgent;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Observable;

public class ViewBombermanGame extends Frame{
    private JFrame frame;
    private PanelBomberman mainPanel;
    private InputMap map;

    public PanelBomberman getPanel(){
        return mainPanel;
    }

    public void setMap(InputMap map){
        this.map = map;
    }

    public InputMap getMap() {
        return map;
    }

    public void restart(){
        super.updateSize(map.get_walls().length*48,map.get_walls()[0].length*48,-100);
        int cols = map.get_walls()[0].length;
        int rows = map.get_walls().length;
        mainPanel = new PanelBomberman(rows,
                cols,
                map.get_walls(),
                map.getStart_breakable_walls(),
                map.getStart_agents());
        frame.setContentPane(mainPanel);
        mainPanel.repaint();
        frame.repaint();
    }

    @Override
    public void init(int width, int height, int yoffset) {
        super.init(map.get_walls().length*48,map.get_walls()[0].length*48,-100);
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
        frame.setFocusable(false);
    }

    public void close(){
        this.frame.dispose();
    }

    @Override
    public void update(Observable o, Object arg) {
        BombermanGame game = (BombermanGame) o;
        ArrayList<InfoAgent> infoAgents = new ArrayList<>();
        for(char c : game.getCharacterMap().keySet()){
            game.getCharacterMap().get(c).forEach(e -> infoAgents.add(e.getInfo()));
        }

        mainPanel.updateInfoGame(game.getBreakableWalls(), infoAgents, game.getItemList(), game.getBombList());
        mainPanel.repaint();
    }
}

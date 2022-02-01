package client.view;

import model.BombermanGame;
import model.InputMap;
import server.controller.ControllerBombermanGame;
import utils.AgentAction;
import utils.InfoAgent;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Observable;

public class ViewBombermanGame extends Frame{
    private JFrame frame;
    private PanelBomberman mainPanel;
    private InputMap map;
    private AgentAction action;

    public PanelBomberman getPanel(){
        return mainPanel;
    }

    public void setMap(InputMap map){
        this.map = map;
    }

    public InputMap getMap() {
        return map;
    }

    public AgentAction getAction() {
        return action;
    }

    public void setAction(AgentAction action) {
        this.action = action;
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
        frame.setFocusable(true);
        this.action = AgentAction.STOP;
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                AgentAction action = AgentAction.STOP;
                switch(e.getKeyCode()){
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_Z:
                        action = AgentAction.MOVE_UP;
                        break;
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_S:
                        action = AgentAction.MOVE_DOWN;
                        break;
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_Q:
                        action = AgentAction.MOVE_LEFT;
                        break;
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_D:
                        action = AgentAction.MOVE_RIGHT;
                        break;
                    case KeyEvent.VK_SPACE:
                    case KeyEvent.VK_E:
                        action = AgentAction.PUT_BOMB;
                        break;
                }

                setAction(action);
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });
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

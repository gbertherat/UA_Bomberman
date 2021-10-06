package agent;

import model.BombermanGame;
import model.InputMap;
import utils.AgentAction;
import utils.ColorAgent;
import utils.InfoAgent;

import java.util.Random;

public class Bird extends Character{
    public Bird(int x, int y) {
        super(new InfoAgent(x, y,
                AgentAction.STOP,
                'V',
                ColorAgent.values()[new Random().nextInt(ColorAgent.values().length)],
                false,
                false));
    }

    @Override
    public boolean isLegalMove(int xdir, int ydir, BombermanGame game) {
        boolean[][] walls = game.getMap().get_walls();

        int nextx = getInfo().getX() + xdir;
        int nexty = getInfo().getY() + ydir;

        if(nextx >= 0 && nexty >= 0 && nextx < walls.length && nexty < walls[nextx].length) {
            return !walls[nextx][nexty];
        } else {
            return false;
        }
    }
}

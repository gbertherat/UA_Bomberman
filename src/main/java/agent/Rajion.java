package agent;

import model.BombermanGame;
import utils.AgentAction;
import utils.ColorAgent;
import utils.InfoAgent;
import utils.InfoBomb;

import java.util.ArrayList;
import java.util.Random;

public class Rajion extends Character{
    public Rajion(int x, int y) {
        super(new InfoAgent(x, y,
                AgentAction.STOP,
                'E',
                ColorAgent.values()[new Random().nextInt(ColorAgent.values().length)],
                false,
                false));
    }

    @Override
    public boolean isLegalMove(int xdir, int ydir, BombermanGame game) {
        boolean[][] walls = game.getMap().get_walls();
        boolean[][] breakableWalls = game.getBreakableWalls();
        ArrayList<InfoBomb> bombList = game.getBombList();

        int nextx = getInfo().getX() + xdir;
        int nexty = getInfo().getY() + ydir;

        for(InfoBomb bomb : bombList){
            if(bomb.getX() == nextx && bomb.getY() == nexty){
                return false;
            }
        }

        if(nextx >= 0 && nexty >= 0
                && nextx < walls.length && nexty < walls[nextx].length) {
            return !(walls[nextx][nexty] || breakableWalls[nextx][nexty]);
        } else {
            return false;
        }
    }
}

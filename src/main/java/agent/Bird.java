package agent;

import model.BombermanGame;
import utils.AgentAction;
import utils.ColorAgent;
import utils.InfoAgent;

import java.util.Random;

public class Bird extends Character{
    private int radius;

    public Bird(int x, int y) {
        super(new InfoAgent(x, y,
                AgentAction.STOP,
                'V',
                ColorAgent.values()[new Random().nextInt(ColorAgent.values().length)],
                false,
                false));
        this.radius = 3;
        getInfo().setActive(false);
    }

    public boolean isBombermanInRange(BombermanGame game){
        InfoAgent birdInfo = super.getInfo();
        for (Character agent : game.getCharacterMap().get('B')) {
            InfoAgent agentInfo = agent.getInfo();
            if (Math.sqrt(
                    Math.pow(birdInfo.getX() - agentInfo.getX(), 2) + Math.pow(birdInfo.getY() - agentInfo.getY(), 2)) <= this.radius) {
                getInfo().setActive(true);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isLegalMove(int xdir, int ydir, BombermanGame game) {
        if(!isBombermanInRange(game)) {
            return false;
        }

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

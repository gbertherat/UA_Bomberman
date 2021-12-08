package agent;

import model.BombermanGame;
import utils.AgentAction;
import utils.ColorAgent;
import utils.InfoAgent;

import java.util.Random;

public class Rajion extends Character{

    public Rajion(int x, int y, BombermanGame game, boolean isAI) {
        super(new InfoAgent(x, y,
                AgentAction.STOP,
                'E',
                ColorAgent.values()[new Random().nextInt(ColorAgent.values().length)],
                false, true, false, false),
                game, isAI);
    }
}

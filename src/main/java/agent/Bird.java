package agent;

import model.BombermanGame;
import utils.AgentAction;
import utils.ColorAgent;
import utils.InfoAgent;

import java.util.Random;

public class Bird extends Character{

    public Bird(int x, int y, BombermanGame game, boolean isAI) {
        super(new InfoAgent(x, y,
                AgentAction.STOP,
                'V',
                ColorAgent.values()[new Random().nextInt(ColorAgent.values().length)],
                true, false, false, false),
                game, isAI);
    }
}

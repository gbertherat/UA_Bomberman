package agent;

import model.BombermanGame;
import utils.AgentAction;
import utils.ColorAgent;
import utils.InfoAgent;

import java.util.Random;

public class Bird extends Character{

    public Bird(int id, int x, int y, ColorAgent color, BombermanGame game, boolean isAI) {
        super(new InfoAgent(id, x, y,
                AgentAction.STOP,
                'V',
                color,
                true, false, false, false),
                game, isAI);
    }
}

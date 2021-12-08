package agent;

import model.BombermanGame;
import utils.AgentAction;
import utils.ColorAgent;
import utils.InfoAgent;

import java.util.Random;

public class Bomberman extends Character {

    public Bomberman(int x, int y, BombermanGame game, boolean isAI) {
        super(new InfoAgent(x, y,
                AgentAction.STOP,
                'B',
                ColorAgent.values()[new Random().nextInt(ColorAgent.values().length)],
                false, true, false, false),
                game, isAI);
    }
}

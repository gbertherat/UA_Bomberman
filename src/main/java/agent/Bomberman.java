package agent;

import model.BombermanGame;
import utils.AgentAction;
import utils.ColorAgent;
import utils.InfoAgent;

import java.util.Random;

public class Bomberman extends Character {

    public Bomberman(int id, int x, int y, ColorAgent color, BombermanGame game, boolean isAI) {
        super(new InfoAgent(id, x, y,
                AgentAction.STOP,
                'B',
                color,
                false, true, false, false),
                game, isAI);
    }
}

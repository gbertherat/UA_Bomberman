package agent;

import model.BombermanGame;
import utils.AgentAction;
import utils.ColorAgent;
import utils.InfoAgent;

public class Enemy extends Character{
    public Enemy(int id, int x, int y, ColorAgent color, BombermanGame game, boolean isAI) {
        super(new InfoAgent(id, x, y,
                AgentAction.STOP,
                'R',
                color,
                false,
                true,
                false,
                false),
                game, isAI);
    }
}

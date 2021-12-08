package agent;

import model.BombermanGame;
import utils.AgentAction;
import utils.ColorAgent;
import utils.InfoAgent;

public class Enemy extends Character{
    public Enemy(int x, int y, BombermanGame game, boolean isAI) {
        super(new InfoAgent(x, y,
                AgentAction.STOP,
                'R',
                ColorAgent.DEFAULT,
                false,
                true,
                false,
                false),
                game, isAI);
    }
}

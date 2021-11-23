package agent;

import model.BombermanGame;
import utils.AgentAction;
import utils.ColorAgent;
import utils.InfoAgent;

public class Enemy extends Character{
    public Enemy(int x, int y, BombermanGame game) {
        super(new InfoAgent(x, y,
                AgentAction.STOP,
                'R',
                ColorAgent.DEFAULT,
                false,
                false),

                game);
    }
}

package agent;

import utils.AgentAction;
import utils.ColorAgent;
import utils.InfoAgent;

public class Enemy extends Character{
    public Enemy(int x, int y) {
        super(new InfoAgent(x, y,
                AgentAction.STOP,
                'R',
                ColorAgent.DEFAULT,
                false,
                false));
    }
}

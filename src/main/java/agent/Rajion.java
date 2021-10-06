package agent;

import utils.AgentAction;
import utils.ColorAgent;
import utils.InfoAgent;

import java.util.Random;

public class Rajion extends Character{
    public Rajion(int x, int y) {
        super(new InfoAgent(x, y,
                AgentAction.STOP,
                'B',
                ColorAgent.values()[new Random().nextInt(ColorAgent.values().length)],
                false,
                false));
    }
}

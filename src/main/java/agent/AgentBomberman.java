package agent;

import utils.AgentAction;
import utils.ColorAgent;
import utils.InfoAgent;

import java.util.Random;

public class AgentBomberman extends Character {
    public AgentBomberman(int x, int y) {
        super(new InfoAgent(x, y,
                AgentAction.STOP,
                'B',
                ColorAgent.values()[new Random().nextInt(ColorAgent.values().length)],
                false,
                false));
    }
}

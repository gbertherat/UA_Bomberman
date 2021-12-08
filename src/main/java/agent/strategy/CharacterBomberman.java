package agent.strategy;

import model.BombermanGame;
import utils.AgentAction;

public class CharacterBomberman implements CharacterStrategy{
    private final BombermanGame game;

    public CharacterBomberman(BombermanGame game) {
        this.game = game;
    }

    @Override
    public AgentAction selectAction() {
        return game.getBombermanAction();
    }
}

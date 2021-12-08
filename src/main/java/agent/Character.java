package agent;

import agent.strategy.CharacterAI;
import agent.strategy.CharacterBomberman;
import agent.strategy.CharacterStrategy;
import model.BombermanGame;
import utils.*;

// Classe mère définissant les méthodes de bases pour tous les agents.
public abstract class Character {
    private final InfoAgent info;
    private final BombermanGame game;
    private final boolean isAI;
    private final CharacterStrategy strategy;

    public Character(InfoAgent info, BombermanGame game, boolean isAI){
        this.info = info;
        this.game = game;
        this.isAI = isAI;
        if(this.isAI){
            this.strategy = new CharacterAI(game, info);
        } else {
            this.strategy = new CharacterBomberman(game);
        }
    }

    public BombermanGame getGame() {
        return game;
    }

    public InfoAgent getInfo() {
        return this.info;
    }

    public void changeType(char type){
        info.setType(type);
    }

    public int getX(){
        return info.getX();
    }

    public int getY(){
        return info.getY();
    }

    public boolean isAI() {
        return isAI;
    }

    public void move(AgentAction action) {
        int xdir = 0;
        int ydir = 0;
        switch (action) {
            case MOVE_UP:
                ydir = -1;
                break;
            case MOVE_DOWN:
                ydir = 1;
                break;
            case MOVE_LEFT:
                xdir = -1;
                break;
            case MOVE_RIGHT:
                xdir = 1;
                break;
        }

        if (game.isLegalMove(getInfo(), xdir, ydir)) {
            this.info.setX(info.getX() + xdir);
            this.info.setY(info.getY() + ydir);
        } else {
            this.info.setAgentAction(AgentAction.STOP);
        }
    }

    public InfoBomb putBomb(){
        return new InfoBomb(info.getX(), info.getY(), info.getBombRange(), StateBomb.Step0);
    }

    public void selectAction(){
        if(getInfo().getTurnUntilNotInvincible() > 0){
            getInfo().decreaseTurnUntilNotInvincible(1);
        }

        if(getInfo().getTurnUntilNotSick() > 0){
            getInfo().decreaseTurnUntilNotSick(1);
        }

        AgentAction action = strategy.selectAction();
        getInfo().setAgentAction(action);

        if (action == AgentAction.PUT_BOMB && getInfo().isActive() && !getInfo().isSick()) {
            if (game.getBombList().stream().noneMatch(e -> e.getX() == getX()
                    && e.getY() == getY())) {
                game.getBombList().add(putBomb());
            }
        } else {
            move(action);
        }
    }
}

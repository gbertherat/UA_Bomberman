package agent;

import model.BombermanGame;
import utils.*;

import java.util.ArrayList;
import java.util.Random;

public abstract class Character {
    private InfoAgent info;

    public Character(InfoAgent info){
        this.info = info;
    }

    public InfoAgent getInfo() {
        return this.info;
    }

    public void changeType(char type){
        info.setType(type);
    }

    public boolean isLegalMove(int xdir, int ydir, BombermanGame game) {
        boolean[][] walls = game.getMap().get_walls();
        boolean[][] breakableWalls = game.getBreakableWalls();
        ArrayList<InfoBomb> bombList = game.getBombList();

        int nextx = info.getX() + xdir;
        int nexty = info.getY() + ydir;

        for(InfoBomb bomb : bombList){
            if(bomb.getX() == nextx && bomb.getY() == nexty){
                return false;
            }
        }

        for(char c : game.getCharacterMap().keySet()){
            for(Character agent : game.getCharacterMap().get(c)){
                InfoAgent info = agent.getInfo();
                if(info.getX() == nextx && info.getY() == nexty){
                    return false;
                }
            }
        }

        if(nextx >= 0 && nexty >= 0
                && nextx < walls.length && nexty < walls[nextx].length) {
            return !(walls[nextx][nexty] || breakableWalls[nextx][nexty]);
        } else {
            return false;
        }
    }

    public void move(AgentAction action, BombermanGame game) {
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

        if (isLegalMove(xdir, ydir, game)) {
            this.info.setX(info.getX() + xdir);
            this.info.setY(info.getY() + ydir);
        } else {
            this.info.setAgentAction(AgentAction.STOP);
        }
    }

    public InfoBomb putBomb(){
        return new InfoBomb(info.getX(), info.getY(), info.getBombRange(), StateBomb.Step0);
    }

    public void selectAction(BombermanGame game){
        if(getInfo().getTurnUntilNotInvincible() > 0){
            getInfo().decreaseTurnUntilNotInvincible(1);
        } else {
            getInfo().setInvincible(false);
        }

        if(getInfo().getTurnUntilNotSick() > 0){
            getInfo().decreaseTurnUntilNotSick(1);
        } else {
            getInfo().setSick(false);
        }

        Random random = new Random();

        AgentAction randomAction = AgentAction.values()[random.nextInt(AgentAction.values().length)];
        getInfo().setAgentAction(randomAction);
        if (randomAction == AgentAction.PUT_BOMB && getInfo().isActive()) {
            if (game.getBombList().stream().noneMatch(e -> e.getX() == getInfo().getX()
                    && e.getY() == getInfo().getY())) {
                game.getBombList().add(putBomb());
            }
        } else {
            move(randomAction, game);
        }
    }
}

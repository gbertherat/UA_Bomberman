package agent;

import model.BombermanGame;
import model.Game;
import sun.management.Agent;
import utils.*;

import java.util.*;

public abstract class Character {
    private InfoAgent info;
    private BombermanGame game;

    public Character(InfoAgent info, BombermanGame game){
        this.info = info;
        this.game = game;
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

    public boolean isLegalMove(int xdir, int ydir) {
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

        if (isLegalMove(xdir, ydir)) {
            this.info.setX(info.getX() + xdir);
            this.info.setY(info.getY() + ydir);
        } else {
            this.info.setAgentAction(AgentAction.STOP);
        }
    }

    public InfoBomb putBomb(){
        return new InfoBomb(info.getX(), info.getY(), info.getBombRange(), StateBomb.Step0);
    }

    public double getDistOfNearestBomb(){
        double minDist = 999;
        for(InfoBomb bomb : game.getBombList()){
            double dist = Math.sqrt(Math.pow(bomb.getX() - this.getX(), 2) + Math.pow(bomb.getY() - this.getY(), 2));
            if(dist < minDist){
                minDist = dist;
            }
        }
        return minDist;
    }

    public InfoBomb getNearestBomb(){
        Optional<InfoBomb> bomb = game.getBombList().stream().filter(e -> Math.sqrt(Math.pow(e.getX() - this.getX(), 2) + Math.pow(e.getY() - this.getY(), 2)) == getDistOfNearestBomb()).findAny();
        return bomb.orElse(null);
    }

    public void checkSurrounding(List<AgentAction> possibilities){
        if(!isLegalMove(0,-1)){
            possibilities.remove(AgentAction.MOVE_UP);
        }
        if(!isLegalMove(0,1)){
            possibilities.remove(AgentAction.MOVE_DOWN);
        }
        if(!isLegalMove(-1,0)){
            possibilities.remove(AgentAction.MOVE_LEFT);
        }
        if(!isLegalMove(1,0)){
            possibilities.remove(AgentAction.MOVE_RIGHT);
        }
    }

    public AgentAction selectSmartAction(){
        Map<AgentAction, Integer> weightedPossibilities = new HashMap<>();
        Arrays.stream(AgentAction.values()).forEach(e -> weightedPossibilities.put(e, 0));

        System.out.println(weightedPossibilities);
        return AgentAction.STOP;
    }

    public void selectAction(){
        if(getInfo().getTurnUntilNotInvincible() > 0){
            getInfo().decreaseTurnUntilNotInvincible(1);
        }

        if(getInfo().getTurnUntilNotSick() > 0){
            getInfo().decreaseTurnUntilNotSick(1);
        }

        AgentAction action = selectSmartAction();
        getInfo().setAgentAction(action);
        if (action == AgentAction.PUT_BOMB && getInfo().isActive() && !getInfo().isSick()) {
            if (game.getBombList().stream().noneMatch(e -> e.getX() == getX()
                    && e.getY() == getY())) {
                game.getBombList().add(putBomb());
            }
        } else {
            move(action, game);
        }
    }
}

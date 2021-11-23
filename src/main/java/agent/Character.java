package agent;

import model.BombermanGame;
import model.Game;
import sun.management.Agent;
import utils.*;

import java.util.*;
import java.util.stream.Collectors;

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

    public void checkSurrounding(Map<AgentAction, Integer> possibilities){
        if(!isLegalMove(0,-1)){
            possibilities.put(AgentAction.MOVE_UP, possibilities.get(AgentAction.MOVE_UP)-9999);
        }
        if(!isLegalMove(0,1)){
            possibilities.put(AgentAction.MOVE_DOWN, possibilities.get(AgentAction.MOVE_DOWN)-9999);
        }
        if(!isLegalMove(-1,0)){
            possibilities.put(AgentAction.MOVE_LEFT, possibilities.get(AgentAction.MOVE_LEFT)-9999);
        }
        if(!isLegalMove(1,0)){
            possibilities.put(AgentAction.MOVE_RIGHT, possibilities.get(AgentAction.MOVE_RIGHT)-9999);
        }
        if(game.getBombList().stream().anyMatch(e -> e.getX() == this.getX() && e.getY() == this.getY())){
            possibilities.put(AgentAction.PUT_BOMB, possibilities.get(AgentAction.PUT_BOMB)-9999);
        }
    }

    public ArrayList<InfoBomb> getNearestBombs(){
        return (ArrayList<InfoBomb>) game.getBombList().stream().filter(e -> Math.sqrt(Math.pow(e.getX() - this.getX(), 2) + Math.pow(e.getY() - this.getY(), 2)) <= 5).collect(Collectors.toList());
    }

    public void runAwayFromBomb(Map<AgentAction, Integer> possibilities){
        if(!getInfo().isInvincible() && possibilities.size() > 0) {
            for(InfoBomb bomb : getNearestBombs()){
                possibilities.put(AgentAction.STOP, possibilities.get(AgentAction.STOP)-1000);
                possibilities.put(AgentAction.PUT_BOMB, possibilities.get(AgentAction.PUT_BOMB)-100);
                if(bomb.getX() == this.getX()){
                    if(bomb.getY() > this.getY()){
                        possibilities.put(AgentAction.MOVE_UP, possibilities.get(AgentAction.MOVE_UP)+50);
                    } else if(bomb.getY() < this.getY()){
                        possibilities.put(AgentAction.MOVE_DOWN, possibilities.get(AgentAction.MOVE_DOWN)+50);
                    }
                    possibilities.put(AgentAction.MOVE_RIGHT, possibilities.get(AgentAction.MOVE_RIGHT)+100);
                    possibilities.put(AgentAction.MOVE_LEFT, possibilities.get(AgentAction.MOVE_LEFT)+100);
                }
                if(bomb.getY() == this.getY()){
                    if(bomb.getX() > this.getX()){
                        possibilities.put(AgentAction.MOVE_LEFT, possibilities.get(AgentAction.MOVE_LEFT)+50);
                    }
                    else if(bomb.getX() < this.getX()){
                        possibilities.put(AgentAction.MOVE_RIGHT, possibilities.get(AgentAction.MOVE_RIGHT)+50);
                    }
                    possibilities.put(AgentAction.MOVE_UP, possibilities.get(AgentAction.MOVE_UP)+100);
                    possibilities.put(AgentAction.MOVE_DOWN, possibilities.get(AgentAction.MOVE_DOWN)+100);
                }
                if(bomb.getX() > this.getX()){
                    possibilities.put(AgentAction.MOVE_LEFT, possibilities.get(AgentAction.MOVE_LEFT)+50);
                }
                else if(bomb.getX() < this.getX()){
                    possibilities.put(AgentAction.MOVE_RIGHT, possibilities.get(AgentAction.MOVE_RIGHT)+50);
                }
                if(bomb.getY() > this.getY()){
                    possibilities.put(AgentAction.MOVE_UP, possibilities.get(AgentAction.MOVE_UP)+50);
                } else if(bomb.getY() < this.getY()){
                    possibilities.put(AgentAction.MOVE_DOWN, possibilities.get(AgentAction.MOVE_DOWN)+50);
                }
            }
        }
    }

    public AgentAction selectSmartAction() {
        Map<AgentAction, Integer> weightedPossibilities = new HashMap<>();
        Arrays.stream(AgentAction.values()).forEach(e -> weightedPossibilities.put(e, 0));

        checkSurrounding(weightedPossibilities);
        runAwayFromBomb(weightedPossibilities);

        int maxWeight = -9999;
        List<AgentAction> smartActionList = new ArrayList<>();
        for (AgentAction action : weightedPossibilities.keySet()) {
            if (weightedPossibilities.get(action) > maxWeight) {
                smartActionList = new ArrayList<>();
                smartActionList.add(action);
                maxWeight = weightedPossibilities.get(action);
            } else if (weightedPossibilities.get(action) == maxWeight) {
                smartActionList.add(action);
            }
        }
        Random random = new Random();
        return smartActionList.get(random.nextInt(smartActionList.size()));
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

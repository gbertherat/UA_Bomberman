package agent;

import model.BombermanGame;
import utils.*;

import java.util.ArrayList;
import java.util.Optional;
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

    public int getX(){
        return info.getX();
    }

    public int getY(){
        return info.getY();
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

    public AgentAction placeLotOfBombsIfInvincible(BombermanGame game){
        if(!this.getInfo().isInvincible() || this.getInfo().getTurnUntilNotInvincible() < 3){
            return null;
        }
        AgentAction action = game.getBombList().stream().noneMatch(e -> e.getX() == this.getX() && e.getY() == this.getY()) ? AgentAction.PUT_BOMB : null;

        if(action == null) {
            if (!game.hasWallAtCoords(this.getX(), this.getY() + 1)) {
                return AgentAction.MOVE_DOWN;
            } else if (!game.hasWallAtCoords(this.getX(), this.getY() - 1)) {
                return AgentAction.MOVE_UP;
            } else if (!game.hasWallAtCoords(this.getX() + 1, this.getY())) {
                return AgentAction.MOVE_RIGHT;
            } else {
                return AgentAction.MOVE_LEFT;
            }
        }
        return null;
    }

    public AgentAction runAwayFromBombs(BombermanGame game){
        if(this.getInfo().isInvincible()){
            return null;
        }

        if(game.getBombList().size() == 0){
            return null;
        }
        ArrayList<AgentAction> possibilities = new ArrayList<>();
        ArrayList<Integer> blockedX = new ArrayList<>();
        ArrayList<Integer> blockedY = new ArrayList<>();
        game.getBombList().forEach(e -> blockedX.add(e.getX()));
        game.getBombList().forEach(e -> blockedY.add(e.getY()));

        if(!blockedX.contains(this.getX()+1) && !blockedY.contains(this.getY()) && !game.hasWallAtCoords(this.getX()+1, this.getY())){
            possibilities.add(AgentAction.MOVE_RIGHT);
        }
        if(!blockedX.contains(this.getX()-1) && !blockedY.contains(this.getY()) && !game.hasWallAtCoords(this.getX()-1, this.getY())){
            possibilities.add(AgentAction.MOVE_LEFT);
        }
        if(!blockedX.contains(this.getX()) && !blockedY.contains(this.getY()+1) && !game.hasWallAtCoords(this.getX(), this.getY()+1)){
            possibilities.add(AgentAction.MOVE_DOWN);
        }
        if(!blockedX.contains(this.getX()) && !blockedY.contains(this.getY()-1) && !game.hasWallAtCoords(this.getX(), this.getY()-1)){
            possibilities.add(AgentAction.MOVE_UP);
        }
        if(blockedX.contains(this.getX())){
            Optional<InfoBomb> bomb = game.getBombList().stream().filter(b -> b.getX() == this.getX()).findFirst();
            if(bomb.isPresent()){
                if(bomb.get().getY() >= this.getY() && !game.hasWallAtCoords(this.getX(), this.getY()-1)) {
                    possibilities.add(AgentAction.MOVE_UP);
                }
                if(bomb.get().getY() <= this.getY() && !game.hasWallAtCoords(this.getX(), this.getY()+1)){
                    possibilities.add(AgentAction.MOVE_DOWN);
                }
            }
        }
        if(blockedY.contains(this.getY())){
            Optional<InfoBomb> bomb = game.getBombList().stream().filter(b -> b.getY() == this.getY()).findFirst();
            if(bomb.isPresent()){
                if(bomb.get().getX() >= this.getX() && !game.hasWallAtCoords(this.getX()-1, this.getY())) {
                    possibilities.add(AgentAction.MOVE_LEFT);
                }
                if(bomb.get().getX() <= this.getX() && !game.hasWallAtCoords(this.getX()+1, this.getY())){
                    possibilities.add(AgentAction.MOVE_RIGHT);
                }
            }
        }
        Random random = new Random();
        return possibilities.size() > 0 ? possibilities.get(random.nextInt(possibilities.size())) : null;
    }

    public AgentAction lookForGoodItemsInRange(BombermanGame game){
        for(InfoItem item : game.getItemList()){
            if(item.getType().isGood()) {
                if (Math.sqrt(
                        Math.pow(item.getX() - this.getX(), 2) + Math.pow(item.getY() - this.getY(), 2)) <= 5) {
                    if (item.getY() > this.getY() && !game.hasWallAtCoords(this.getX(), this.getY() + 1)) {
                        return AgentAction.MOVE_DOWN;
                    } else if (item.getY() < this.getY() && !game.hasWallAtCoords(this.getX(), this.getY() - 1)) {
                        return AgentAction.MOVE_UP;
                    } else if (item.getX() > this.getX() && !game.hasWallAtCoords(this.getX() + 1, this.getY())) {
                        return AgentAction.MOVE_RIGHT;
                    } else if (item.getX() < this.getX() && !game.hasWallAtCoords(this.getX() - 1, this.getY())) {
                        return AgentAction.MOVE_LEFT;
                    }
                }
            }
        }
        return null;
    }

    public AgentAction selectSmartAction(BombermanGame game){
        AgentAction action = placeLotOfBombsIfInvincible(game);
        if(action != null){
            return action;
        }

        action = runAwayFromBombs(game);
        if(action != null){
            return action;
        }

        action = lookForGoodItemsInRange(game);
        if(action != null){
            return action;
        }

        Random random = new Random();
        action = AgentAction.values()[random.nextInt(AgentAction.values().length)];
        return action;
    }

    public void selectAction(BombermanGame game){
        if(getInfo().getTurnUntilNotInvincible() > 0){
            getInfo().decreaseTurnUntilNotInvincible(1);
        }

        if(getInfo().getTurnUntilNotSick() > 0){
            getInfo().decreaseTurnUntilNotSick(1);
        }

        AgentAction action = selectSmartAction(game);
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

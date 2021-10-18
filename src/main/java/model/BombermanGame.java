package model;

import agent.*;
import agent.Character;
import utils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class BombermanGame extends Game {
    private InputMap map;
    private ArrayList<Character> characterList;
    private ArrayList<InfoBomb> bombList;
    private ArrayList<InfoItem> itemList;
    private boolean[][] breakableWalls;

    public BombermanGame(int maxTurn, int timeMs, InputMap map){
        super(maxTurn, timeMs);
        this.map = map;
        this.characterList = new ArrayList<>();
        this.bombList = new ArrayList<>();
        this.itemList = new ArrayList<>();
        this.breakableWalls = map.getStart_breakable_walls();
    }

    public InputMap getMap() {
        return map;
    }

    public ArrayList<Character> getCharacterList() {
        return characterList;
    }

    public ArrayList<InfoBomb> getBombList() {
        return bombList;
    }

    public ArrayList<InfoItem> getItemList() {
        return itemList;
    }

    public boolean[][] getBreakableWalls() {
        return breakableWalls;
    }

    @Override
    public void init() {
        super.init();
        for(InfoAgent agent: map.getStart_agents()){
            switch(agent.getType()){
                case 'B':
                    characterList.add(new AgentBomberman(agent.getX(), agent.getY()));
                    break;
                case 'V':
                    characterList.add(new Bird(agent.getX(), agent.getY()));
                    break;
                case 'R':
                    characterList.add(new Rajion(agent.getX(), agent.getY()));
                    break;
                case 'E':
                    characterList.add(new Enemy(agent.getX(), agent.getY()));
                    break;
            }
        }
    }

    @Override
    public void restart() {
        setTurn(0);
        this.characterList = new ArrayList<>();
        this.bombList = new ArrayList<>();
        this.itemList = new ArrayList<>();
        this.breakableWalls = map.getStart_breakable_walls();
        init();
        setChanged();
        notifyObservers();
    }

    public void checkWallsInBombRange(int x, int y, int range){
        for(int i = -range; i < range+1; i++){
            breakableWalls[x+i][y] = false;
            breakableWalls[x][y+i] = false;
        }
    }

    public void checkCharactersInBombRange(int x, int y, int range){
        ArrayList<Character> newCharacterList = new ArrayList<>();
        System.out.println(characterList.size());
        for(Character agent : characterList){
            InfoAgent infoAgent = agent.getInfo();
            boolean isAlive = true;
            int i = -range;
            while(i < range+1 && isAlive){
                if ((infoAgent.getX() == x + i && infoAgent.getY() == y)
                        || infoAgent.getX() == x && infoAgent.getY() == y + i) {
                    isAlive = false;
                }
                i++;
            }
            if(isAlive && !newCharacterList.contains(agent)){
                newCharacterList.add(agent);
            }
        }
        characterList = newCharacterList;
    }

    public void checkBombs(){
        ArrayList<InfoBomb> newBombList = new ArrayList<>();
        for(InfoBomb bomb : bombList){
            if(bomb.getStateBomb() == StateBomb.Boom){
                continue;
            }
            StateBomb bombState = bomb.getStateBomb();
            bomb.setStateBomb(bombState.next());
            if(bombState.next() == StateBomb.Boom){
                checkWallsInBombRange(bomb.getX(), bomb.getY(), bomb.getRange());
                checkCharactersInBombRange(bomb.getX(), bomb.getY(), bomb.getRange());
            }
            newBombList.add(bomb);
        }
        bombList = newBombList;
    }

    @Override
    public void takeTurn() {
        Random random = new Random();
        checkBombs();

        for(Character character : characterList){
            AgentAction randomAction = AgentAction.values()[random.nextInt(AgentAction.values().length)];
            character.getInfo().setAgentAction(randomAction);
            if (randomAction == AgentAction.PUT_BOMB) {
                if(bombList.stream().noneMatch(e -> e.getX() == character.getInfo().getX()
                                                && e.getY() == character.getInfo().getY())){
                    bombList.add(character.putBomb());
                }
            } else {
                character.move(randomAction, this);
            }

        }
        setChanged();
        notifyObservers();
    }

    @Override
    public boolean gameContinue() {
        return getTurn() < getMaxturn();
    }

    @Override
    public void gameOver() {

    }
}

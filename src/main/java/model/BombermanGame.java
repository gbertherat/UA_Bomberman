package model;

import agent.*;
import agent.Character;
import utils.AgentAction;
import utils.InfoAgent;
import utils.InfoBomb;
import utils.InfoItem;

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

    @Override
    public void takeTurn() {
        Random random = new Random();
        for(Character character : characterList){
            AgentAction randomAction = AgentAction.values()[random.nextInt(AgentAction.values().length)];
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

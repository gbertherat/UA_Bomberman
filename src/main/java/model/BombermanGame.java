package model;

import agent.*;
import agent.Character;
import utils.*;

import java.util.*;

public class BombermanGame extends Game {
    private InputMap map;
    private HashMap<java.lang.Character, ArrayList<Character>> characterMap;
    private ArrayList<InfoBomb> bombList;
    private ArrayList<InfoItem> itemList;
    private boolean[][] breakableWalls;

    public BombermanGame(int maxTurn, int timeMs, InputMap map){
        super(maxTurn, timeMs);
        this.map = map;
        this.characterMap = new HashMap<>();
        for(AgentType type: AgentType.values()){
            this.characterMap.put(type.getChar(), new ArrayList<>());
        }
        this.bombList = new ArrayList<>();
        this.itemList = new ArrayList<>();
        this.breakableWalls = Arrays.stream(map.getStart_breakable_walls()).map(boolean[]::clone).toArray($ -> map.getStart_breakable_walls().clone());
    }

    public InputMap getMap() {
        return map;
    }

    public HashMap<java.lang.Character, ArrayList<Character>> getCharacterMap() {
        return characterMap;
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
                    characterMap.get(agent.getType()).add(0, new Bomberman(agent.getX(), agent.getY()));
                    break;
                case 'V':
                    characterMap.get(agent.getType()).add(new Bird(agent.getX(), agent.getY()));
                    break;
                case 'E':
                    characterMap.get(agent.getType()).add(new Rajion(agent.getX(), agent.getY()));
                    break;
                case 'R':
                    characterMap.get(agent.getType()).add(new Enemy(agent.getX(), agent.getY()));
                    break;
            }
        }
    }

    @Override
    public void restart() {
        super.restart();
        this.characterMap = new HashMap<>();
        for(AgentType type: AgentType.values()){
            this.characterMap.put(type.getChar(), new ArrayList<>());
        }
        this.bombList = new ArrayList<>();
        this.itemList = new ArrayList<>();
        this.breakableWalls = Arrays.stream(map.getStart_breakable_walls()).map(boolean[]::clone).toArray($ -> map.getStart_breakable_walls().clone());
        init();
        setGameOverReason("");
        setChanged();
        notifyObservers(this);
    }

    public void addRandomItem(int x, int y) {
        Random random = new Random();

        if(random.nextInt(3) == 1) {
            ItemType randomItem = ItemType.values()[random.nextInt(ItemType.values().length)];
            itemList.add(new InfoItem(x, y, randomItem));
        }
    }

    public void checkWallsInBombRange(int x, int y, int range){
        for(int i = -range; i < range+1; i++){
            if(x+i > 0 && x+i < breakableWalls.length && y > 0 && y < breakableWalls[x+i].length && breakableWalls[x+i][y]) {
                breakableWalls[x + i][y] = false;
                addRandomItem(x + i, y);
            }
            if(y+i > 0 && y+i < breakableWalls[x].length && x > 0 && breakableWalls[x][y + i]){
                breakableWalls[x][y + i] = false;
                addRandomItem(x, y + i);
            }
        }
    }

    public void checkCharactersInBombRange(int x, int y, int range){
        for(char c : characterMap.keySet()){
            for(Character agent: characterMap.get(c)){
                InfoAgent infoAgent = agent.getInfo();
                int i = -range;
                while(i < range+1 && infoAgent.isAlive()){
                    if ((infoAgent.getX() == x + i && infoAgent.getY() == y)
                            || infoAgent.getX() == x && infoAgent.getY() == y + i) {
                        infoAgent.setAlive(false);
                    }
                    i++;
                }
            }
        }
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

    public void checkRajionOnBomberman(){
        ArrayList<Character> rajionList = characterMap.get('E');
        ArrayList<Character> bombermanList = characterMap.get('B');

        for(Character rajion : rajionList){
            for(Character bomberman : bombermanList){
                if(rajion.getInfo().getX() == bomberman.getInfo().getX() && rajion.getInfo().getY() == bomberman.getInfo().getY()){
                    bomberman.getInfo().setAlive(false);
                }
            }
        }
    }

    public void removeDeadCharacters(){
        for(char c : characterMap.keySet()){
            characterMap.get(c).removeIf(e -> !e.getInfo().isAlive());
        }
    }

    public void checkCharactersOnItems(){
        for(char c : characterMap.keySet()){
            for(Character character: characterMap.get(c)){
                ListIterator<InfoItem> ite = itemList.listIterator();
                while(ite.hasNext()){
                    InfoItem itemInfo = ite.next();
                    if(itemInfo.getX() == character.getInfo().getX() && itemInfo.getY() == character.getInfo().getY()){
                        itemInfo.getType().applyItem(character);
                        ite.remove();
                    }
                }
            }
        }
    }

    public void checkIfGameOver(){
        ArrayList<Character> characters = new ArrayList<>();
        characterMap.keySet().forEach(key -> characters.addAll(characterMap.get(key)));

        if(characterMap.get('B').stream().noneMatch(c -> c.getInfo().isAlive())){
            gameOver("You died!");
        }
        if(characters.size() == 1){
            if(characters.get(0).getInfo().getType() == 'B'){
                gameOver("You won!");
            }
        }
    }

    @Override
    public void takeTurn() {
        checkBombs();
        checkRajionOnBomberman();
        removeDeadCharacters();
        checkCharactersOnItems();
        checkIfGameOver();

        for(char c : characterMap.keySet()) {
            for (Character character : characterMap.get(c)) {
                character.selectAction(this);
            }
        }
    }

    @Override
    public boolean gameContinue() {
        return (getTurn() < getMaxturn()) && !isFinished();
    }

    @Override
    public void gameOver(String reason) {
        setGameOverReason(reason);
        setFinished(true);
        setChanged();
        notifyObservers(this);
    }
}

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
    private AgentAction bombermanAction;
    private int difficulty;
    private boolean started;

    public BombermanGame(int maxTurn, int timeMs, InputMap map){
        super(maxTurn, timeMs);
        this.map = map;
        this.bombermanAction = AgentAction.STOP;
        this.difficulty = 3;
        this.started = false;
    }

    public InputMap getMap() {
        return map;
    }

    public void changeMap(InputMap map){
        this.map = map;
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

    public void setBombermanAction(AgentAction action){
        bombermanAction = action;
    }

    public AgentAction getBombermanAction(){
        return bombermanAction;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    @Override
    public void init() {
        super.init();
        this.characterMap = new HashMap<>();
        this.bombList = new ArrayList<>();
        this.itemList = new ArrayList<>();
        this.breakableWalls = Arrays.stream(map.getStart_breakable_walls()).map(boolean[]::clone).toArray($ -> map.getStart_breakable_walls().clone());
        for(AgentType type: AgentType.values()){
            this.characterMap.put(type.getChar(), new ArrayList<>());
        }
    }

    public void addAgent(InfoAgent agent, int id, boolean isAI){
        getMap().addStart_agent(agent);
        switch(agent.getType()){
            case 'B':
                characterMap.get(agent.getType()).add(new Bomberman(id, agent.getX(), agent.getY(), this, isAI));
                break;
            case 'V':
                characterMap.get(agent.getType()).add(new Bird(id, agent.getX(), agent.getY(), this, isAI));
                break;
            case 'E':
                characterMap.get(agent.getType()).add(new Rajion(id, agent.getX(), agent.getY(), this, isAI));
                break;
            case 'R':
                characterMap.get(agent.getType()).add(new Enemy(id, agent.getX(), agent.getY(), this, isAI));
                break;
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

        if(random.nextInt(2) == 1) {
            ItemType randomItem = ItemType.values()[random.nextInt(ItemType.values().length)];
            itemList.add(new InfoItem(x, y, randomItem));
        }
    }

    public boolean hasWallAtCoords(int x, int y){
        if(x < 0 || y < 0 || x > getBreakableWalls().length || y > getBreakableWalls()[x].length){
            return false;
        }
        return getBreakableWalls()[x][y] || map.get_walls()[x][y];
    }

    public boolean hasCharacterAtCoords(int x, int y){
        if(characterMap != null) {
            for (char c : characterMap.keySet()) {
                if (characterMap.get(c).stream().anyMatch(e -> e.getX() == x && e.getY() == y)) {
                    return true;
                }
            }
        }
        return false;
    }

    // On active les agents Bird qui sont proche d'un Bomberman.
    public void checkBirdsNearBomberman(){
        ArrayList<Character> birds = characterMap.get('V');
        ArrayList<Character> bomberman = characterMap.get('B');
        for(Character bird: birds){
            InfoAgent birdInfo = bird.getInfo();
            if(bomberman.stream().anyMatch(e -> Math.sqrt(Math.pow(birdInfo.getX() - e.getInfo().getX(), 2) + Math.pow(birdInfo.getY() - e.getInfo().getY(), 2)) < 5)){
                bird.getInfo().setActive(true);
            }
        }
    }

    // Si des murs cassables sont dans la range d'une bombe qui explose, ils sont détruits
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

    // Si des agents sont dans la range d'une bombe qui explose, ils sont éliminés
    public void checkCharactersInBombRange(int x, int y, int range){
        for(char c : characterMap.keySet()){
            for(Character agent: characterMap.get(c)){
                InfoAgent infoAgent = agent.getInfo();
                int i = -range;
                while(i < range+1 && infoAgent.isAlive()){
                    if ((infoAgent.getX() == x + i && infoAgent.getY() == y)
                            || infoAgent.getX() == x && infoAgent.getY() == y + i) {
                        if(!infoAgent.isInvincible()) {
                            infoAgent.setAlive(false);
                        }
                    }
                    i++;
                }
            }
        }
    }

    // On actualise les bombes sur la map
    public void checkBombs(){
        ArrayList<InfoBomb> newBombList = new ArrayList<>();
        for(InfoBomb bomb : bombList){
            if(bomb.getStateBomb() == StateBomb.Exploded){
                continue;
            }
            if(bomb.getStateBomb() == StateBomb.Boom){
                checkWallsInBombRange(bomb.getX(), bomb.getY(), bomb.getRange());
                checkCharactersInBombRange(bomb.getX(), bomb.getY(), bomb.getRange());
            }
            StateBomb bombState = bomb.getStateBomb();
            bomb.setStateBomb(bombState.next());
            newBombList.add(bomb);
        }
        bombList = newBombList;
    }

    // Si le rajion se trouve sur le bomberman, il le tue.
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

    // On supprime les agents morts de la liste des joueurs.
    public void removeDeadCharacters(){
        for(char c : characterMap.keySet()){
            characterMap.get(c).removeIf(e -> !e.getInfo().isAlive());
        }
    }

    // On regarde si un agent se trouve sur un item pour le lui donner.
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

    // Check si le jeu est fini
    // Le jeu est fini si le joueur est mort ou si tous les agents sont morts.
    public void checkIfGameOver(){
        ArrayList<Character> characters = new ArrayList<>();
        characterMap.keySet().forEach(key -> characters.addAll(characterMap.get(key)));

        if(characterMap.get('B').stream().noneMatch(e -> !e.isAI() && e.getInfo().isAlive())){
            gameOver("You died!");
        }
        if(characters.size() == 1){
            if(characters.get(0).getInfo().getType() == 'B'){
                gameOver("You won!");
            }
        }
    }

    // Check si le déplacement vers une direction est possible
    public boolean isLegalMove(InfoAgent info, int xdir, int ydir) {
        if(!info.isActive()) {
            return false;
        }

        boolean[][] walls = getMap().get_walls();
        boolean[][] breakableWalls = getBreakableWalls();
        ArrayList<InfoBomb> bombList = getBombList();

        int nextx = info.getX() + xdir;
        int nexty = info.getY() + ydir;

        if(!info.getCanFly()) {
            for (InfoBomb bomb : bombList) {
                if (bomb.getX() == nextx && bomb.getY() == nexty) {
                    return false;
                }
            }
        }

        for(char c : getCharacterMap().keySet()){
            for(Character agent : getCharacterMap().get(c)){
                InfoAgent i = agent.getInfo();
                if(i.getX() == nextx && i.getY() == nexty){
                    return false;
                }
            }
        }

        if(nextx >= 0 && nexty >= 0
                && nextx < walls.length && nexty < walls[nextx].length) {
            return !(walls[nextx][nexty] || (breakableWalls[nextx][nexty] && !info.getCanFly()));
        } else {
            return false;
        }
    }

    @Override
    public void takeTurn() {
        System.out.println("Take turn");
        // Checklist à chaque début de tours
        checkBirdsNearBomberman();
        checkBombs();
        checkRajionOnBomberman();
        removeDeadCharacters();
        checkCharactersOnItems();
        checkIfGameOver();

        // Chaque agent doivent choisir une action
        for(char c : characterMap.keySet()) {
            for (Character character : characterMap.get(c)) {
                character.selectAction();
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
    }
}

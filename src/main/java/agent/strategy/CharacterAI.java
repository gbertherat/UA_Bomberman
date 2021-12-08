package agent.strategy;

import agent.Character;
import model.BombermanGame;
import pathfinding.AStar;
import pathfinding.Node;
import utils.AgentAction;
import utils.InfoAgent;
import utils.InfoBomb;
import utils.InfoItem;

import java.util.*;
import java.util.stream.Collectors;

public class CharacterAI implements CharacterStrategy{
    private final BombermanGame game;
    private final InfoAgent info;

    public CharacterAI(BombermanGame game, InfoAgent info){
        this.game = game;
        this.info = info;
    }

    // S'il y a un mur à côté de l'agent, il est impossible pour lui d'aller dans cette direction.
    public void checkSurrounding(Map<AgentAction, Integer> possibilities){
        // Sauf si c'est un agent qui peut voler
        if(info.getCanFly()){
            return;
        }

        if(!game.isLegalMove(info, 0,-1)){
            possibilities.put(AgentAction.MOVE_UP, possibilities.get(AgentAction.MOVE_UP)-9999);
        }
        if(!game.isLegalMove(info, 0,1)){
            possibilities.put(AgentAction.MOVE_DOWN, possibilities.get(AgentAction.MOVE_DOWN)-9999);
        }
        if(!game.isLegalMove(info, -1,0)){
            possibilities.put(AgentAction.MOVE_LEFT, possibilities.get(AgentAction.MOVE_LEFT)-9999);
        }
        if(!game.isLegalMove(info, 1,0)){
            possibilities.put(AgentAction.MOVE_RIGHT, possibilities.get(AgentAction.MOVE_RIGHT)-9999);
        }
        if(game.getBombList().stream().anyMatch(e -> e.getX() == info.getX() && e.getY() == info.getY())){
            possibilities.put(AgentAction.PUT_BOMB, possibilities.get(AgentAction.PUT_BOMB)-9999);
        }
    }

    // Permet de récupérer la liste des bombes dans un certain rayon autour de l'agent
    public ArrayList<InfoBomb> getNearestBombs(){
        return (ArrayList<InfoBomb>) game.getBombList().stream().filter(e -> Math.sqrt(Math.pow(e.getX() - info.getX(), 2) + Math.pow(e.getY() - info.getY(), 2)) <= 5).collect(Collectors.toList());
    }

    // Les agents doivent absolument s'éloigner des bombes et cherchent à les éviter.
    public void runAwayFromBomb(Map<AgentAction, Integer> possibilities){
        if(!info.isInvincible() && possibilities.size() > 0) {
            for(InfoBomb bomb : getNearestBombs()){
                possibilities.put(AgentAction.PUT_BOMB, possibilities.get(AgentAction.PUT_BOMB)-100);
                // Si une bombe est sur la même colonne que l'agent
                if(bomb.getX() == info.getX()){
                    if(bomb.getY() > info.getY()){
                        possibilities.put(AgentAction.MOVE_UP, possibilities.get(AgentAction.MOVE_UP)+50);
                    } else if(bomb.getY() < info.getY()){
                        possibilities.put(AgentAction.MOVE_DOWN, possibilities.get(AgentAction.MOVE_DOWN)+50);
                    }
                    possibilities.put(AgentAction.MOVE_RIGHT, possibilities.get(AgentAction.MOVE_RIGHT)+100);
                    possibilities.put(AgentAction.MOVE_LEFT, possibilities.get(AgentAction.MOVE_LEFT)+100);
                }
                // Si une bombe est sur la même ligne que l'agent
                if(bomb.getY() == info.getY()){
                    if(bomb.getX() > info.getX()){
                        possibilities.put(AgentAction.MOVE_LEFT, possibilities.get(AgentAction.MOVE_LEFT)+50);
                    }
                    else if(bomb.getX() < info.getX()){
                        possibilities.put(AgentAction.MOVE_RIGHT, possibilities.get(AgentAction.MOVE_RIGHT)+50);
                    }
                    possibilities.put(AgentAction.MOVE_UP, possibilities.get(AgentAction.MOVE_UP)+100);
                    possibilities.put(AgentAction.MOVE_DOWN, possibilities.get(AgentAction.MOVE_DOWN)+100);
                }
                // Si une bombe se trouve à droite de l'agent, on l'incite à aller à gauche
                if(bomb.getX() > info.getX()){
                    possibilities.put(AgentAction.MOVE_LEFT, possibilities.get(AgentAction.MOVE_LEFT)+50);
                }
                // Si une bombe se trouve à gauche de l'agent, on l'incite à aller à droite
                else if(bomb.getX() < info.getX()){
                    possibilities.put(AgentAction.MOVE_RIGHT, possibilities.get(AgentAction.MOVE_RIGHT)+50);
                }
                // Si une bombe se trouve en dessous de l'agent, on l'incite à aller en haut
                if(bomb.getY() > info.getY()){
                    possibilities.put(AgentAction.MOVE_UP, possibilities.get(AgentAction.MOVE_UP)+50);
                // Si une bombe se trouve au dessus de l'agent, on l'incite à aller en bas
                } else if(bomb.getY() < info.getY()){
                    possibilities.put(AgentAction.MOVE_DOWN, possibilities.get(AgentAction.MOVE_DOWN)+50);
                }
            }
        }
    }

    // Permet de récupérer la liste des items qui sont dans un certain rayon autour de l'agent
    public ArrayList<InfoItem> getNearestItems(){
        return (ArrayList<InfoItem>) game.getItemList().stream().filter(e -> Math.sqrt(Math.pow(e.getX() - info.getX(), 2) + Math.pow(e.getY() - info.getY(), 2)) <= 10).collect(Collectors.toList());
    }

    // Les agents sont incités à se diriger vers les bons items (bonus)
    // Pour ce faire, ils suivent l'algorithme de pathfinding A*
    // Les déplacements indiqués par l'algorithme agissent sur le poids des possibilités.
    public void checkForItems(Map<AgentAction, Integer> possibilities){
        AStar pathfinding = new AStar(game.getMap().get_walls(), info.getX(), info.getY());
        for(InfoItem item : getNearestItems()){
            int toAdd = 100 - (int) Math.sqrt(Math.pow(item.getX() - info.getX(), 2) + Math.pow(item.getY() - info.getY(), 2))*10;
            if(!item.getType().isGood()){
                toAdd = -toAdd;
            }
            List<Node> path = pathfinding.getPathTo(item.getX(), item.getY());
            if(path != null && path.size() > 1) {
                Node n = path.get(1);
                if (n.getX() < info.getX()) {
                    possibilities.put(AgentAction.MOVE_LEFT, possibilities.get(AgentAction.MOVE_LEFT) + toAdd);
                } else if (n.getX() > info.getX()) {
                    possibilities.put(AgentAction.MOVE_RIGHT, possibilities.get(AgentAction.MOVE_RIGHT) + toAdd);
                } else if (n.getY() < info.getY()) {
                    possibilities.put(AgentAction.MOVE_UP, possibilities.get(AgentAction.MOVE_UP) + toAdd);
                } else if (n.getY() > info.getY()) {
                    possibilities.put(AgentAction.MOVE_DOWN, possibilities.get(AgentAction.MOVE_DOWN) + toAdd);
                }
            }
        }
    }

    // Les agents ont pour objectif de se diriger vers le bomberman
    // Pour ce faire, ils suivent l'algorithme de pathfinding A*
    // Les déplacements indiqués par l'algorithme agissent sur le poids des possibilités.
    public void targetBomberman(Map<AgentAction, Integer> possibilities){
        AStar pathfinding = new AStar(game.getMap().get_walls(), info.getX(), info.getY());

        // On cherche un bomberman dans un rayon choisie.
        Optional<Character> bomberman = game.getCharacterMap().get('B').stream().filter(e -> Math.sqrt(Math.pow(e.getX() - info.getX(), 2) + Math.pow(e.getY() - info.getY(), 2)) <= 30).findFirst();

        // Si un bomberman est trouvé, alors les agents sont incités à se diriger vers lui.
        if(bomberman.isPresent()) {
            List<Node> path = pathfinding.getPathTo(bomberman.get().getX(), bomberman.get().getY());
            if(path != null && path.size() > 1) {
                Node n = path.get(1);
                if (n.getX() < info.getX()) {
                    possibilities.put(AgentAction.MOVE_LEFT, possibilities.get(AgentAction.MOVE_LEFT) + 100);
                } else if (n.getX() > info.getX()) {
                    possibilities.put(AgentAction.MOVE_RIGHT, possibilities.get(AgentAction.MOVE_RIGHT) + 100);
                } else if (n.getY() < info.getY()) {
                    possibilities.put(AgentAction.MOVE_UP, possibilities.get(AgentAction.MOVE_UP) + 100);
                } else if (n.getY() > info.getY()) {
                    possibilities.put(AgentAction.MOVE_DOWN, possibilities.get(AgentAction.MOVE_DOWN) + 100);
                }
            }
        }
    }

    // Permet de récupérer l'agent le plus proche
    public Character getNearestCharacter(){
        if(game.getCharacterMap().get('B').size() > 0) {
            Character nearestChar = game.getCharacterMap().get('B').get(0);
            for (char key : game.getCharacterMap().keySet()) {
                for (Character character : game.getCharacterMap().get(key)) {
                    if(character == nearestChar){
                        continue;
                    }
                    if (Math.sqrt(Math.pow(character.getX() - info.getX(), 2) + Math.pow(character.getY() - info.getY(), 2)) <
                            Math.sqrt(Math.pow(nearestChar.getX() - info.getX(), 2) + Math.pow(nearestChar.getY() - info.getY(), 2))) {
                        nearestChar = character;
                    }
                }
            }
            return nearestChar;
        }
        return null;
    }

    // Si un agent est proche d'un bomberman, il est incité à poser une bombe.
    public void placeBombIfNearCharacter(Map<AgentAction, Integer> possibilities){
        Character nearestChar = getNearestCharacter();
        if(nearestChar != null) {
            if (Math.sqrt(Math.pow(nearestChar.getX() - info.getX(), 2) + Math.pow(nearestChar.getY() - info.getY(), 2)) < info.getBombRange()) {
                possibilities.put(AgentAction.PUT_BOMB, possibilities.get(AgentAction.PUT_BOMB) + 200);
            }
        }
    }

    // Plus il y a de murs cassables autour des agents, plus ils sont incités à placer une bombe.
    public void placeBombIfNearWall(Map<AgentAction, Integer> possibilities){
        for(int x = -1; x < 1; x++){
            if(x != 0) {
                if (game.hasWallAtCoords(x, info.getY())) {
                    possibilities.put(AgentAction.PUT_BOMB, possibilities.get(AgentAction.PUT_BOMB) + 20);
                } else {
                    possibilities.put(AgentAction.PUT_BOMB, possibilities.get(AgentAction.PUT_BOMB) - 50);
                }
            }
        }
        for(int y = -1; y < 1; y++){
            if(y != 0) {
                if (game.hasWallAtCoords(info.getX(), y)) {
                    possibilities.put(AgentAction.PUT_BOMB, possibilities.get(AgentAction.PUT_BOMB) + 20);
                } else {
                    possibilities.put(AgentAction.PUT_BOMB, possibilities.get(AgentAction.PUT_BOMB) - 50);
                }
            }
        }
    }

    public AgentAction selectSmartAction(){
        // Les agents sont attribués une liste d'actions possibles avec un poids,
        // ce poids sera modifié dans les différentes fonctions "neuronnes"
        Map<AgentAction, Integer> weightedPossibilities = new HashMap<>();
        Arrays.stream(AgentAction.values()).forEach(e -> weightedPossibilities.put(e, 0));

        // Liste de fonctions "neuronnes" pour l'intelligence artificielle des agents
        checkSurrounding(weightedPossibilities);
        runAwayFromBomb(weightedPossibilities);
        checkForItems(weightedPossibilities);
        if(info.getType() != 'B'){
            targetBomberman(weightedPossibilities);
        }
        placeBombIfNearCharacter(weightedPossibilities);
        placeBombIfNearWall(weightedPossibilities);

        // On ne garde que les actions avec le poids le plus élevée
        int maxWeight = -9999;
        List<AgentAction> smartActionList = new ArrayList<>();
        for(AgentAction action : weightedPossibilities.keySet()){
            if(weightedPossibilities.get(action) > maxWeight){
                smartActionList = new ArrayList<>();
                smartActionList.add(action);
                maxWeight = weightedPossibilities.get(action);
            } else if(weightedPossibilities.get(action) == maxWeight){
                smartActionList.add(action);
            }
        }
        Random random = new Random();
        AgentAction action = smartActionList.get(random.nextInt(smartActionList.size()));

        // DEBUG LINE
        //System.out.println(this + " could " + smartActionList + " and did " + action);
        return action;
    }

    @Override
    public AgentAction selectAction() {
        return selectSmartAction();
    }
}

package client;

import client.view.ViewBombermanGame;
import lombok.Getter;
import lombok.Setter;
import model.InputMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.*;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

@Setter
@Getter
public class JsonClient {
    private JSONObject data;
    private ViewBombermanGame view;

    public JsonClient(ViewBombermanGame view, JSONObject data){
        this.data = data;
        this.view = view;
    }

    // RECUPERER LES INFOS EN PROVENANCE DU SERVEUR
    public InputMap retrieveLayout(){
        String map = (String) data.get("map");
        return new InputMap(map);
    }

    public boolean[][] retrieveWalls() {
        JSONArray arr = (JSONArray) data.get("walls");
        boolean[][] viewWalls = view.getPanel().getBreakable_walls();

        for(Object obj : arr) {
            JSONObject wall = (JSONObject) obj;

            int x = ((Long) wall.get("x")).intValue();
            int y = ((Long) wall.get("y")).intValue();

            viewWalls[x][y] = false;
        }

        return viewWalls;
    }

    public ArrayList<InfoAgent> retrievePlayers() {

        JSONArray arr = (JSONArray) data.get("players");
        ArrayList<InfoAgent> agents = new ArrayList<>();

        for(Object obj : arr){
            JSONObject agent = (JSONObject) obj;

            int x = ((Long) agent.get("x")).intValue();
            int y = ((Long) agent.get("y")).intValue();
            char type = ((String) agent.get("type")).charAt(0);
            String action = (String) agent.get("action");
            boolean canFly = Boolean.parseBoolean((String) agent.get("canFly"));
            boolean isActive = Boolean.parseBoolean((String) agent.get("isActive"));
            boolean isAlive = Boolean.parseBoolean((String) agent.get("isAlive"));
            boolean isInvincible = Boolean.parseBoolean((String) agent.get("isInvincible"));
            boolean isSick = Boolean.parseBoolean((String) agent.get("isSick"));

            InfoAgent info = new InfoAgent(x, y, AgentAction.valueOf(action), type, ColorAgent.DEFAULT, canFly, isActive, isInvincible, isSick);
            info.setAlive(isAlive);

            agents.add(info);
        }
        return agents;
    }

    public ArrayList<InfoBomb> retrieveBombs() {
        JSONArray arr = (JSONArray) data.get("bombs");
        ArrayList<InfoBomb> viewBombs = new ArrayList<>();

        for (Object obj : arr) {
            JSONObject bombs = (JSONObject) obj;
            int x = ((Long) bombs.get("x")).intValue();
            int y = ((Long) bombs.get("y")).intValue();
            int range = ((Long) bombs.get("range")).intValue();
            String jState = (String) bombs.get("state");

            InfoBomb bomb = new InfoBomb(x, y, range, StateBomb.valueOf(jState));
            viewBombs.add(bomb);
        }
        return viewBombs;
    }

    public ArrayList<InfoItem> retrieveItems() {
        JSONArray items = (JSONArray) data.get("items");
        ArrayList<InfoItem> viewItems = view.getPanel().getListInfoItems();

        for(Object obj: items){
            JSONObject item = (JSONObject) obj;
            int x = ((Long) item.get("x")).intValue();
            int y = ((Long) item.get("y")).intValue();
            String type = (String) item.get("type");
            boolean state = Boolean.parseBoolean((String) item.get("state"));

            if(state){
                viewItems.add(new InfoItem(x, y, ItemType.valueOf(type)));
            } else {
                viewItems.removeIf(e -> e.getX() == x && e.getY() == y);
            }
        }

        return viewItems;
    }

    public void updateView(){
        InputMap map = retrieveLayout();
        if(!map.getFilename().equals(view.getMap().getFilename())) {
            view.setMap(map);
            view.restart();
        }

        boolean[][] walls = retrieveWalls();
        ArrayList<InfoBomb> bombs = retrieveBombs();
        ArrayList<InfoItem> items = retrieveItems();
        ArrayList<InfoAgent> infoAgents =  retrievePlayers();

        view.getPanel().updateInfoGame(walls, infoAgents, items, bombs);
        view.getPanel().repaint();
    }

}

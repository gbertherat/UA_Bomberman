package client;

import agent.Character;
import client.view.ViewBombermanGame;
import lombok.Getter;
import lombok.Setter;
import model.InputMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.InfoAgent;
import utils.InfoBomb;
import utils.InfoItem;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
        String layout = (String) data.get("layout");
        URL url = JsonClient.class.getClassLoader().getResource("layouts/"+layout);

        InputMap map = null;
        try {
            assert url != null;
            map = new InputMap(url.toURI().getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return map;
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

    public HashMap<java.lang.Character, ArrayList<Character>> retrievePlayers() {
        JSONArray players = (JSONArray) data.get("players");
        return new HashMap<>();
    }

    public ArrayList<InfoBomb> retrieveBombs() {
        JSONArray bombs = (JSONArray) data.get("bombs");
        return new ArrayList<>();
    }

    public ArrayList<InfoItem> retrieveItems() {
        JSONArray items = (JSONArray) data.get("items");
        return new ArrayList<>();
    }

    public void updateView(){
        InputMap map = retrieveLayout();
        if(!map.getFilename().equals(view.getMap().getFilename())) {
            view.setMap(map);
            view.restart();
        }

        boolean[][] walls = retrieveWalls();
        ArrayList<InfoBomb> bombs = view.getPanel().getListInfoBombs();
        ArrayList<InfoItem> items = view.getPanel().getListInfoItems();
        ArrayList<InfoAgent> infoAgents =  view.getPanel().getListInfoAgents();

        view.getPanel().updateInfoGame(walls, infoAgents, items, bombs);
    }

    // ENVOYER LES INFOS AU SERVEUR


}

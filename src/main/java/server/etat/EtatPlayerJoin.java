package server.etat;

import agent.Bomberman;
import model.BombermanGame;
import org.json.simple.JSONObject;
import server.JsonServer;
import utils.InfoAgent;

import java.util.ArrayList;

public class EtatPlayerJoin implements ServerState{
    private final JsonServer jServer;

    public EtatPlayerJoin(JsonServer jServer){
        this.jServer = jServer;
    }

    @Override
    public String sendJson(String action) {
        JSONObject obj = new JSONObject();
        BombermanGame game = jServer.getGame();

        obj.put("status", "OK");
        obj.put("message", "Vous êtes connecté au server, en attente du début de la partie.");
        obj.put("map", jServer.getServer().getMap().getFilename());

        ArrayList<InfoAgent> players = new ArrayList<>();
        for(char type : game.getCharacterMap().keySet()){
            game.getCharacterMap().get(type).forEach(e -> players.add(e.getInfo()));
        }

        obj.put("players", players);
        obj.put("walls", new ArrayList<>());
        obj.put("bombs", new ArrayList<>());
        obj.put("items", new ArrayList<>());

        return obj.toJSONString();
    }
}

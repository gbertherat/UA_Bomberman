package server.etat;

import agent.Bomberman;
import model.BombermanGame;
import org.json.simple.JSONObject;
import server.JsonServer;
import utils.AgentAction;
import utils.ColorAgent;
import utils.InfoAgent;

import java.util.ArrayList;
import java.util.Random;

public class EtatPlayerJoin implements ServerState{
    private final JsonServer jServer;
    private boolean doSendJson;

    public EtatPlayerJoin(JsonServer jServer){
        this.jServer = jServer;
        this.doSendJson = true;
    }

    @Override
    public String sendJson(String action) {
        if(!doSendJson){
            return null;
        }

        JSONObject obj = new JSONObject();
        BombermanGame game = jServer.getGame();

        if(doSendJson) {
            Random random = new Random();
            int x = 0;
            int y = 0;
            while (game.hasWallAtCoords(x, y) || game.hasCharacterAtCoords(x, y)) {
                x = random.nextInt(game.getMap().get_walls().length);
                y = random.nextInt(game.getMap().get_walls()[x].length);
            }
            game.addAgent(new InfoAgent(x, y, AgentAction.STOP, 'B', ColorAgent.DEFAULT, false, true, false, false), false);
        }

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

        doSendJson = false;
        if(players.size() >= 2){
            jServer.setState(new EtatGameStart(jServer));
        }

        return obj.toJSONString();
    }

    @Override
    public void setDoSendJson(boolean bool) {
        this.doSendJson = bool;
    }
}

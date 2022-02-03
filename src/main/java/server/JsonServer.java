package server;

import lombok.Getter;
import lombok.Setter;
import model.BombermanGame;
import org.json.simple.JSONObject;
import server.etat.EtatPlayerJoin;
import server.etat.ServerState;
import utils.InfoAgent;

import java.util.ArrayList;

@Getter
@Setter
public class JsonServer {
    private Server server;
    private ServerState state;
    private BombermanGame game;

    public JsonServer(Server server, BombermanGame game) {
        this.server = server;
        this.game = game;
        state = new EtatPlayerJoin(this);
    }

    public void setState(ServerState state) {
        this.state = state;
    }

    public String sendJson(int id, String action) {
        return state.sendJson(id, action);
    }

    public JSONObject getGameData(String message){
        JSONObject obj = new JSONObject();
        obj.put("status", "OK");
        obj.put("message", message);
        obj.put("map", getServer().getMap().getFilename());

        ArrayList<InfoAgent> players = new ArrayList<>();
        for(char type : game.getCharacterMap().keySet()){
            game.getCharacterMap().get(type).forEach(e -> players.add(e.getInfo()));
        }

        obj.put("players", players);
        obj.put("walls", new ArrayList<>());
        obj.put("bombs", game.getBombList());
        obj.put("items", game.getItemList());

        return obj;
    }
}

package server;

import lombok.Getter;
import lombok.Setter;
import model.BombermanGame;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.etat.EtatGameEnd;
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

        JSONArray destroyedWalls = new JSONArray();
        for(ArrayList<Integer> coords : game.getDestroyedWalls()){
            JSONObject coord = new JSONObject();
            coord.put("x", coords.get(0));
            coord.put("y", coords.get(1));

            destroyedWalls.add(coord);
        }

        obj.put("walls", destroyedWalls);
        obj.put("bombs", game.getBombList());
        obj.put("items", game.getItemList());

        return obj;
    }
}

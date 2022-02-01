package server;

import lombok.Getter;
import lombok.Setter;
import model.BombermanGame;
import org.json.simple.JSONObject;
import server.etat.EtatPlayerJoin;
import server.etat.ServerState;

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

    public String sendJson(String action) {
        return state.sendJson(action);
    }
}

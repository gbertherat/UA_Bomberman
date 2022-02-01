package server.etat;

import org.json.simple.JSONObject;
import server.JsonServer;

public class EtatGameRunning implements ServerState{
    private final JsonServer server;

    public EtatGameRunning(JsonServer server){
        this.server = server;
    }

    @Override
    public String sendJson(String action) {
        server.sendJson(action);
        return new JSONObject().toJSONString();
    }
}

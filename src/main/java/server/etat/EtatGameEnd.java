package server.etat;

import org.json.simple.JSONObject;
import server.JsonServer;

public class EtatGameEnd implements ServerState{
    private final JsonServer jServer;

    public EtatGameEnd(JsonServer jServer){
        this.jServer = jServer;
    }

    @Override
    public String sendJson(int id, String action) {
        return new JSONObject().toJSONString();
    }
}

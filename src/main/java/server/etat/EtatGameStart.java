package server.etat;

import org.json.simple.JSONObject;
import server.JsonServer;

public class EtatGameStart implements ServerState{
    private final JsonServer jServer;

    public EtatGameStart(JsonServer jServer){
        this.jServer = jServer;
    }

    @Override
    public String sendJson(String action) {
        return new JSONObject().toJSONString();
    }
}

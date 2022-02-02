package server.etat;

import org.json.simple.JSONObject;
import server.JsonServer;

public class EtatGameEnd implements ServerState{
    private final JsonServer jServer;
    private boolean doSendJson;

    public EtatGameEnd(JsonServer jServer){
        this.jServer = jServer;
        this.doSendJson = true;
    }

    @Override
    public String sendJson(String action) {
        return new JSONObject().toJSONString();
    }

    @Override
    public void setDoSendJson(boolean bool) {
        this.doSendJson = bool;
    }
}

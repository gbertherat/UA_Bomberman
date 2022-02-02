package server.etat;

import org.json.simple.JSONObject;
import server.JsonServer;

public class EtatGameRunning implements ServerState{
    private final JsonServer server;
    private boolean doSendJson;

    public EtatGameRunning(JsonServer server){
        this.server = server;
        this.doSendJson = true;
    }

    @Override
    public String sendJson(String action) {
        server.sendJson(action);
        return new JSONObject().toJSONString();
    }

    @Override
    public void setDoSendJson(boolean bool) {
        this.doSendJson = bool;
    }
}

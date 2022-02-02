package server.etat;

import org.json.simple.JSONObject;

public interface ServerState {
    String sendJson(String action);
    void setDoSendJson(boolean bool);
}

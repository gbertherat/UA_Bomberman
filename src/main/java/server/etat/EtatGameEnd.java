package server.etat;

public class EtatGameEnd implements ServerState{

    public EtatGameEnd(){}

    @Override
    public String sendJson(int id, String action) {
        return "EXIT";
    }
}

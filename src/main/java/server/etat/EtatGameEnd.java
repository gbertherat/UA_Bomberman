package server.etat;

import agent.Character;
import server.JsonServer;
import server.ServerClientThread;

import java.util.ArrayList;

public class EtatGameEnd implements ServerState{
    JsonServer jServer;

    public EtatGameEnd(JsonServer jServer){
        this.jServer = jServer;
    }

    @Override
    public String sendJson(int id, String action) {
        ArrayList<Character> players = new ArrayList<>();
        jServer.getGame().getCharacterMap().keySet().forEach(key -> players.addAll(jServer.getGame().getCharacterMap().get(key)));
        if(players.size() == 1){
            Character player = players.get(0);
            if(!player.isAI() && player.getInfo().getId() == id){
                return "EXIT:Vous avez gagné!";
            }
        }

        return "EXIT:Vous avez perdu..";
    }
}

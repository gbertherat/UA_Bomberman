package server.etat;

import agent.Character;
import model.BombermanGame;
import server.JsonServer;
import utils.AgentAction;
import utils.InfoAgent;

import java.util.Optional;

public class EtatGameRunning implements ServerState {
    private final JsonServer server;

    public EtatGameRunning(JsonServer server) {
        this.server = server;
    }

    @Override
    public String sendJson(int id, String action) {
        BombermanGame game = server.getGame();
        InfoAgent agent = null;

        for(char c : game.getCharacterMap().keySet()){
            Optional<Character> ch = game.getCharacterMap().get(c).stream().filter(e -> e.getInfo().getId() == id).findFirst();
            if(ch.isPresent()){
                agent = ch.get().getInfo();
            }
        }

        if(agent != null){
            agent.setAgentAction(AgentAction.valueOf(action));
        }

        return server.getGameData("Jeu en cours").toJSONString();
    }
}

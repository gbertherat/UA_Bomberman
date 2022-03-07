package server.etat;

import agent.Character;
import model.BombermanGame;
import server.JsonServer;
import utils.AgentAction;
import utils.InfoAgent;
import utils.InfoBomb;
import utils.StateBomb;

import java.util.ArrayList;
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

        for(InfoBomb bomb : game.getBombList()){
            if(bomb.getStateBomb() == StateBomb.Exploded){
                game.checkWallsInBombRange(bomb.getX(), bomb.getY(), bomb.getRange());
                game.checkCharactersInBombRange(bomb.getX(), bomb.getY(), bomb.getRange());
            }
        }

        ArrayList<InfoAgent> players = new ArrayList<>();
        for(char type : game.getCharacterMap().keySet()){
            game.getCharacterMap().get(type).forEach(e -> players.add(e.getInfo()));
        }
        if(players.size() <= 1){
            server.setState(new EtatGameEnd());
        }
        return server.getGameData("Jeu en cours").toJSONString();
    }
}

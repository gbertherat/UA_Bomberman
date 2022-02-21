package server.etat;

import agent.Bomberman;
import model.BombermanGame;
import org.json.simple.JSONObject;
import server.JsonServer;
import server.Timer;
import utils.AgentAction;
import utils.ColorAgent;
import utils.InfoAgent;

import java.util.ArrayList;
import java.util.Random;

public class EtatPlayerJoin implements ServerState{
    private final JsonServer jServer;
    private boolean addedPlayer;

    public EtatPlayerJoin(JsonServer jServer){
        this.jServer = jServer;
    }

    @Override
    public String sendJson(int id, String action) {
        BombermanGame game = jServer.getGame();

        if(!addedPlayer) {
            Random random = new Random();
            int x = 0;
            int y = 0;
            while (game.hasWallAtCoords(x, y) || game.hasCharacterAtCoords(x, y)) {
                x = random.nextInt(game.getMap().get_walls().length);
                y = random.nextInt(game.getMap().get_walls()[x].length);
            }
            game.addAgent(new InfoAgent(id, x, y, AgentAction.STOP, 'B', ColorAgent.DEFAULT, false, true, false, false), id,false);
            addedPlayer = true;
        }

        JSONObject obj = jServer.getGameData("Vous êtes connecté au server, en attente du début de la partie.");

        if(jServer.getServer().getClients().size() >= 2){
            System.out.println("Enough players joined, starting the game.");
            jServer.setState(new EtatGameRunning(jServer));
        }

        return obj.toJSONString();
    }
}

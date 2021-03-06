package server;

import agent.Character;
import lombok.Getter;
import model.BombermanGame;
import model.InputMap;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

@Getter
public class Server {

    private int id;
    private final ServerSocket sSocket;
    private final ArrayList<ServerClientThread> clients;
    private final ServerConnectionThread sct;
    private final BombermanGame game;

    public Server() throws IOException {
        this.id = -1;

        clients = new ArrayList<>();

        this.sSocket = new ServerSocket(0);
        this.sct = new ServerConnectionThread(this, sSocket);

        this.game = new BombermanGame(1024, new InputMap("niveau2.lay"));
        this.game.init();
    }

    public BombermanGame getGame() {
        return game;
    }

    public InputMap getMap() {
        return game.getMap();
    }

    private void addServerToServerlist(){
        try {
            // Requête HTTP GET à l'API pour ajouter le serveur à la liste des serveurs
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet request = new HttpGet("http://127.0.0.1:8080/BombermanJEE/server"+
                    "?ip="  + sSocket.getInetAddress().getHostAddress() +
                    "&port="+ sSocket.getLocalPort() +
                    "&token=791cdc4f-1812-4078-a265-4feed8f2af2b");

            try (CloseableHttpResponse response = client.execute(request)) {
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    String result = EntityUtils.toString(entity).trim();
                    if(!result.equals("null")){
                        this.id = Integer.parseInt(result);
                        System.out.println("Server added to serverlist!");
                    }
                }
            }
        } catch (IOException el) {
            el.printStackTrace();
        }
    }

    private void removeServerFromServerlist(){
        if(this.id == -1){
            return;
        }

        try {
            // Requête HTTP DELETE à l'API pour supprimer le serveur de la liste des serveurs
            CloseableHttpClient client = HttpClients.createDefault();
            HttpDelete request = new HttpDelete("http://127.0.0.1:8080/BombermanJEE/server?id=" + this.id +
                    "&token=047ff0b3-d5cf-4549-b46a-1f876984c93d");
            client.execute(request);
            System.out.println("Server removed from server list");

        } catch (IOException el) {
            el.printStackTrace();
        }
    }

    private int addGame(){
        try {
            // Requête HTTP POST pour ajouter la partie à la liste des parties
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost request = new HttpPost("http://127.0.0.1:8080/BombermanJEE/game/add");

            ArrayList<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("map", game.getMap().getFilename()));
            params.add(new BasicNameValuePair("token", "2028e275-7757-44f9-893a-d1990153c6c7"));
            request.setEntity(new UrlEncodedFormEntity(params));

            try (CloseableHttpResponse response = client.execute(request)) {
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    String result = EntityUtils.toString(entity).trim();
                    if(!result.equals("null")){
                        int id = Integer.parseInt(result);
                        return id;
                    }
                }
            }

        } catch (IOException el) {
            el.printStackTrace();
        }
        return -1;
    }

    private void addGamePlayed(int userid, int gameid){
        try {
            // Requête HTTP POST pour ajouter la partie à la liste des parties jouées d'un joueur
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost request = new HttpPost("http://127.0.0.1:8080/BombermanJEE/game/played");

            ArrayList<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("userid", String.valueOf(userid)));
            params.add(new BasicNameValuePair("gameid", String.valueOf(gameid)));
            params.add(new BasicNameValuePair("token", "132913b9-6b2e-44e0-8265-3562299e1518"));
            request.setEntity(new UrlEncodedFormEntity(params));
            client.execute(request);

        } catch (IOException el) {
            el.printStackTrace();
        }
    }

    private void addGameWon(int userid, int gameid){
        try {
            // Requête HTTP POST pour ajouter la partie à la liste des parties gagnées d'un joueur
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost request = new HttpPost("http://127.0.0.1:8080/BombermanJEE/game/won");

            ArrayList<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("userid", String.valueOf(userid)));
            params.add(new BasicNameValuePair("gameid", String.valueOf(gameid)));
            params.add(new BasicNameValuePair("token", "c479631b-e171-4cdc-bad0-e60b7c98b281"));
            request.setEntity(new UrlEncodedFormEntity(params));
            client.execute(request);

        } catch (IOException el) {
            el.printStackTrace();
        }
    }

    public void execute() {
        // Méthode principale utilisée par le serveur, elle tourne en continue pour mettre à jour les données
        // du jeu en fonction des actions des joueurs et des bots.
        // Note: Pour que le serveur puisse se lancé, le serveur JEE doit être lancé aussi.

        this.sct.start(); // Permet de lancer le thread de gestion des connexions utilisateurs
        addServerToServerlist(); // On ajoute le serveur à la liste des serveurs
        System.out.println("Server listening on port: " + sSocket.getLocalPort());

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                removeServerFromServerlist(); // Le serveur est supprimé s'il s'éteint.
            }
        });

        while(!game.isFinished()){
            if(clients.stream().filter(e -> e.getClientId() != -1).count() >= 2 && !game.isStarted()){
                // Si le jeu n'est pas démarré et qu'on a au moins 2 joueurs connectés, on lance la partie.
                clients.removeIf(e -> e.getClientId() == -1);
                try {
                    this.sct.getsSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int id = addGame();
                game.setId(id);

                ArrayList<Integer> ids = new ArrayList<>();
                for(int i = 0; i < getClients().size(); i++){
                    ids.add(getClients().get(i).getClientId());
                }
                game.addBots(getClients().size(), ids); // On ajoute les bots à la partie
                game.setStarted(true);

                removeServerFromServerlist(); // On supprime le serveur de la liste des serveurs
            }

            if(game.isStarted()){
                game.setDestroyedWalls(new ArrayList<>());
                game.takeTurn(); // Permet de mettre à jour / faire avancer la partie
            }

            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for(ServerClientThread sct : clients){
            int clientId = sct.getClientId();
            addGamePlayed(clientId, game.getId()); // On ajoute la partie jouée à chaque client de la partie.
        }

        ArrayList<Character> players = new ArrayList<>();
        game.getCharacterMap().keySet().forEach(key -> players.addAll(game.getCharacterMap().get(key)));
        if(players.size() == 1){
            Character player = players.get(0);
            if(!player.isAI()){ // Si le joueur n'est pas un bot
                addGameWon(player.getInfo().getId(), game.getId()); // On ajoute la partie gagnée au joueur gagnant
            }
        }
    }

    public void removeClient(ServerClientThread clientThread) {
        // Permet de supprimer le client du serveur.
        System.out.println("A client has left!");

        for(char c : game.getCharacterMap().keySet()){
            for(Character character : game.getCharacterMap().get(c)){
                if(character.getInfo().getId() == clientThread.getClientId()){
                    game.getCharacterMap().get(c).remove(character);
                    break;
                }
            }
        }

        this.clients.remove(clientThread);

        // S'il n'y a qu'un seul joueur de connecter, la partie se termine.
        if(this.clients.size() <= 1){
            this.game.setFinished(true);
        }
    }

    public static void main(String[] args) {
        try {
            Server server = new Server(); // Création d'un serveur.
            server.execute();
        } catch (IOException e) {
            System.out.println("Server error (main):");
            e.printStackTrace();
        }
    }
}

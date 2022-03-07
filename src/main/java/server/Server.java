package server;

import agent.Character;
import lombok.Getter;
import model.BombermanGame;
import model.InputMap;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
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
    private ArrayList<ServerClientThread> clients;
    private ServerConnectionThread sct;
    private BombermanGame game;

    public Server() throws IOException {
        this.id = -1;

        clients = new ArrayList<>();

        this.sSocket = new ServerSocket(0);
        this.sct = new ServerConnectionThread(this, sSocket, clients);

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
            CloseableHttpClient client = HttpClients.createDefault();
            HttpDelete request = new HttpDelete("http://127.0.0.1:8080/BombermanJEE/server?id=" + this.id +
                    "&token=047ff0b3-d5cf-4549-b46a-1f876984c93d");
            client.execute(request);
            System.out.println("Server removed from server list");

        } catch (IOException el) {
            el.printStackTrace();
        }
    }

    private void addGamePlayed(int id){
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPut request = new HttpPut("http://127.0.0.1:8080/BombermanJEE/user/gameplayed");

            ArrayList<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("id", String.valueOf(id)));
            params.add(new BasicNameValuePair("token", "71ed552a-59b9-4ee1-926a-1f04e8e11d32"));
            request.setEntity(new UrlEncodedFormEntity(params));
            client.execute(request);

        } catch (IOException el) {
            el.printStackTrace();
        }
    }

    private void addGameWon(int id){
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPut request = new HttpPut("http://127.0.0.1:8080/BombermanJEE/user/gamewon");

            ArrayList<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("id", String.valueOf(id)));
            params.add(new BasicNameValuePair("token", "8e79d143-8cdf-4abf-96e8-93f0e172a12b"));
            request.setEntity(new UrlEncodedFormEntity(params));
            client.execute(request);

        } catch (IOException el) {
            el.printStackTrace();
        }
    }

    public void execute() {
        this.sct.start();
        addServerToServerlist();
        System.out.println("Server listening on port: " + sSocket.getLocalPort());

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                removeServerFromServerlist();
            }
        });

        while(!game.isFinished()){
            if(clients.stream().filter(e -> e.getClientId() != -1).count() >= 2 && !game.isStarted()){
                game.setStarted(true);
                sct.setExit(true);
                removeServerFromServerlist();
            }

            if(game.isStarted()){
                game.takeTurn();
            }

            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for(ServerClientThread sct : clients){
            int clientId = sct.getClientId();
            addGamePlayed(clientId);
        }
        ArrayList<Character> players = new ArrayList<>();
        game.getCharacterMap().keySet().forEach(key -> players.addAll(game.getCharacterMap().get(key)));
        if(players.size() == 1){
            Character player = players.get(0);
            if(!player.isAI()){
                addGameWon(player.getInfo().getId());
            }
        }
    }

    public void removeClient(ServerClientThread clientThread) {
        this.clients.remove(clientThread);
        System.out.println("A client has left!");
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.execute();
        } catch (IOException e) {
            System.out.println("Server error (main):");
            e.printStackTrace();
        }
    }
}

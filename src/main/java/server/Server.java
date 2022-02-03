package server;

import controller.DefaultSpeed;
import lombok.Getter;
import model.BombermanGame;
import model.InputMap;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

@Getter
public class Server {

    private final ServerSocket sSocket;
    private ArrayList<ServerClientThread> clients;
    private ServerConnectionThread sct;
    private BombermanGame game;

    public Server() throws IOException {
        clients = new ArrayList<>();
        this.sSocket = new ServerSocket(1664);

        this.sct = new ServerConnectionThread(this, sSocket, clients);

        this.game = new BombermanGame(1024, DefaultSpeed.value, new InputMap("niveau2.lay"));
        this.game.init();
    }

    public BombermanGame getGame() {
        return game;
    }

    public InputMap getMap() {
        return game.getMap();
    }

    public void execute() {
        this.sct.start();
        System.out.println("Server listening on port: " + sSocket.getLocalPort());

        while(true){
            if(clients.size() >= 1){
                game.setStarted(true);
            }

            if(game.isStarted()){
                game.takeTurn();
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeClient(ServerClientThread clientThread) {
        this.clients.remove(clientThread);
        System.out.println("A client has left!");
    }

    public void broadcast(String msg) {
        for (ServerClientThread sct : clients) {
            sct.sendJson(msg);
        }
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

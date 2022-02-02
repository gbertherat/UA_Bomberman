package server;

import controller.DefaultSpeed;
import model.BombermanGame;
import model.InputMap;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class Server {

    private final ServerSocket sSocket;
    private ArrayList<ServerClientThread> arrayClientThreads;
    private BombermanGame game;
    private JsonServer jServer;

    public Server() throws IOException {
        arrayClientThreads = new ArrayList<>();
        this.sSocket = new ServerSocket(1664);

        this.game = new BombermanGame(1024, DefaultSpeed.value, new InputMap("niveau2.lay"));
        this.game.init();

        this.jServer = new JsonServer(this, game);
    }

    public BombermanGame getGame() {
        return game;
    }

    public InputMap getMap(){
        return game.getMap();
    }

    public JsonServer getjServer() {
        return jServer;
    }

    public void execute() {
        System.out.println("Server listening on port: " + sSocket.getLocalPort());
        acceptConnection();
    }

    public void acceptConnection() {
        while (true) {
            try {
                Socket socket = sSocket.accept();
                jServer.getState().setDoSendJson(true);
                ServerClientThread clientThread = new ServerClientThread(socket, this);
                arrayClientThreads.add(clientThread);
                Thread thread = new Thread(clientThread);
                thread.start();
                System.out.println("New user connected");

            } catch (IOException e) {
                System.out.println("Server error (acceptConnection):");
                e.printStackTrace();
            }
        }
    }

    public void removeClient(ServerClientThread clientThread) {
        this.arrayClientThreads.remove(clientThread);
        System.out.println("A client has left!");
    }

    public void broadcast(String msg) {
        for (ServerClientThread sct : arrayClientThreads) {
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

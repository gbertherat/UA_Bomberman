package server;

import model.BombermanGame;
import server.controller.ControllerBombermanGame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private final ServerSocket sSocket;
    private ArrayList<ServerClientThread> arrayClientThreads;

    public Server() throws IOException {
        arrayClientThreads = new ArrayList<>();
        this.sSocket = new ServerSocket(1664);
    }

    public void execute() {
        System.out.println("Server listening on port: " + sSocket.getLocalPort());
        acceptConnection();
    }

    public void acceptConnection() {
        while (true) {
            try {
                Socket socket = sSocket.accept();
                ServerClientThread clientThread = new ServerClientThread(socket, this);
                arrayClientThreads.add(clientThread);
                Thread thread = new Thread(clientThread);
                thread.start();
                System.out.println("New user connected");

            } catch (IOException e) {
                System.out.println("Server error (acceptConnection):\n" + e.getMessage());
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
            System.out.println("Server error (main):\n" + e.getMessage());
        }
    }
}

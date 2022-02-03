package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerConnectionThread extends Thread {
    private final Server server;
    private ServerSocket sSocket;
    private ArrayList<ServerClientThread> clients;

    public ServerConnectionThread(Server server, ServerSocket socket, ArrayList<ServerClientThread> clients){
        this.server = server;
        this.sSocket = socket;
        this.clients = clients;
    }

    @Override
    public void run() {
        int id = 0;
        while (true) {
            try {
                Socket socket = sSocket.accept();
                System.out.println("New user connected");

                ServerClientThread clientThread = new ServerClientThread(id, socket, server, server.getGame());
                clients.add(clientThread);

                clientThread.start();
                id++;
            } catch (IOException e) {
                System.out.println("Server error (acceptConnection):");
                e.printStackTrace();
            }
        }
    }
}

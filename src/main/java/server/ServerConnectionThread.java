package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerConnectionThread extends Thread {
    private final Server server;
    private ServerSocket sSocket;
    private ArrayList<ServerClientThread> clients;
    private boolean exit;

    public ServerConnectionThread(Server server, ServerSocket socket, ArrayList<ServerClientThread> clients){
        this.server = server;
        this.sSocket = socket;
        this.clients = clients;
        this.exit = false;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    @Override
    public void run() {
        int id = 0;
        while (!exit) {
            try {
                Socket socket = sSocket.accept();
                System.out.println("New user connected");

                ServerClientThread clientThread = new ServerClientThread(id, socket, server, server.getGame());
                clients.add(clientThread);

                clientThread.start();

                if(server.getClients().size() >= 2){
                    this.exit = true;
                }
                id++;
            } catch (IOException e) {
                System.out.println("Server error (acceptConnection):");
                e.printStackTrace();
            }
        }
        try {
            sSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

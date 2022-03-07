package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerConnectionThread extends Thread {
    private final Server server;
    private final ServerSocket sSocket;
    private boolean exit;

    public ServerConnectionThread(Server server, ServerSocket socket, ArrayList<ServerClientThread> clients){
        this.server = server;
        this.sSocket = socket;
        this.exit = false;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    @Override
    public void run() {
        while (!exit) {
            try {
                Socket socket = sSocket.accept();
                System.out.println("New client joining");

                ServerClientThread clientThread = new ServerClientThread(socket, server, server.getGame());
                clientThread.start();

                if(server.getClients().stream().filter(e -> e.getClientId() != -1).count() >= 2){
                    this.exit = true;
                    server.getClients().removeIf(e -> e.getClientId() == -1);
                }
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

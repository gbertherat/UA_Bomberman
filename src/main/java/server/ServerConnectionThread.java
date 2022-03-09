package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ServerConnectionThread extends Thread {
    private final Server server;
    private final ServerSocket sSocket;

    public ServerConnectionThread(Server server, ServerSocket socket){
        this.server = server;
        this.sSocket = socket;
    }

    public ServerSocket getsSocket() {
        return sSocket;
    }

    @Override
    public void run() {
        while (!sSocket.isClosed()) {
            try {
                Socket socket = sSocket.accept();
                System.out.println("New client joining");

                ServerClientThread clientThread = new ServerClientThread(socket, server, server.getGame());
                clientThread.start();
            } catch (SocketException e){
                System.out.println("Server closed to new players");
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

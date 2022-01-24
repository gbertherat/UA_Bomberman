package client.thread;

import client.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientWriter extends Thread {
    private final Socket socket;
    private final Client client;
    private final PrintWriter writer;

    public ClientWriter(Socket socket, Client client) throws IOException {
        this.socket = socket;
        this.client = client;
        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        // Envoie du JSON

        try{
            do {
                break;
            } while(true);
        } catch (IOException e) {
            System.out.println("ClientWriter error (run): " + e.getMessage());
        }
    }
}

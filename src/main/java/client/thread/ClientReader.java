package client.thread;

import client.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReader extends Thread {
    private final Socket socket;
    private final Client client;
    private final BufferedReader reader;

    public ClientReader(Socket socket, Client client) throws IOException {
        this.socket = socket;
        this.client = client;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        // Réception du JSON

        try{
            do {
                break;
            } while(true);
        } catch (IOException e) {
            System.out.println("ClientReader error (run): " + e.getMessage());
        }
    }
}

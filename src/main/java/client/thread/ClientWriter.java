package client.thread;

import client.Client;
import client.view.PanelBomberman;
import client.view.ViewBombermanGame;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientWriter extends Thread {
    private final Socket socket;
    private final Client client;
    private final PrintWriter writer;
    private final ViewBombermanGame view;

    public ClientWriter(Socket socket, Client client, ViewBombermanGame view) throws IOException {
        this.socket = socket;
        this.client = client;
        writer = new PrintWriter(socket.getOutputStream(), true);
        this.view = view;
    }

    @Override
    public void run() {

        do {
//            writer.println("{" +
//                    "\"status\":\"OK\"," +
//                    "\"message\":\"Test\"," +
//                    "\"players\":[]," +
//                    "\"walls\":[]," +
//                    "\"bombs\":[]," +
//                    "\"items\":[]," +
//                    "}");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while (!socket.isClosed());

        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("ClientWriter error (run):\n" + e.getMessage());
        }
    }
}

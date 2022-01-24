package client;

import client.thread.ClientReader;
import client.thread.ClientWriter;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.Socket;

@Setter
@Getter
public class Client {

    private String username;
    private final String host;
    private final int port;

    private final Socket socket;
    private final ClientWriter clientWriter;
    private final ClientReader clientReader;

    public Client(String host, int port) throws IOException {
        this.host = host;
        this.port = port;

        this.socket = new Socket(host, port);
        this.clientWriter = new ClientWriter(socket, this);
        this.clientReader = new ClientReader(socket, this);
    }

    public void execute() {
        clientWriter.start();
        clientReader.start();
    }

    public void killProcess(ClientReader r, ClientWriter w) {
        r.interrupt();
        w.interrupt();

        System.exit(1);
    }

    public static void main(String[] args) {
        try {
            Client client = new Client("127.0.0.1", 1664);
            client.execute();
        } catch (IOException e) {
            System.out.println("Client error (main):\n" + e.getMessage());
        }
    }
}

package client;

import client.thread.ClientReader;
import client.thread.ClientWriter;
import client.view.PanelBomberman;
import client.view.ViewBombermanGame;
import lombok.Getter;
import lombok.Setter;
import model.InputMap;
import server.controller.AbstractController;
import server.controller.ControllerBombermanGame;

import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;

@Setter
@Getter
public class Client {

    private String username;
    private final String host;
    private final int port;

    private final Socket socket;
    private final ClientWriter clientWriter;
    private final ClientReader clientReader;
    private final ViewBombermanGame view;

    public Client(String host, int port, ViewBombermanGame view) throws IOException {
        this.host = host;
        this.port = port;

        this.socket = new Socket(host, port);
        this.clientWriter = new ClientWriter(socket, this, view);
        this.clientReader = new ClientReader(socket, this, view);
        this.view = view;
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

    public static void main(String[] args) throws URISyntaxException {
        try {
            URL url = Client.class.getClassLoader().getResource("layouts/niveau3.lay");
            assert url != null;

            ViewBombermanGame view = new ViewBombermanGame();
            InputMap map = new InputMap(url.toURI().getPath());
            view.setMap(map);
            view.init(map.get_walls().length*48,map.get_walls()[0].length*48,-100);

            Client client = new Client("127.0.0.1", 1664, view);
            client.execute();
        } catch (IOException e) {
            System.out.println("Client error (main):\n" + e.getMessage());
        }
    }
}

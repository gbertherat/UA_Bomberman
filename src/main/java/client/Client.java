package client;

import client.thread.ClientReader;
import client.thread.ClientWriter;
import client.view.ViewBombermanGame;
import client.view.ViewConnection;
import client.view.ViewGameEnd;
import lombok.Getter;
import lombok.Setter;
import model.InputMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;

@Setter
@Getter
public class Client {
    private final int id;
    private final Socket socket;
    private final ClientWriter clientWriter;
    private final ClientReader clientReader;
    private final ViewBombermanGame view;

    public Client(Socket socket, int id, ViewBombermanGame view) throws IOException {
        this.id = id;
        this.socket = socket;
        this.clientWriter = new ClientWriter(socket, view);
        this.clientReader = new ClientReader(socket, this, view);
        this.view = view;
    }

    public void execute() { // On lance les threads writer et reader
        clientWriter.start();
        clientReader.start();
    }

    public void disconnect(String cause){
        view.getJFrame().setVisible(false);
        view.getJFrame().dispose();

        try {
            killProcess();
            getSocket().close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        ViewGameEnd vge = new ViewGameEnd(cause);
        vge.init(500,500,-100);
    }

    public void killProcess() throws InterruptedException {
        // Pour arrêter les threads, on met exit à true et on attends qu'ils s'arrêtent.
        clientWriter.setExit(true);
        clientWriter.join();

        clientReader.setExit(true);
    }

    public static void startClient(Socket socket, int id){
        try {
            ViewBombermanGame view = new ViewBombermanGame();
            InputMap map = new InputMap("niveau3.lay");
            view.setMap(map);
            view.init(map.get_walls().length * 48, map.get_walls()[0].length * 48, -100);

            Client client = new Client(socket, id, view); // On créé le client
            client.execute();

            view.getJFrame().addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent windowEvent) {
                    try {
                        client.killProcess();
                        client.getSocket().close();

                        System.exit(0);
                    } catch (IOException | InterruptedException e) {
                        System.out.println("windowClosing error :");
                        e.printStackTrace();
                    }
                }
            });

        } catch (IOException e) {
            System.out.println("Client error (main):");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ViewConnection connection = new ViewConnection(); // On lance la fenêtre de connexion
        connection.init(500,600,-100);
    }
}

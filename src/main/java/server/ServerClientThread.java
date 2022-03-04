package server;

import model.BombermanGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerClientThread extends Thread {
    private int clientId;
    private final Socket socket;
    private final Server server;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private BombermanGame game;

    public ServerClientThread(int clientId, Socket socket, Server server, BombermanGame game) throws IOException {
        this.game = game;
        this.clientId = clientId;
        this.socket = socket;
        this.server = server;
        this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
    }

    public int getClientId() {
        return clientId;
    }

    @Override
    public void run() {
        try {
            JsonServer jsonServer = new JsonServer(this.server, game);

            while (!socket.isClosed()){
                String obj = reader.readLine();
                if(obj != null) {
                    if (obj.equals("EXIT")) {
                        socket.close();
                    } else if (obj.chars().allMatch(Character::isDigit)) {
                        this.clientId = Integer.parseInt(obj);
                    } else {
                        String json = jsonServer.sendJson(clientId, obj);

                        if (json != null) {
                            writer.println(json);
                        }

                        Thread.sleep(100);
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            server.removeClient(this);
        }
    }

    public void sendJson(String msg) {
        writer.println(msg);
    }
}

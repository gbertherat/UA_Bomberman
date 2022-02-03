package server;

import model.BombermanGame;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.AgentAction;
import utils.ColorAgent;
import utils.InfoAgent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class ServerClientThread extends Thread {
    private final int id;
    private final Socket socket;
    private final Server server;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private BombermanGame game;

    public ServerClientThread(int id, Socket socket, Server server, BombermanGame game) throws IOException {
        this.game = game;
        this.id = id;
        this.socket = socket;
        this.server = server;
        this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            JsonServer jsonServer = new JsonServer(this.server, game);

            while (!socket.isClosed()){
                String obj = reader.readLine();

                if(obj.equals("EXIT")){
                    socket.close();
                } else {
                    String json = jsonServer.sendJson(id, obj);

                    if(json != null){
                        System.out.println(json);
                        writer.println(json);
                    }
                    Thread.sleep(500);
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

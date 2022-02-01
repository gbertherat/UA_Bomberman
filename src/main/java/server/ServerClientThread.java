package server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerClientThread implements Runnable {
    private final Socket socket;
    private final Server server;
    private final BufferedReader reader;
    private final PrintWriter writer;

    public ServerClientThread(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        String json = "{" +
                "\"status\":\"OK\"," +
                "\"message\":\"Envoie de test\"," +
                "\"layout\":\"niveau3.lay\"," +
                "\"walls\":[" +
                "{" +
                "\"x\":3," +
                "\"y\":3" +
                "}," +
                "]," +
                "\"players\":[" +
                "{" +
                "\"x\":3," +
                "\"y\":3," +
                "\"type\":\"B\"," +
                "\"action\":\"MOVE_DOWN\"," +
                "\"canFly\":\"false\"," +
                "\"isActive\":\"true\"," +
                "\"isAlive\":\"true\"," +
                "\"isInvincible\":\"false\"," +
                "\"isSick\":\"false\"," +
                "}," +
                "{" +
                "\"x\":3," +
                "\"y\":6," +
                "\"type\":\"R\"," +
                "\"action\":\"MOVE_UP\"," +
                "\"canFly\":\"false\"," +
                "\"isActive\":\"true\"," +
                "\"isAlive\":\"true\"," +
                "\"isInvincible\":\"false\"," +
                "\"isSick\":\"false\"," +
                "}" +
                "]," +
                "\"bombs\":[" +
                "{" +
                "\"x\":3," +
                "\"y\":4," +
                "\"range\":3," +
                "\"state\":\"Step3\"" +
                "}" +
                "]," +
                "\"items\":[" +
                "{" +
                "\"x\":4," +
                "\"y\":4," +
                "\"type\":\"FIRE_UP\"" +
                "\"state\":\"true\"" +
                "}" +
                "]" +
                "}";
        server.broadcast(json);

        try {
            while (!socket.isConnected()){
                String line;
                if((line = reader.readLine()) != null){
                    System.out.println(line);
                }
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            server.removeClient(this);
        }
    }

    public void sendJson(/*DU JSON*/ String msg) {
        writer.println(msg);
    }
}

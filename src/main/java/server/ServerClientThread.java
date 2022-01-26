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
        JSONParser parser = new JSONParser();

        try {
            do {
                String json = "{" +
                        "\"status\":\"OK\"," +
                        "\"message\":\"Envoie de test\"," +
                        "\"layout\":\"niveau3.lay\"," +
                        "\"walls\":[" +
                            "{" +
                                "\"x\":3" +
                                "\"y\":3" +
                            "}," +
                        "]," +
                        "\"players\":[]," +
                        "\"bombs\":[]," +
                        "\"items\":[]," +
                        "}";
                server.broadcast(json);
                Thread.sleep(1000);
            } while (socket.isConnected());

            server.removeClient(this);
            socket.close();
        } catch (IOException | InterruptedException e) {
            System.out.println("ServerClientThread error (run):\n" + e.getMessage());
        }
    }

    public void sendJson(/*DU JSON*/ String msg) {
        writer.println(msg);
    }
}

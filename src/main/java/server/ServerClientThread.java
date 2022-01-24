package server;

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
        this.writer = new PrintWriter(socket.getOutputStream());
    }

    @Override
    public void run() {

        do {
            break;
        } while (true);

        server.removeClient(this);

        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("ServerClientThread error (run):\n" + e.getMessage());
        }
    }

    public void sendJson(/*DU JSON*/ String msg) {
        writer.println(msg);
    }
}

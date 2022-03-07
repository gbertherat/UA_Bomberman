package client.thread;

import client.Client;
import client.view.ViewBombermanGame;
import utils.AgentAction;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientWriter extends Thread {
    private final PrintWriter writer;
    private final ViewBombermanGame view;
    private boolean exit;

    public ClientWriter(Socket socket, ViewBombermanGame view) throws IOException {
        writer = new PrintWriter(socket.getOutputStream(), true);
        this.view = view;
        this.exit = false;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public boolean getExit() {
        return exit;
    }

    @Override
    public void run() {
        while (!exit) {
            writer.println(view.getAction().toString());
            view.setAction(AgentAction.STOP);
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        writer.println("EXIT");
    }
}

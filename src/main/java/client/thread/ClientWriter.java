package client.thread;

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
            writer.println(view.getAction().toString()); // On récupère l'action choisie par l'utilisateur
            view.setAction(AgentAction.STOP); // Après que l'action ait été envoyé, elle est remise à STOP
            try {
                Thread.sleep(400);
                // Le temps de sleep ici est plutôt élevé pour avoir le temps de réfléchir à son action, le temps est
                // le même côté serveur pour que les bots ne jouent pas instantanément et que ce soit injouable.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        writer.println("EXIT");
    }
}

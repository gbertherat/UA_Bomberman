package client.thread;

import client.Client;
import client.JsonClient;
import client.view.ViewBombermanGame;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReader extends Thread {
    private final Socket socket;
    private final Client client;
    private final BufferedReader reader;
    private final ViewBombermanGame view;
    private boolean exit;
    private String exitCause;

    public ClientReader(Socket socket, Client client, ViewBombermanGame view) throws IOException {
        this.socket = socket;
        this.client = client;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
        // Réception du JSON
        JSONParser parser = new JSONParser();
        try {
            while (!socket.isClosed() && !exit) {
                String line;
                if ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    if(line.contains("EXIT")){ // Si le client ferme sa fenêtre, on envoie "EXIT" au serveur.
                        exit = true;
                        exitCause = line.split(":")[1];
                    } else {
                        JSONObject jsonObj = (JSONObject) parser.parse(line);
                        if (jsonObj.get("status") != null && jsonObj.get("status").equals("OK")) {
                            JsonClient jClient = new JsonClient(view, jsonObj);
                            // Même principe que le JsonServer côté serveur, la classe JsonClient permet ici de récupére
                            // le JSON contenant les données de la partie et de mettre à jour l'affichage des clients
                            jClient.updateView(); // Mise à jour de la vue
                        }
                    }
                }
                Thread.sleep(100);
                // Le temps de sleep est bas pour mettre à jour la vue en permanence pour éviter les décalages d'affichages
                // entre les clients
            }
        } catch (IOException | InterruptedException e) {
            exit = true; // En cas d'erreur, on déconnecte le client
        } catch (ParseException e) {
            System.out.println("ClientReader Parse error (run): ");
            e.printStackTrace();
        } finally {
            System.out.println("Disconnecting");
            client.disconnect(exitCause);
        }
    }
}

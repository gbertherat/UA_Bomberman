package client.thread;

import client.Client;
import client.JsonClient;
import client.view.PanelBomberman;
import client.view.ViewBombermanGame;
import org.json.simple.JSONArray;
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

    public ClientReader(Socket socket, Client client, ViewBombermanGame view) throws IOException {
        this.socket = socket;
        this.client = client;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.view = view;
    }

    @Override
    public void run() {
        // Réception du JSON
        try {
            JSONParser parser = new JSONParser();

            do {
                String line;
                if((line = reader.readLine()) != null) {
                    JSONObject jsonObj = (JSONObject) parser.parse(line);
                    if (jsonObj.get("status") != null && jsonObj.get("status").equals("OK")) {
                        JsonClient jClient = new JsonClient(view, jsonObj);
                        jClient.updateView();
                    }
                }
                Thread.sleep(1000);
            } while (!socket.isClosed());

        } catch (IOException | InterruptedException e) {
            System.out.println("ClientReader IO error (run): " + e.getMessage());
        } catch (ParseException e) {
            System.out.println("ClientReader Parse error (run): " + e.getMessage());
        }
    }
}

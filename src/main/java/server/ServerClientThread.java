package server;

import model.BombermanGame;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ServerClientThread extends Thread {
    private int clientId;
    private final Socket socket;
    private final Server server;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private final BombermanGame game;

    public ServerClientThread(Socket socket, Server server, BombermanGame game) throws IOException {
        this.clientId = -1;
        this.game = game;
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
            // On commence par vérifier les identifiants du client qui se connecte grâce à un appel à l'API
            // La réponse de l'API est communiqué au client
            String line = null;
            while((line = reader.readLine()) == null){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            CloseableHttpClient client = HttpClients.createDefault();
            String[] parameters = line.split("&");
            String username = parameters[0].split("=")[1];
            String password = parameters[1].split("=")[1];

            HttpPost request = new HttpPost("http://127.0.0.1:8080/BombermanJEE/user/login");

            ArrayList<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("login", username));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("token", "5634964c-5cf4-4c68-a5b7-745955e873ea"));
            request.setEntity(new UrlEncodedFormEntity(params));

            try (CloseableHttpResponse response = client.execute(request)) {
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    String result = EntityUtils.toString(entity).trim();
                    if(!result.equals("null") && !result.equals("")){
                        int id = Integer.parseInt(result);

                        for(ServerClientThread clients : server.getClients()){
                            if(clients.getClientId() == id){
                                System.out.println("Connection refused : Already connected.");
                                writer.println("-1"); // Le client est déjà connecté, erreur -1
                                return;
                            }
                        }

                        // On envoie l'id du client au client en cas de connexion réussie
                        clientId = id;
                        writer.println(id);
                        server.getClients().add(this);
                    } else {
                        System.out.println("Connection refused : Wrong login / password.");
                        writer.println("-2"); // Mauvais identifiants, erreur -2
                        return;
                    }
                }
            }

            JsonServer jsonServer = new JsonServer(this.server, game);
            // La classe JsonServer n'est pas un serveur mais elle permet de transformer les données de la partie en
            // format JSON pour qu'ils soient envoyés à chaque clients
            // Exemple de données envoyés:
            // {
            //  "walls":[],
            //  "players":[
            //      {
            //          "id":5,                 "x":19,     "y":1,
            //          "type":"B",             "color":"DEFAULT",
            //          "action":"STOP",        "isAlive":"true",
            //          "isActive":"true",      "canFly":"false",
            //          "bombRange":1,          "isInvincible":"false",
            //          "isSick":"false"
            //      }
            //   ],
            //  "bombs":[],
            //  "message":"Vous êtes connecté au server, en attente du début de la partie.",
            //  "map":"niveau2.lay",
            //  "items":[],
            //  "status":"OK"
            //  }
            while (!socket.isClosed()){
                String obj = reader.readLine();
                if(obj != null) {
                    if (obj.equals("EXIT")) { // Si le client envoie EXIT, il est déconnecté
                        socket.close();
                    } else if (obj.chars().allMatch(Character::isDigit)) {
                        this.clientId = Integer.parseInt(obj);
                    } else {
                        String json = jsonServer.sendJson(clientId, obj); // On récupère le json de la partie

                        if (json != null) {
                            writer.println(json); // On envoie le json au client
                        }

                        Thread.sleep(100);
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            server.removeClient(this); // En cas d'erreur le client est déconnecté.
        }
    }
}

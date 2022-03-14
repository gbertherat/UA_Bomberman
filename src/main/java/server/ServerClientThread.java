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
                                writer.println("-1");
                                return;
                            }
                        }

                        clientId = id;
                        writer.println(id);
                        server.getClients().add(this);
                    } else {
                        System.out.println("Connection refused : Wrong login / password.");
                        writer.println("-2");
                        return;
                    }
                }
            }

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
}

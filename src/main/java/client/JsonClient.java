package client;

import client.view.PanelBomberman;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JsonClient {
    private String data;
    private PanelBomberman panel;

    public JsonClient(PanelBomberman panel){
        this.panel = panel;
    }

    // RECUPERER LES INFOS EN PROVENANCE DU SERVEUR
    public void retrieveWalls() {

    }

    public void retrievePlayers() {
    }

    public void retrieveBombs() {
    }

    public void retrieveItems() {
    }

    // ENVOYER LES INFOS AU SERVEUR


}

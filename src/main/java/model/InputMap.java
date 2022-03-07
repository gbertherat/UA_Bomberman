package model;

import utils.AgentAction;
import utils.ColorAgent;
import utils.InfoAgent;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;


/**
 * Classe qui permet de charger une carte de Bomberman à partir d'un fichier de layout d'extension .lay
 */

public class InputMap implements Serializable {
    private static final long serialVersionUID = 1L;
    private String filename;

    private boolean[][] walls;
    private boolean[][] start_breakable_walls;

    private ArrayList<InfoAgent> start_agents;

    public InputMap(String filename) {
        this.filename = filename;

        URL url = getClass().getClassLoader().getResource("layouts/" + filename);
        assert url != null;

        try {
            InputStream flux = new FileInputStream(url.toURI().getPath());
            InputStreamReader lecture = new InputStreamReader(flux);
            BufferedReader buffer = new BufferedReader(lecture);

            String ligne;

            int nbX = 0;
            int nbY = 0;

            while ((ligne = buffer.readLine()) != null) {
                ligne = ligne.trim();
                if (nbX == 0) {
                    nbX = ligne.length();
                } else if (nbX != ligne.length())
                    throw new Exception("Toutes les lignes doivent avoir la même longueur");
                nbY++;
            }
            buffer.close();

            int size_x = nbX;
            int size_y = nbY;

            walls = new boolean[size_x][size_y];
            start_breakable_walls = new boolean[size_x][size_y];
            start_agents = new ArrayList<>();

            flux = new FileInputStream(url.toURI().getPath());
            lecture = new InputStreamReader(flux);
            buffer = new BufferedReader(lecture);
            int y = 0;

            while ((ligne = buffer.readLine()) != null) {
                ligne = ligne.trim();

                for (int x = 0; x < ligne.length(); x++) {
                    walls[x][y] = ligne.charAt(x) == '%';
                    start_breakable_walls[x][y] = ligne.charAt(x) == '$';
                }
                y++;
            }
            buffer.close();

            //On verifie que le labyrinthe est clos
            for (int x = 0; x < size_x; x++)
                if (!walls[x][0]) throw new Exception("Mauvais format du fichier: la carte doit etre close");
            for (int x = 0; x < size_x; x++)
                if (!walls[x][size_y - 1]) throw new Exception("Mauvais format du fichier: la carte doit etre close");
            for (y = 0; y < size_y; y++)
                if (!walls[0][y]) throw new Exception("Mauvais format du fichier: la carte doit etre close");
            for (y = 0; y < size_y; y++)
                if (!walls[size_x - 1][y]) throw new Exception("Mauvais format du fichier: la carte doit etre close");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFilename() {
        return filename;
    }

    public boolean[][] getStart_breakable_walls() {
        return start_breakable_walls;
    }

    public boolean[][] get_walls() {
        return walls;
    }

    public ArrayList<InfoAgent> getStart_agents() {
        return start_agents;
    }

    public void addStart_agent(InfoAgent agent){
        start_agents.add(agent);
    }


}
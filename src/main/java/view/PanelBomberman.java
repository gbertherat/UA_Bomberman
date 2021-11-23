package view;

import utils.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;


/**
 * Classe qui permet de charger d'afficher le panneau du jeu à partir d'une carte et de listes d'agents avec leurs positions.
 */


public class PanelBomberman extends JPanel {

    private static final long serialVersionUID = 1L;
    protected Color wallColor = Color.GRAY;
    protected Color brokable_walls_Color = Color.lightGray;
    protected Color ground_Color = new Color(50, 150, 50);

    private int sizeX;
    private int sizeY;

    private int fen_x;
    private int fen_y;

    private double stepx;
    private double stepy;


    float[] contraste = {0, 0, 0, 1.0f};
    float[] invincible = {200, 200, 200, 1.0f};
    float[] skull = {0.5f, 0.5f, 0.5f, 0.75f};


    protected ArrayList<InfoAgent> listInfoAgents;
    protected ArrayList<InfoItem> listInfoItems;
    protected ArrayList<InfoBomb> listInfoBombs;

    private boolean breakable_walls[][];

    private boolean[][] walls;

    int cpt;

    public PanelBomberman(int sizeX, int sizeY, boolean[][] walls, boolean[][] breakable_walls, ArrayList<InfoAgent> listInfoAgents) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        this.walls = walls;

        this.listInfoAgents = listInfoAgents;
        this.breakable_walls = breakable_walls;

        this.listInfoItems = new ArrayList<>();
        this.listInfoBombs = new ArrayList<>();
    }

    public void paint(Graphics g) {
        fen_x = getSize().width;
        fen_y = getSize().height;

        this.stepx = fen_x / (double) sizeX;
        this.stepy = fen_y / (double) sizeY;

        g.setColor(ground_Color);
        g.fillRect(0, 0, fen_x, fen_y);

        double position_x = 0;

        for (int x = 0; x < sizeX; x++) {
            double position_y = 0;

            for (int y = 0; y < sizeY; y++) {
                if (walls[x][y]) {
                    try {
                        Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource("../images/wall.png")));
                        g.drawImage(img, (int) position_x, (int) position_y, (int) stepx, (int) stepy, this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (this.breakable_walls[x][y]) {
                    try {
                        Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource("../images/brique_2.png")));
                        g.drawImage(img, (int) position_x, (int) position_y, (int) stepx, (int) stepy, this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource("../images/grass.png")));
                        g.drawImage(img, (int) position_x, (int) position_y, (int) stepx, (int) stepy, this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                position_y += stepy;
            }
            position_x += stepx;
        }

        for (InfoItem listInfoItem : listInfoItems) {
            dessine_Items(g, listInfoItem);
        }

        for (InfoBomb listInfoBomb : listInfoBombs) {
            dessine_Bomb(g, listInfoBomb);
        }

        for (InfoAgent listInfoAgent : listInfoAgents) {
            dessine_Agent(g, listInfoAgent);
        }

        cpt++;
    }


    void dessine_Agent(Graphics g, InfoAgent infoAgent) {
        int px = infoAgent.getX();
        int py = infoAgent.getY();

        double pos_x = px * stepx;
        double pos_y = py * stepy;

        AgentAction agentAction = infoAgent.getAgentAction();

        int direction = 0;

        if (agentAction == AgentAction.MOVE_UP) {
            direction = 0;
        } else if (agentAction == AgentAction.MOVE_DOWN) {
            direction = 1;
        } else if (agentAction == AgentAction.MOVE_RIGHT) {
            direction = 2;
        } else if (agentAction == AgentAction.MOVE_LEFT) {
            direction = 3;
        } else {
            direction = 4;
        }

        BufferedImage img = null;

        try {
            if (infoAgent.getType() == 'R') {
                img = ImageIO.read(Objects.requireNonNull(getClass().getResource("../images/" + infoAgent.getType() + direction + this.cpt % 2 + ".png")));
            } else {
                img = ImageIO.read(Objects.requireNonNull(getClass().getResource("../images/" + infoAgent.getType() + direction + this.cpt % 3 + ".png")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        float[] scales = new float[]{1, 1, 1, 1.0f};

        if (infoAgent.getColor() != null) {
            switch (infoAgent.getColor()) {
                case ROUGE:
                    scales = new float[]{3, 0.75f, 0.75f, 1.0f};
                    break;
                case VERT:
                    scales = new float[]{0.75f, 3, 0.75f, 1.0f};
                    break;
                case BLEU:
                    scales = new float[]{0.75f, 0.75f, 3, 1.0f};
                    break;
                case JAUNE:
                    scales = new float[]{3, 3, 0.75f, 1.0f};
                    break;
                case BLANC:
                    scales = new float[]{2, 2, 2, 1.0f};
                    break;
                case DEFAULT:
                    scales = new float[]{1, 1, 1, 1.0f};
                    break;
            }
        }

        if (infoAgent.isInvincible())
            contraste = invincible;
        else contraste = new float[]{0, 0, 0, 1.0f};

        if (infoAgent.isSick())
            scales = skull;


        RescaleOp op = new RescaleOp(scales, contraste, null);
        img = op.filter(img, null);


        if (img != null) {
            g.drawImage(img, (int) pos_x, (int) pos_y, (int) stepx, (int) stepy, this);
        }
    }


    void dessine_Items(Graphics g, InfoItem item) {
        int px = item.getX();
        int py = item.getY();

        double pos_x = px * stepx;
        double pos_y = py * stepy;

        if (item.getType() == ItemType.FIRE_UP) {
            try {
                Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource("../images/Item_FireUp.png")));
                g.drawImage(img, (int) pos_x, (int) pos_y, (int) stepx, (int) stepy, this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (item.getType() == ItemType.FIRE_DOWN) {
            try {
                Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource("../images/Item_FireDown.png")));
                g.drawImage(img, (int) pos_x, (int) pos_y, (int) stepx, (int) stepy, this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (item.getType() == ItemType.FIRE_SUIT) {
            try {
                Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource("../images/Item_FireSuit.png")));
                g.drawImage(img, (int) pos_x, (int) pos_y, (int) stepx, (int) stepy, this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (item.getType() == ItemType.SKULL) {
            try {
                Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource("../images/Item_Skull.png")));
                g.drawImage(img, (int) pos_x, (int) pos_y, (int) stepx, (int) stepy, this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    void dessine_Bomb(Graphics g, InfoBomb bomb) {
        int px = bomb.getX();
        int py = bomb.getY();

        double pos_x = px * stepx;
        double pos_y = py * stepy;

        if (bomb.getStateBomb() == StateBomb.Step0) {

            try {
                Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource("../images/Bomb_0.png")));
                g.drawImage(img, (int) pos_x, (int) pos_y, (int) stepx, (int) stepy, this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (bomb.getStateBomb() == StateBomb.Step1) {

            try {
                Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource("../images/Bomb_0.png")));
                g.drawImage(img, (int) pos_x, (int) pos_y, (int) stepx, (int) stepy, this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (bomb.getStateBomb() == StateBomb.Step2) {

            try {
                Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource("../images/Bomb_1_jaune.png")));
                g.drawImage(img, (int) pos_x, (int) pos_y, (int) stepx, (int) stepy, this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (bomb.getStateBomb() == StateBomb.Step3) {

            try {
                Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource("../images/Bomb_2_rouge.png")));
                g.drawImage(img, (int) pos_x, (int) pos_y, (int) stepx, (int) stepy, this);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (bomb.getStateBomb() == StateBomb.Boom) {

            try {
                Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource("../images/Range_CENTRE.png")));
                g.drawImage(img, (int) pos_x, (int) pos_y, (int) stepx, (int) stepy, this);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int range = bomb.getRange();
            range = bomb.getRange_wall_at(1);

            for (int i = 1; i <= range; i++) {


                if (py + i < sizeY) {
                    if (i == range) {
                        try {
                            Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource("../images/Range_SOUTH_Fin.png")));
                            g.drawImage(img, (int) pos_x, (int) (pos_y + (stepy * i)), (int) stepx, (int) stepy, this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource("../images/Range_SOUTH.png")));
                            g.drawImage(img, (int) pos_x, (int) (pos_y + (stepy * i)), (int) stepx, (int) stepy, this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            range = bomb.getRange_wall_at(3);

            for (int i = 1; i <= range; i++) {


                if (py - i >= 0) {
                    if (i == range) {
                        try {
                            Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource("../images/Range_NORTH_Fin.png")));
                            g.drawImage(img, (int) pos_x, (int) (pos_y - (stepy * i)), (int) stepx, (int) stepy, this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource("../images/Range_NORTH.png")));
                            g.drawImage(img, (int) pos_x, (int) (pos_y - (stepy * i)), (int) stepx, (int) stepy, this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            range = bomb.getRange_wall_at(0);

            for (int i = 1; i <= range; i++) {

                if (px + i < sizeX) {
                    if (i == range) {
                        try {
                            Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource("../images/Range_EAST_Fin.png")));
                            g.drawImage(img, (int) (pos_x + (stepy * i)), (int) (pos_y), (int) stepx, (int) stepy, this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource("../images/Range_EAST.png")));
                            g.drawImage(img, (int) (pos_x + (stepy * i)), (int) (pos_y), (int) stepx, (int) stepy, this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            range = bomb.getRange_wall_at(2);

            for (int i = 1; i <= range; i++) {

                if (px - i >= 0) {
                    if (i == range) {
                        try {
                            Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource("../images/Range_WEST_Fin.png")));
                            g.drawImage(img, (int) (pos_x - (stepy * i)), (int) (pos_y), (int) stepx, (int) stepy, this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource("../images/Range_WEST.png")));
                            g.drawImage(img, (int) (pos_x - (stepy * i)), (int) (pos_y), (int) stepx, (int) stepy, this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }


        }

    }


    public void updateInfoGame(boolean[][] breakable_walls, ArrayList<InfoAgent> listInfoAgents, ArrayList<InfoItem> listInfoItems, ArrayList<InfoBomb> listInfoBombs) {
        this.listInfoAgents = listInfoAgents;
        this.listInfoItems = listInfoItems;
        this.listInfoBombs = listInfoBombs;
        this.breakable_walls = breakable_walls;
    }


    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }
}

package controller;

import model.BombermanGame;
import model.InputMap;
import view.PanelBomberman;
import view.ViewBombermanGame;
import view.ViewCommand;
import view.strategy.EtatCreated;

public class ControllerBombermanGame extends AbstractController{
    private BombermanGame bg;
    private PanelBomberman pb;
    private ViewBombermanGame vbg;
    private ViewCommand vc;
    private InputMap map;

    public ControllerBombermanGame(String mapName){
        map = new InputMap(mapName + ".lay");

        bg = new BombermanGame(128,1000, map);
        vbg = new ViewBombermanGame();
        vc = new ViewCommand(this);

        bg.addObserver(vbg);
        bg.addObserver(vc);
        bg.init();

        vbg.setMap(map);
        vbg.init(map.get_walls().length*64,map.get_walls()[0].length*64,-350);
        vc.init(700,300,350);
        vc.setEtat(new EtatCreated(vc));
    }

    public void changeMap(InputMap map){
        this.map = map;
    }

    @Override
    public void restart() {
        bg.restart();
        bg.init();
        vbg.restart();
    }

    @Override
    public void step() {
        bg.step();
    }

    @Override
    public void play() {
        bg.resume();
    }

    @Override
    public void pause() {
        bg.pause();
    }
}

package controller;

import model.BombermanGame;
import model.InputMap;
import view.ViewBombermanGame;
import view.ViewCommand;
import view.strategy.EtatCreated;

public class ControllerBombermanGame extends AbstractController{
    private BombermanGame bg;
    private ViewBombermanGame vbg;
    private ViewCommand vc;
    private InputMap map;

    public ControllerBombermanGame(String mapName){
        map = new InputMap(mapName + ".lay");

        bg = new BombermanGame(1024,500, map);
        vbg = new ViewBombermanGame();
        vc = new ViewCommand(this);

        bg.addObserver(vbg);
        bg.addObserver(vc);
        bg.init();

        vbg.setMap(map);
        vbg.init(map.get_walls().length*48,map.get_walls()[0].length*48,-100);
        vc.init(600,300,300);
        vc.setEtat(new EtatCreated(vc));
    }

    public void changeMap(InputMap map){
        this.map = map;
    }

    @Override
    public void restart() {
        bg.restart();
        vbg.restart();
    }

    @Override
    public void step() {
        bg.step();
    }

    @Override
    public void play() {
        bg.launch();
    }

    @Override
    public void pause() {
        bg.pause();
    }

    @Override
    public void setSpeed(int speed) {
        bg.setTimeMs(speed * 1000);
    }
}

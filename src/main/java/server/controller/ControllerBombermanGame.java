package server.controller;

import model.BombermanGame;
import model.InputMap;
import utils.AgentAction;
import client.view.ViewBombermanGame;
import client.view.ViewCommand;
import client.view.etat.EtatCreated;

public class ControllerBombermanGame extends AbstractController{
    private final BombermanGame bg;
    private final ViewBombermanGame vbg;
    private final ViewCommand vc;
    private InputMap map;

    public ControllerBombermanGame(String mapName){
        map = new InputMap(mapName);

        bg = new BombermanGame(1024,DefaultSpeed.value, map);
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

    public void changeMap(String mapPath){
        this.map = new InputMap(mapPath);
        this.bg.changeMap(this.map);
        this.bg.init();
        this.vbg.close();
        this.vbg.setMap(map);
        this.vbg.init(map.get_walls().length*48,map.get_walls()[0].length*48,-100);
        vc.setEtat(new EtatCreated(vc));
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
        bg.setTimeMs((speed+1) * DefaultSpeed.value);
    }

    public void setBombermanAction(AgentAction action){
        this.bg.setBombermanAction(action);
    }

    public void setDifficulty(int difficulty){
        this.bg.setDifficulty(difficulty);
    }
}

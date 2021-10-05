package controller;

import model.BombermanGame;
import model.InputMap;
import view.ViewBombermanGame;
import view.ViewCommand;

public class ControllerBombermanGame extends AbstractController{
    private BombermanGame bg;
    private ViewBombermanGame vbg;
    private ViewCommand vc;

    public ControllerBombermanGame(){
        InputMap map = new InputMap("arene.lay");

        vbg = new ViewBombermanGame();
        vc = new ViewCommand(this);

        bg = new BombermanGame(32,1000);
        bg.addObserver(vbg);
        bg.addObserver(vc);

        vbg.init(700,700,-350);
        vc.init(700,300,350);
    }

    @Override
    public void restart() {

    }

    @Override
    public void step() {

    }

    @Override
    public void play() {

    }

    @Override
    public void pause() {

    }
}

package controller;

import model.Game;
import model.SimpleGame;
import view.*;
import view.strategy.EtatCreated;

public class ControllerSimpleGame extends AbstractController {
    private Game game;
    private ViewSimpleGame vsg;
    private ViewCommand vc;

    public ControllerSimpleGame() {
        super();
        game = new SimpleGame(10, 500);
        vsg = new ViewSimpleGame();
        vc = new ViewCommand(this);
        game.addObserver(vsg);
        game.addObserver(vc);
        game.init();

        vsg.init(700, 700, -350);
        vc.init(700, 300, 350);
        vc.setEtat(new EtatCreated(vc));
    }

    @Override
    public void restart() {
        game.restart();
    }

    @Override
    public void step() {
        game.step();
    }

    @Override
    public void play() {
        game.launch();
    }

    @Override
    public void pause() {
        game.pause();
    }
}

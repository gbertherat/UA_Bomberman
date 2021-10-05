package controller;

import model.Game;

public abstract class AbstractController {
    private Game game;

    public abstract void restart();
    public abstract void step();
    public abstract void play();
    public abstract void pause();

    public void setSpeed(int speed){
        game.setTimeMs(speed * 1000);
    }
}

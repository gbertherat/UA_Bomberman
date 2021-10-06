package controller;

public abstract class AbstractController {
    public abstract void restart();
    public abstract void step();
    public abstract void play();
    public abstract void pause();
    public abstract void setSpeed(int speed);
}

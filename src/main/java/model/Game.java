package model;

import java.util.Observable;

public abstract class Game extends Observable implements Runnable {
    private int turn;
    private int maxturn;
    private int timeMs;
    private boolean isRunning;
    private Thread thread;

    public Game(int maxTurn, int timeMs) {
        this.maxturn = maxTurn;
        this.timeMs = timeMs;
    }

    public int getTurn(){
        return turn;
    }

    public int getMaxturn(){
        return maxturn;
    }

    public void setTimeMs(int time){
        this.timeMs = time;
    }

    public void init(){
        turn = 0;
    }

    public void step(){
        if(gameContinue()){
            turn++;
            takeTurn();
        } else {
            isRunning = false;
            gameOver();
        }
        setChanged();
        notifyObservers(turn);
    }

    public void pause(){
        isRunning = false;
    }

    public void resume(){
        isRunning = true;
    }

    public void restart(){
        isRunning = false;
        turn = 0;
    }

    public void run(){
        while(isRunning){
            step();
            try {
                Thread.sleep(timeMs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void launch(){
        this.isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    abstract void takeTurn();
    abstract boolean gameContinue();
    abstract void gameOver();
}

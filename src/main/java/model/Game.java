package model;

import java.util.Observable;

public abstract class Game extends Observable implements Runnable {
    private int turn;
    private int maxturn;
    private int timeMs;
    private boolean isRunning;
    private boolean isFinished;
    private String gameOverReason;
    private Thread thread;

    public Game(int maxTurn, int timeMs) {
        this.maxturn = maxTurn;
        this.timeMs = timeMs;
        this.isFinished = false;
    }

    public int getTurn(){
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getMaxturn(){
        return maxturn;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public String getGameOverReason() {
        return gameOverReason;
    }

    public void setGameOverReason(String gameOverReason) {
        this.gameOverReason = gameOverReason;
    }

    public void setTimeMs(int time){
        this.timeMs = time;
    }

    public void init(){
        turn = 1;
    }

    public void step(){
        if(gameContinue()){
            turn++;
            takeTurn();
        } else if(isFinished){
            isRunning = false;
        } else {
            gameOver("Turn limit reached!");
            isRunning = false;
        }
        setChanged();
        notifyObservers(this);
    }

    public void pause(){
        isRunning = false;
    }

    public void resume(){
        isRunning = true;
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

    public void restart(){
        this.isFinished = false;
        this.isRunning = false;
        setTurn(1);
    }

    abstract void takeTurn();
    abstract boolean gameContinue();
    abstract void gameOver(String reason);
}

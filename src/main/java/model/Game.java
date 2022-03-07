package model;

public abstract class Game {
    private int turn;
    private int maxturn;
    private boolean isFinished;
    private String gameOverReason;

    public Game(int maxTurn) {
        this.maxturn = maxTurn;
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

    public void init(){
        turn = 1;
    }

    public void restart(){
        this.isFinished = false;
        setTurn(1);
    }

    abstract void takeTurn();
    abstract boolean gameContinue();
    abstract void gameOver(String reason);
}

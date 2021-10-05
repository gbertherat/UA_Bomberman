package model;

import java.util.Observable;

public class BombermanGame extends Game {
    public BombermanGame(int maxTurn, int timeMs){
        super(maxTurn, timeMs);
    }

    @Override
    public void takeTurn() {

    }

    @Override
    public boolean gameContinue() {
        return getTurn() < getMaxturn();
    }

    @Override
    public void gameOver() {

    }
}

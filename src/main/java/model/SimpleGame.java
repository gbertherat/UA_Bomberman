package model;

public class SimpleGame extends Game{
    public SimpleGame(int maxTurn, int timeMs){
        super(maxTurn, timeMs);
    }

    @Override
    void takeTurn() {
        System.out.println("Tour " + getTurn() + " du jeu en cours");
    }

    @Override
    boolean gameContinue() {
        return getTurn() < getMaxturn();
    }

    @Override
    void gameOver(String reason) {
        System.out.println("Le jeu est terminé!\n" + reason);
    }
}

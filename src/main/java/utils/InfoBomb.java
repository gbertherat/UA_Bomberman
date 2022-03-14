package utils;

import java.util.Arrays;

public class InfoBomb {

    private int x;
    private int y;
    private int range;
    private final int[] range_wall;


    StateBomb stateBomb;

    public InfoBomb(int x, int y, int range, StateBomb stateBomb) {
        this.x = x;
        this.y = y;
        this.range = range;
        this.range_wall = new int[4];
        for (int i = 0; i < 4; i++) {
            range_wall[i] = range;
        }
        this.stateBomb = stateBomb;
    }


    public int getRange_wall_at(int index) {
        return range_wall[index];
    }


    public void setRange_wall_at(int index, int r) {
        this.range_wall[index] = r;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public StateBomb getStateBomb() {
        return stateBomb;
    }

    public void setStateBomb(StateBomb stateBomb) {
        this.stateBomb = stateBomb;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    @Override
    public String toString() {
        return "{" +
                "\"x\":" + x + "," +
                "\"y\":" + y + "," +
                "\"range\":" + range + "," +
                "\"state\":\"" + stateBomb + "\"," +
                "},";
    }
}
	
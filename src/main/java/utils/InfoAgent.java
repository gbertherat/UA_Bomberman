package utils;

public class InfoAgent {
    private int id;
    private int x;
    private int y;
    private AgentAction agentAction;
    private ColorAgent color;
    private char type;

    private boolean isAlive;
    private boolean isActive;
    private boolean canFly;

    private int bombRange;
    private boolean isInvincible;
    private int turnUntilNotInvincible;

    private boolean isSick;
    private int turnUntilNotSick;

    public InfoAgent(int id, int x, int y, AgentAction agentAction, char type, ColorAgent color, boolean canFly, boolean isActive, boolean isInvincible, boolean isSick) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.agentAction = agentAction;
        this.color = color;
        this.type = type;

        this.isAlive = true;
        this.isActive = isActive;
        this.canFly = canFly;

        this.bombRange = 1;
        this.isInvincible = isInvincible;
        this.turnUntilNotInvincible = 10;

        this.isSick = isSick;
        this.turnUntilNotSick = 10;
    }

    public int getId() {
        return id;
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

    public ColorAgent getColor() {
        return color;
    }

    public void setColor(ColorAgent color) {
        this.color = color;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public int getBombRange() {
        return bombRange;
    }

    public void setBombRange(int bombRange) {
        this.bombRange = bombRange;
    }

    public void increaseBombRangeBy(int range) {
        this.bombRange += range;
    }

    public void decreaseBombRange(int range) {
        this.bombRange -= range;
        if (bombRange < 1) {
            bombRange = 1;
        }
    }

    public boolean isInvincible() {
        return isInvincible;
    }

    public void setInvincible(boolean isInvincible) {
        this.isInvincible = isInvincible;
    }

    public int getTurnUntilNotInvincible() {
        return turnUntilNotInvincible;
    }

    public void setTurnUntilNotInvincible(int turnUntilNotInvincible) {
        this.turnUntilNotInvincible = turnUntilNotInvincible;
    }

    public void decreaseTurnUntilNotInvincible(int turn) {
        this.turnUntilNotInvincible -= turn;
        if (this.turnUntilNotInvincible <= 0) {
            this.turnUntilNotInvincible = 0;
            setInvincible(false);
        }
    }

    public boolean isSick() {
        return isSick;
    }

    public void setSick(boolean isSick) {
        this.isSick = isSick;
    }

    public int getTurnUntilNotSick() {
        return turnUntilNotSick;
    }

    public void setTurnUntilNotSick(int turnUntilNotSick) {
        this.turnUntilNotSick = turnUntilNotSick;
    }

    public void decreaseTurnUntilNotSick(int turn) {
        this.turnUntilNotSick -= turn;
        if (this.turnUntilNotSick <= 0) {
            this.turnUntilNotSick = 0;
            setSick(false);
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean getCanFly() {
        return canFly;
    }

    public AgentAction getAgentAction() {
        return agentAction;
    }

    public void setAgentAction(AgentAction agentAction) {
        this.agentAction = agentAction;
    }

    @Override
    public String toString() {
        return  "{" +
                "\"id\":" + id + "," +
                "\"x\":" + x + "," +
                "\"y\":" + y + "," +
                "\"type\":\"" + type + "\"," +
                "\"action\":\"" + agentAction + "\"," +
                "\"isAlive\":\"" + isAlive + "\"," +
                "\"isActive\":\"" + isActive + "\"," +
                "\"canFly\":\"" + canFly + "\"," +
                "\"bombRange\":" + bombRange + "," +
                "\"isInvincible\":\"" + isInvincible + "\"," +
                "\"isSick\":\"" + isSick +"\"" +
                "}";
    }
}
	
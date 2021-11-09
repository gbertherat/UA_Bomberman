package utils;

public class InfoAgent {

    private int x;
    private int y;
    private AgentAction agentAction;
    private ColorAgent color;
    private char type;

    private boolean isAlive;
    private boolean isActive;

    private int bombRange;
    private boolean isInvincible;
    private int turnUntilNotInvincible;

    private boolean isSick;
    private int turnUntilNotSick;

    public InfoAgent(int x, int y, AgentAction agentAction, char type, ColorAgent color, boolean isInvincible, boolean isSick) {
        this.x = x;
        this.y = y;
        this.agentAction = agentAction;
        this.color = color;
        this.type = type;

        this.isAlive = true;
        this.isActive = true;

        this.bombRange = 10;
        this.isInvincible = isInvincible;
        this.turnUntilNotInvincible = 10;

        this.isSick = isSick;
        this.turnUntilNotSick = 10;
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

    public AgentAction getAgentAction() {
        return agentAction;
    }

    public void setAgentAction(AgentAction agentAction) {
        this.agentAction = agentAction;
    }


}
	
package pathfinding;

public class Node implements Comparable<Node>{
    private final Node parent;
    private final int x;
    private final int y;
    private int f = 0;
    private int g;
    private int h;

    public Node(Node parent, int x, int y, int g, int h) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.g = g;
        this.h = h;
    }

    public Node getParent() {
        return parent;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getF(){
        return g+h;
    }

    public int getG(){
        return g;
    }

    public int getH(){
        return h;
    }

    @Override
    public int compareTo(Node o) {
        return this.getF() - o.getF();
    }

    @Override
    public String toString() {
        return "(" + x + ";" + y + ")";
    }
}

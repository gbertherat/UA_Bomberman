package pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AStar {
    private final boolean[][] walls;
    private final int xstart;
    private final int ystart;

    public AStar(boolean[][] walls, int xstart, int ystart){
        this.walls = walls;
        this.xstart = xstart;
        this.ystart = ystart;
    }

    private int getDistanceBetweenCoords(Node cur, int dx, int dy, int xend, int yend){
        return Math.abs(cur.getX() + dx - xend) + Math.abs(cur.getY() + dy - yend);
    }

    private boolean nodeIsInList(Node node, List<Node> list){
        for(Node n : list){
            if(n.getX() == node.getX() && n.getY() == node.getY()){
                return true;
            }
        }
        return false;
    }

    private void addNeighboursToList(Node node, List<Node> open, List<Node> closed, int xend, int yend){
        int x = node.getX();
        int y = node.getY();

        if(x+1 < walls.length && y < walls[x+1].length && !walls[x + 1][y]){
            Node newNode = new Node(node, x+1, y, node.getG(), getDistanceBetweenCoords(node, 1, 0, xend, yend));
            if(!nodeIsInList(newNode, open) && !nodeIsInList(newNode, closed)) {
                open.add(newNode);
            }
        }
        if(x-1 > 0 && y < walls[x-1].length && !walls[x - 1][y]){
            Node newNode = new Node(node, x-1, y, node.getG(), getDistanceBetweenCoords(node, -1, 0, xend, yend));
            if(!nodeIsInList(newNode, open) && !nodeIsInList(newNode, closed)) {
                open.add(newNode);
            }
        }
        if(x < walls.length && y+1 < walls[x].length && !walls[x][y + 1]){
            Node newNode = new Node(node, x, y+1, node.getG(), getDistanceBetweenCoords(node, 0, 1, xend, yend));
            if(!nodeIsInList(newNode, open) && !nodeIsInList(newNode, closed)) {
                open.add(newNode);
            }
        }
        if(x < walls.length && y-1 > 0 && !walls[x][y - 1]){
            Node newNode = new Node(node, x, y-1, node.getG(), getDistanceBetweenCoords(node, 0, -1, xend, yend));
            if(!nodeIsInList(newNode, open) && !nodeIsInList(newNode, closed)) {
                open.add(newNode);
            }
        }

        Collections.sort(open);
    }

    public List<Node> getPathTo(int x, int y){
        List<Node> closed = new ArrayList<>();
        List<Node> open = new ArrayList<>();
        List<Node> path = new ArrayList<>();
        Node curNode = new Node(null, xstart, ystart, 0, 0);
        closed.add(curNode);
        addNeighboursToList(curNode, open, closed, x, y);

        while(curNode.getX() != x || curNode.getY() != y){
            if(open.size() == 0){
                return null;
            }

            curNode = open.get(0);
            open.remove(0);
            closed.add(curNode);
            addNeighboursToList(curNode, open, closed, x, y);
        }
        path.add(0, curNode);
        while(curNode.getX() != xstart || curNode.getY() != ystart){
            curNode = curNode.getParent();
            path.add(0, curNode);
        }
        return path;
    }
}

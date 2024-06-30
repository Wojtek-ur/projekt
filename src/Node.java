import java.awt.*;

public class Node {
    private String name;
    private Point position;
    private boolean visitedBFS;
    private boolean visitedDFS;

    public Node(String name) {
        this.name = name;
    }

    public Node(String name, Point position) {
        this.name = name;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public boolean isVisitedBFS() {
        return visitedBFS;
    }

    public void setVisitedBFS(boolean visitedBFS) {
        this.visitedBFS = visitedBFS;
    }

    public boolean isVisitedDFS() {
        return visitedDFS;
    }

    public void setVisitedDFS(boolean visitedDFS) {
        this.visitedDFS = visitedDFS;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node node = (Node) obj;
        return name.equals(node.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
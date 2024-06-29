import java.awt.*;

public class Node {
    private String name;
    private Point position; // Position for graphical representation, can be null if not needed

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
}

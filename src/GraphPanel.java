import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GraphPanel extends JPanel {
    private Graph graph;
    private Point lastMousePosition;
    private Node selectedNode;

    public GraphPanel(Graph graph) {
        this.graph = graph;
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                lastMousePosition = e.getPoint();
                String nodeName = JOptionPane.showInputDialog(GraphPanel.this, "Enter node name:");
                if (nodeName != null && !nodeName.isEmpty()) {
                    graph.addNode(new Node(nodeName, lastMousePosition));
                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                lastMousePosition = e.getPoint();
                for (Node node : graph.getNodes()) {
                    Point pos = node.getPosition();
                    if (pos != null && pos.x - 10 <= lastMousePosition.x && lastMousePosition.x <= pos.x + 10 &&
                            pos.y - 10 <= lastMousePosition.y && lastMousePosition.y <= pos.y + 10) {
                        selectedNode = node;
                        break;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                selectedNode = null;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedNode != null) {
                    selectedNode.setPosition(e.getPoint());
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw edges
        for (Edge edge : graph.getEdges()) {
            Point start = edge.getStartNode().getPosition();
            Point end = edge.getEndNode().getPosition();
            if (start != null && end != null) {
                g.setColor(Color.BLACK);
                g.drawLine(start.x, start.y, end.x, end.y);
                if (edge.isDirected()) {
                    drawArrow(g, start.x, start.y, end.x, end.y);
                }
            }
        }

        // Draw nodes
        g.setColor(Color.BLUE);
        for (Node node : graph.getNodes()) {
            Point pos = node.getPosition();
            if (pos != null) {
                g.fillOval(pos.x - 10, pos.y - 10, 20, 20);
                g.drawString(node.getName(), pos.x - 10, pos.y - 15);
            }
        }
    }

    private void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {
        Graphics2D g2 = (Graphics2D) g.create();
        double angle = Math.atan2(y2 - y1, x2 - x1);
        int arrowLength = 10;
        g2.drawLine(x1, y1, x2, y2);
        g2.drawLine(x2, y2, (int) (x2 - arrowLength * Math.cos(angle - Math.PI / 6)),
                (int) (y2 - arrowLength * Math.sin(angle - Math.PI / 6)));
        g2.drawLine(x2, y2, (int) (x2 - arrowLength * Math.cos(angle + Math.PI / 6)),
                (int) (y2 - arrowLength * Math.sin(angle + Math.PI / 6)));
        g2.dispose();
    }
}

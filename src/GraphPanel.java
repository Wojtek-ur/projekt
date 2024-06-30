import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class GraphPanel extends JPanel {
    private Graph graph;
    private Point lastMousePosition;
    private Node selectedNode;
    private Color originalBackgroundColor;

    public GraphPanel(Graph graph) {
        this.graph = graph;
        setBackground(Color.WHITE);
        originalBackgroundColor = getBackground();

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
                    if (pos != null && isMouseWithinNode(pos, lastMousePosition)) {
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

    private boolean isMouseWithinNode(Point nodePosition, Point mousePosition) {
        return Math.abs(mousePosition.x - nodePosition.x) <= 10 && Math.abs(mousePosition.y - nodePosition.y) <= 10;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw nodes
        for (Node node : graph.getNodes()) {
            Point pos = node.getPosition();
            if (pos != null) {
                g.setColor(determineNodeColor(node));
                g.fillOval(pos.x - 10, pos.y - 10, 20, 20);
                g.setColor(Color.BLACK);
                g.drawString(node.getName(), pos.x - 10, pos.y - 15);
            }
        }

        // Draw edges
        for (Edge edge : graph.getEdges()) {
            Point start = edge.getStartNode().getPosition();
            Point end = edge.getEndNode().getPosition();
            if (start != null && end != null) {
                g.setColor(Color.BLACK);
                if (edge.isDirected()) {
                    drawArrow(g, start, end);
                } else {
                    g.drawLine(start.x, start.y, end.x, end.y);
                }
            }
        }
    }

    private void drawArrow(Graphics g, Point start, Point end) {
        Graphics2D g2 = (Graphics2D) g.create();

        double dx = end.x - start.x;
        double dy = end.y - start.y;
        double angle = Math.atan2(dy, dx);
        int len = 10;

        g2.drawLine(start.x, start.y, end.x, end.y);


        Polygon arrowHead = new Polygon();
        arrowHead.addPoint(end.x, end.y);
        arrowHead.addPoint((int) (end.x - len * Math.cos(angle - Math.PI / 6)),
                (int) (end.y - len * Math.sin(angle - Math.PI / 6)));
        arrowHead.addPoint((int) (end.x - len * Math.cos(angle + Math.PI / 6)),
                (int) (end.y - len * Math.sin(angle + Math.PI / 6)));
        g2.fill(arrowHead);

        g2.dispose();
    }

    private Color determineNodeColor(Node node) {
        if (node.isVisitedBFS()) {
            return Color.GREEN;
        } else if (node.isVisitedDFS()) {
            return Color.YELLOW;
        } else {
            return Color.BLUE;
        }
    }

    public void highlightNodes(List<Node> nodes) {
        SwingUtilities.invokeLater(() -> {
            Timer timer = new Timer(1000, null);
            timer.addActionListener(e -> {
                if (!nodes.isEmpty()) {
                    Node node = nodes.remove(0);
                    node.setVisitedBFS(true);
                    repaint();
                } else {
                    timer.stop();
                    resetVisitedStatus();
                    setBackground(originalBackgroundColor);
                }
            });
            timer.start();
        });
    }

    private void resetVisitedStatus() {
        for (Node node : graph.getNodes()) {
            node.setVisitedBFS(false);
            node.setVisitedDFS(false);
        }
    }
}
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class GraphAlgorithmsApp extends JFrame {

    private GraphPanel graphPanel;
    private Graph graph;

    public GraphAlgorithmsApp() {
        setTitle("Graph Algorithms App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        graph = new Graph();
        graphPanel = new GraphPanel(graph);

        getContentPane().add(graphPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addEdgeButton = new JButton("Add Edge");
        JButton deleteEdgeButton = new JButton("Delete Edge");
        JButton removeAllNodesButton = new JButton("Remove All Nodes");
        JButton removeNodeButton = new JButton("Remove Node");
        JButton bfsButton = new JButton("Run BFS");
        JButton dfsButton = new JButton("Run DFS");
        JButton importButton = new JButton("Import Graph");
        JButton exportButton = new JButton("Export Graph");
        JTextArea resultArea = new JTextArea(10, 30);

        addEdgeButton.addActionListener(e -> {
            String startNodeName = JOptionPane.showInputDialog(this, "Enter start node name:");
            String endNodeName = JOptionPane.showInputDialog(this, "Enter end node name:");
            boolean isDirected = JOptionPane.showConfirmDialog(this, "Is it a directed edge?", "Directed Edge",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
            graph.addEdge(startNodeName, endNodeName, isDirected);
            graphPanel.repaint();
        });

        deleteEdgeButton.addActionListener(e -> {
            String startNodeName = JOptionPane.showInputDialog(this, "Enter start node name:");
            String endNodeName = JOptionPane.showInputDialog(this, "Enter end node name:");
            graph.removeEdge(startNodeName, endNodeName);
            graphPanel.repaint();
        });

        removeNodeButton.addActionListener(e -> {
            String nodeName = JOptionPane.showInputDialog(this, "Enter node name:");
            Node node = graph.getNodes().stream()
                    .filter(n -> n.getName().equals(nodeName))
                    .findFirst()
                    .orElse(null);
            if (node != null) {
                graph.removeNode(node);
                graphPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Node not found.");
            }
        });

        removeAllNodesButton.addActionListener(e -> {
            graph.removeAllNodes();
            graphPanel.repaint();
        });

        bfsButton.addActionListener(e -> {
            String startNodeName = JOptionPane.showInputDialog(this, "Enter start node name:");
            Node startNode = graph.getNodes().stream()
                    .filter(n -> n.getName().equals(startNodeName))
                    .findFirst()
                    .orElse(null);
            if (startNode != null) {
                List<Node> visitedNodes = graph.bfs(startNode);
                StringBuilder result = new StringBuilder("BFS Traversal: ");
                for (Node node : visitedNodes) {
                    result.append(node.getName()).append(" ");
                }
                resultArea.setText(result.toString());
            } else {
                JOptionPane.showMessageDialog(this, "Start node not found.");
            }
        });

        dfsButton.addActionListener(e -> {
            String startNodeName = JOptionPane.showInputDialog(this, "Enter start node name:");
            Node startNode = graph.getNodes().stream()
                    .filter(n -> n.getName().equals(startNodeName))
                    .findFirst()
                    .orElse(null);
            if (startNode != null) {
                List<Node> visitedNodes = graph.dfs(startNode);
                StringBuilder result = new StringBuilder("DFS Traversal: ");
                for (Node node : visitedNodes) {
                    result.append(node.getName()).append(" ");
                }
                resultArea.setText(result.toString());
            } else {
                JOptionPane.showMessageDialog(this, "Start node not found.");
            }
        });

        importButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getPath();
                importGraph(filePath);
            }
        });

        exportButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getPath();
                exportGraph(filePath);
            }
        });

        buttonPanel.add(addEdgeButton);
        buttonPanel.add(deleteEdgeButton);
        buttonPanel.add(removeNodeButton);
        buttonPanel.add(removeAllNodesButton);
        buttonPanel.add(bfsButton);
        buttonPanel.add(dfsButton);
        buttonPanel.add(importButton);
        buttonPanel.add(exportButton);

        getContentPane().add(buttonPanel, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(resultArea), BorderLayout.SOUTH);
    }

    private void exportGraph(String filePath) {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            // First, write nodes with their positions
            for (Node node : graph.getNodes()) {
                Point pos = node.getPosition();
                if (pos != null) {
                    writer.println(node.getName() + " " + pos.x + " " + pos.y);
                } else {
                    writer.println(node.getName());
                }
            }

            // Write a separator between nodes and edges
            writer.println("EDGES");

            // Then, write edges
            for (Edge edge : graph.getEdges()) {
                writer.println(edge.getStartNode().getName() + " " + edge.getEndNode().getName() + " " + edge.isDirected());
            }

            JOptionPane.showMessageDialog(this, "Graph exported to " + filePath);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void importGraph(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean readingEdges = false;

            while ((line = reader.readLine()) != null) {
                if (line.trim().equals("EDGES")) {
                    readingEdges = true;
                    continue;
                }

                if (!readingEdges) {
                    // Reading nodes with positions
                    String[] parts = line.trim().split("\\s+");
                    if (parts.length == 3) {
                        String nodeName = parts[0];
                        int x = Integer.parseInt(parts[1]);
                        int y = Integer.parseInt(parts[2]);
                        Node node = findOrCreateNode(nodeName, new Point(x, y));
                    } else if (parts.length == 1) {
                        String nodeName = parts[0];
                        Node node = findOrCreateNode(nodeName, null);
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid format in file: " + filePath, "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    // Reading edges
                    String[] parts = line.trim().split("\\s+");
                    if (parts.length == 3) {
                        String startNodeName = parts[0];
                        String endNodeName = parts[1];
                        boolean isDirected = Boolean.parseBoolean(parts[2]);
                        graph.addEdge(startNodeName, endNodeName, isDirected);
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid format in file: " + filePath, "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }

            JOptionPane.showMessageDialog(this, "Graph imported from " + filePath);
            graphPanel.repaint(); // Repaint the graph panel to reflect changes
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Node findOrCreateNode(String nodeName, Point position) {
        Node existingNode = graph.getNodes().stream()
                .filter(node -> node.getName().equals(nodeName))
                .findFirst()
                .orElse(null);

        if (existingNode == null) {
            existingNode = new Node(nodeName, position); // Use provided position
            graph.addNode(existingNode);
        }

        return existingNode;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GraphAlgorithmsApp app = new GraphAlgorithmsApp();
            app.setVisible(true);
        });
    }
}

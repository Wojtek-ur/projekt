import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;

public class GraphAlgorithmsApp extends JFrame {

    private GraphPanel graphPanel;
    private Graph graph;

    public GraphAlgorithmsApp() {
        setTitle("Graph Algorithms App");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.BLUE);

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
        JTextField addNodeField = new JTextField(10);


        JButton addNodeButton = new JButton("Add Node");
        addNodeButton.addActionListener(e -> {
            String nodeName = addNodeField.getText().trim();
            if (!nodeName.isEmpty()) {
                Node newNode = new Node(nodeName);
                graph.addNode(newNode);
                graphPanel.repaint();
                addNodeField.setText("");
            }
        });

        addEdgeButton.addActionListener(e -> {
            String startNodeName = JOptionPane.showInputDialog(this, "Enter start node name:");
            String endNodeName = JOptionPane.showInputDialog(this, "Enter end node name:");

            Node startNode = graph.getNodes().stream()
                    .filter(n -> n.getName().equals(startNodeName))
                    .findFirst()
                    .orElse(null);
            Node endNode = graph.getNodes().stream()
                    .filter(n -> n.getName().equals(endNodeName))
                    .findFirst()
                    .orElse(null);

            if (startNode != null && endNode != null) {
                graph.addEdge(startNodeName, endNodeName);
                graphPanel.repaint();
            } else {
                if (startNode == null && endNode == null) {
                    JOptionPane.showMessageDialog(this, "Both nodes do not exist.");
                } else if (startNode == null) {
                    JOptionPane.showMessageDialog(this, "Start node does not exist.");
                } else {
                    JOptionPane.showMessageDialog(this, "End node does not exist.");
                }
            }
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
                BFSAlgorithm bfsAlgorithm = new BFSAlgorithm(graph);
                List<Node> visitedNodes = bfsAlgorithm.run(startNode);
                graphPanel.highlightNodes(visitedNodes);
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
                DFSAlgorithm dfsAlgorithm = new DFSAlgorithm(graph);
                List<Node> visitedNodes = dfsAlgorithm.run(startNode);
                graphPanel.highlightNodes(visitedNodes);
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

    private void importGraph(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {

                System.out.println(line);
            }

            JOptionPane.showMessageDialog(this, "Graph imported from " + filePath);
            graphPanel.repaint();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportGraph(String filePath) {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            for (Node node : graph.getNodes()) {
                writer.println(node.getName() + " " + node.getPosition().x + " " + node.getPosition().y);
            }
            writer.println("EDGES");
            for (Edge edge : graph.getEdges()) {
                writer.println(edge.getStartNode().getName() + " " + edge.getEndNode().getName() + " " + edge.isDirected());
            }

            JOptionPane.showMessageDialog(this, "Graph exported to " + filePath);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GraphAlgorithmsApp app = new GraphAlgorithmsApp();
            app.setVisible(true);
        });
    }
}
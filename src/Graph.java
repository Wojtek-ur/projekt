import java.util.*;

public class Graph {
    private List<Node> nodes;
    private List<Edge> edges;

    public Graph() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void removeNode(Node node) {
        nodes.remove(node);
        edges.removeIf(edge -> edge.getStartNode().equals(node) || edge.getEndNode().equals(node));
    }

    public void removeAllNodes() {
        nodes.clear();
        edges.clear();
    }

    public void addEdge(String startNodeName, String endNodeName) {
        Node startNode = findOrCreateNode(startNodeName);
        Node endNode = findOrCreateNode(endNodeName);
        edges.add(new Edge(startNode, endNode, false));
    }

    public void removeEdge(String startNodeName, String endNodeName) {
        Node startNode = findNode(startNodeName);
        Node endNode = findNode(endNodeName);
        edges.removeIf(edge -> edge.getStartNode().equals(startNode) && edge.getEndNode().equals(endNode));
    }

    private Node findNode(String nodeName) {
        return nodes.stream()
                .filter(node -> node.getName().equals(nodeName))
                .findFirst()
                .orElse(null);
    }

    private Node findOrCreateNode(String nodeName) {
        Node existingNode = findNode(nodeName);
        if (existingNode == null) {
            existingNode = new Node(nodeName);
            nodes.add(existingNode);
        }
        return existingNode;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        edges.stream()
                .filter(edge -> edge.getStartNode().equals(node))
                .forEach(edge -> neighbors.add(edge.getEndNode()));
        edges.stream()
                .filter(edge -> edge.getEndNode().equals(node) && !edge.isDirected())
                .forEach(edge -> neighbors.add(edge.getStartNode()));
        return neighbors;
    }
}
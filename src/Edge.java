public class Edge {
    private Node startNode;
    private Node endNode;
    private boolean directed;

    public Edge(Node startNode, Node endNode, boolean directed) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.directed = directed;
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public boolean isDirected() {
        return directed;
    }
}
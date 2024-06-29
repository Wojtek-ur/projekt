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
        // Remove edges associated with this node
        edges.removeIf(edge -> edge.getStartNode().equals(node) || edge.getEndNode().equals(node));
    }

    public void removeAllNodes() {
        nodes.clear();
        edges.clear(); // Clear all edges when removing all nodes
    }

    public void addEdge(String startNodeName, String endNodeName, boolean directed) {
        Node startNode = findNodeByName(startNodeName);
        Node endNode = findNodeByName(endNodeName);
        if (startNode != null && endNode != null) {
            edges.add(new Edge(startNode, endNode, directed));
        }
    }

    public void removeEdge(String startNodeName, String endNodeName) {
        Iterator<Edge> iterator = edges.iterator();
        while (iterator.hasNext()) {
            Edge edge = iterator.next();
            if (edge.getStartNode().getName().equals(startNodeName) &&
                    edge.getEndNode().getName().equals(endNodeName)) {
                iterator.remove();
                break; // Assuming no duplicate edges between same nodes
            }
        }
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public List<Node> bfs(Node startNode) {
        List<Node> visitedNodes = new ArrayList<>();
        Queue<Node> queue = new LinkedList<>();
        Set<Node> visitedSet = new HashSet<>();

        queue.add(startNode);
        visitedSet.add(startNode);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            visitedNodes.add(current);
            List<Node> neighbors = getNeighbors(current);
            for (Node neighbor : neighbors) {
                if (!visitedSet.contains(neighbor)) {
                    visitedSet.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return visitedNodes;
    }

    public List<Node> dfs(Node startNode) {
        List<Node> visitedNodes = new ArrayList<>();
        Set<Node> visitedSet = new HashSet<>();
        Stack<Node> stack = new Stack<>();

        stack.push(startNode);

        while (!stack.isEmpty()) {
            Node current = stack.pop();
            if (!visitedSet.contains(current)) {
                visitedSet.add(current);
                visitedNodes.add(current);
                List<Node> neighbors = getNeighbors(current);
                for (int i = neighbors.size() - 1; i >= 0; i--) {
                    Node neighbor = neighbors.get(i);
                    if (!visitedSet.contains(neighbor)) {
                        stack.push(neighbor);
                    }
                }
            }
        }

        return visitedNodes;
    }

    private Node findNodeByName(String nodeName) {
        for (Node node : nodes) {
            if (node.getName().equals(nodeName)) {
                return node;
            }
        }
        return null;
    }

    private List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getStartNode().equals(node)) {
                neighbors.add(edge.getEndNode());
            }
        }
        return neighbors;
    }
}

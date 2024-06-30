import java.util.*;

public class BFSAlgorithm {
    private Graph graph;

    public BFSAlgorithm(Graph graph) {
        this.graph = graph;
    }

    public List<Node> run(Node startNode) {
        List<Node> visitedNodes = new ArrayList<>();
        Queue<Node> queue = new LinkedList<>();
        Set<Node> visitedSet = new HashSet<>();

        queue.add(startNode);
        visitedSet.add(startNode);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            visitedNodes.add(currentNode);

            List<Node> neighbors = graph.getNeighbors(currentNode);
            for (Node neighbor : neighbors) {
                if (!visitedSet.contains(neighbor)) {
                    visitedSet.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return visitedNodes;
    }
}
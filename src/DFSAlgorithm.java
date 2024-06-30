import java.util.*;

public class DFSAlgorithm {
    private Graph graph;

    public DFSAlgorithm(Graph graph) {
        this.graph = graph;
    }

    public List<Node> run(Node startNode) {
        List<Node> visitedNodes = new ArrayList<>();
        Stack<Node> stack = new Stack<>();
        Set<Node> visitedSet = new HashSet<>();

        stack.push(startNode);
        visitedSet.add(startNode);

        while (!stack.isEmpty()) {
            Node currentNode = stack.pop();
            visitedNodes.add(currentNode);

            List<Node> neighbors = graph.getNeighbors(currentNode);
            for (Node neighbor : neighbors) {
                if (!visitedSet.contains(neighbor)) {
                    visitedSet.add(neighbor);
                    stack.push(neighbor);
                }
            }
        }

        return visitedNodes;
    }
}

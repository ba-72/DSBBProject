import java.awt.Point;
import java.util.*;

public class Dijkstra {
    public static List<Point> findPath(ImageGraph graph, Point start, Point end) {
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(n -> n.cost));
        Map<Point, Double> costSoFar = new HashMap<>();
        Map<Point, Point> cameFrom = new HashMap<>();

        queue.add(new Node(start, 0));
        costSoFar.put(start, 0.0);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current.p.equals(end)) break;

            // 获取8邻接点
            for (Point neighbor : getNeighbors(current.p, graph.cost[0].length, graph.cost.length)) {
                double newCost = costSoFar.get(current.p) + graph.getCost(current.p, neighbor);
                if (!costSoFar.containsKey(neighbor) || newCost < costSoFar.get(neighbor)) {
                    costSoFar.put(neighbor, newCost);
                    queue.add(new Node(neighbor, newCost));
                    cameFrom.put(neighbor, current.p);
                }
            }
        }
        return reconstructPath(cameFrom, end);
    }

    private static List<Point> reconstructPath(Map<Point, Point> cameFrom, Point end) {
        List<Point> path = new ArrayList<>();
        Point current = end;
        while (cameFrom.containsKey(current)) {
            path.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(path);
        return path;
    }

    private static List<Point> getNeighbors(Point p, int maxX, int maxY) {
        List<Point> neighbors = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int x = p.x + dx;
                int y = p.y + dy;
                if (x >= 0 && x < maxX && y >= 0 && y < maxY) {
                    neighbors.add(new Point(x, y));
                }
            }
        }
        return neighbors;
    }

    private static class Node {
        Point p;
        double cost;
        Node(Point p, double cost) { this.p = p; this.cost = cost; }
    }
}
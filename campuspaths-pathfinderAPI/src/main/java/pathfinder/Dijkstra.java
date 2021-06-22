package pathfinder;

import graph.Edge;
import graph.Graph;
import pathfinder.datastructures.Path;

import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * Class of dijkstras method
 */
public class Dijkstra {
    /**
     * @spec.requires g's edges are all doubles, and are all non-negative doubles, and not null
     * @spec.requires n1, n2, and g are not null
     * @throws IllegalArgumentException if n1 or n2 is not in g
     * @param g a graph
     * @param n1 starting node
     * @param n2 ending node
     * @return a path, representing min cost path from n1 to n2, null if path doesn't exist
     */
    public static <N>  Path<N> findPath(Graph<N, Double> g, N n1, N n2) {
        if (!g.hasNode(n1) && !g.hasNode(n2)) {
            throw new IllegalArgumentException("Both nodes are not in the graph");
        }
        if (!g.hasNode(n1)) {
            throw new IllegalArgumentException("Start node is not in the graph");
        }
        if (!g.hasNode(n2)) {
            throw new IllegalArgumentException("End node is not in the graph");
        }
        N start = n1;
        N dest = n2;
        PriorityQueue<Path<N>> active = new PriorityQueue<Path<N>>();
        HashSet<N> finished = new HashSet<N>();
        Path<N> itself = new Path<N>(start);
        active.add(itself);
        while (!active.isEmpty()) {
            Path<N> minPath = active.poll();
            N minDest = minPath.getEnd();
            if (minDest.equals(dest)) {
                return minPath;
            }
            if (finished.contains(minDest)) {
                continue;
            }
            for (Edge<Double, N> e : g.getEdges(minDest)) {
                if (!finished.contains(e.getDest())) {
                    Path<N> newPath = minPath.extend(e.getDest(), e.getLabel());
                    active.add(newPath);
                }
            }
            finished.add(minDest);
        }
//        no path exists
        return null;

    }
}

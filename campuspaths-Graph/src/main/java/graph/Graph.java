package graph;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a mutable directed graph.
 *
 *
 *  @spec.specfield nodes : Set&lt;N&gt; // Nodes of the graph
 *  @spec.specfield edges : Collection&lt;Set&lt;Edge&lt;E,N&gt;&gt;&gt; // Edges of the graph
 *
 *
 *  Abstract Invariant:
 *  Each node stores completely different data
 *  Each edge must be completely different from other edges
 */

// N is the type of the node, E is the type of the edge labels
public class Graph<N, E> {
//    this map maps the nodes to the node's set of edges
    private Map<N, Set<Edge<E,N>>> map;

//    whether or not to include expensive checkRep statements.
    public static final boolean DEBUG = false;
//    Abstraction function:
//        AF(this) = directed graph g
//        Graph's set of nodes = map.keySet();
//        set of edges going out from node x = map.get(x);
//

//    Rep invariant:
//        Graph is not null
//        No null nodes and no null edges
//        For all nodes in graph, map.get(node) != null
//        No duplicate nodes
//        no duplicate edges with same label

    /**
     * @spec.effects constructs an empty mutable directed graph.
     */
    public Graph () {
        this.map = new HashMap<>();
        checkRep();
    }

    /**
     * Adds node to graph if graph doesn't already contain it
     * @param node node to be added to the graph
     * @throws IllegalArgumentException if node is null
     * @spec.modifies this.nodes
     * @spec.effects adds node to graph's nodes if graph does not contain it already
     * @return true if graph did not contain node before adding
     */
    public boolean addNode(N node) {
        if (node == null) {
            throw new IllegalArgumentException();
        }
        checkRep();
        if (this.map.containsKey(node)) {
            return false;
        }
        this.map.put(node, new HashSet<>());
        checkRep();
        return true;
    }

    /**
     * Adds edge to graph if graph doesn't already contain this edge
     * @param n1 origin node of the edge
     * @param n2 destination node of the edge
     * @param label the label of the edge
     * @spec.requires n1,n2, and label are not null
     * @throws IllegalArgumentException if n1 is not in graph or n2 is not in graph
     * @spec.modifies this.edges
     * @spec.effects adds edge from n1 to n2 with label to the graph if graph doesn't already contain this edge
     * @return true if graph did not contain edge with same label and parent node before adding it.
     */
    public boolean addEdge(N n1, N n2, E label) {
        if (!map.containsKey(n1) || !map.containsKey(n2)) {
            throw new IllegalArgumentException();
        }
        checkRep();
        Edge<E,N> e = new Edge<E,N>(label, n2);
        if (map.get(n1).contains(e)) {
            return false;
        }
        this.map.get(n1).add(e);
        checkRep();
        return true;
    }

    /**
     * Returns true if graph contains node
     *
     * @param node node in question
     * @throws IllegalArgumentException if node is null
     * @return true if graph contains node
     */
    public boolean hasNode(N node) {
        if (node == null) {
            throw new IllegalArgumentException();
        }
        checkRep();
        return map.containsKey(node);
    }

    /**
     * Returns true if edges already exist on node
     * @param n1 node we are examining
     * @param label what to label the edge
     * @param n2 node which n1 points to in the edge
     * @spec.requires n1 and label are not null
     * @throws IllegalArgumentException if n1 is not in graph
     * @return true if n1 contains this edge
     */
    public boolean hasEdge(N n1, N n2, E label)  {
        if (!map.containsKey(n1)) {
            throw new IllegalArgumentException();
        }
        checkRep();
        Edge<E,N> e = new Edge<E,N>(label, n2);
        checkRep();
        return map.get(n1).contains(e);
    }

    /**
     * Return set of nodes
     * @return set of all nodes in the graph
     */
    public Set<N> getNodes() {
        checkRep();
        return new HashSet<N>(map.keySet());
    }

    /**
     *  Returns a node's children
     * @param node node we examine
     * @spec.requires node is not null
     * @throws IllegalArgumentException if node is not in graph
     * @return set of edge from node
     */
    public Set<Edge<E,N>> getEdges (N node) {
        if (!map.containsKey(node)) {
            throw new IllegalArgumentException();
        }
        checkRep();
        return map.get(node);
    }

    /**
     * Returns a string representation of hte graph
     *
     * @return string representation of the graph
     */
    public String toString() {
        checkRep();
        return map.toString();
    }

    /**
     * Makes sure that rep invariant holds
     */
    private void checkRep() {
        assert(map != null);
        if (DEBUG) {
            for (N node : map.keySet()) {
                assert(node != null);
                Set<Edge<E,N>> get = map.get(node);
                assert(get != null);
                for (Edge<E,N> edge : get) {
                    assert(edge) != null;
                }
            }
        }
//        disclaimer: avoided test for duplicate nodes/edges because too costly to test and by how HashSet
//        implementation works, there are no two equal Strings so duplicate nodes are impossible and because
//        I implemented the hashcode for edges, there are no duplicate edges from the same node.
    }
}
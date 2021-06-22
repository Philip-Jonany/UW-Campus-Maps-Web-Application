package pathfinder.junitTests;

import graph.Graph;
import marvel.MarvelPaths;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import pathfinder.Dijkstra;
import pathfinder.datastructures.Path;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static pathfinder.Dijkstra.findPath;

public class TestDijkstra {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    //    creates new graph
    private Graph<String,Double> g() {
        return new Graph<String,Double>();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDijsktraThrowsException1() throws Exception {
        Graph<String, Double> g = g();
        findPath(g, "n1", "n2");
    }
    @Test(expected = IllegalArgumentException.class)
    public void testDijsktraThrowsException2() throws Exception {
        Graph<String, Double> g = g();
        g.addNode("n1");
        findPath(g, "n1", "n2");
    }
    @Test(expected = IllegalArgumentException.class)
    public void testDijsktraThrowsException3() throws Exception {
        Graph<String, Double> g = g();
        g.addNode("n2");
        findPath(g, "n1", "n2");
    }

    @Test
    public void testDijsktraOneEdge(){
        Graph<String, Double> g = g();
        g.addNode("n1");
        g.addNode("n2");
        g.addEdge("n1","n2", 9999.9999);
        Path<String> path = findPath(g, "n1", "n2");
        Path<String> newPath = new Path<>("n1");
        newPath = newPath.extend("n2",9999.9999);
        assertEquals(path, newPath);
    }
}

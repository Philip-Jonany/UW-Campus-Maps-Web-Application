package graph.junitTests;

import graph.Edge;
import graph.Graph;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.HashSet;

import static org.junit.Assert.*;

public class GraphTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

//    creates new graph
    private Graph<String,String> g() {
        return new Graph<String,String>();
    }

//    nodes of empty graph
    @Test()
    public void testNodesOfEmpty () {
        assertEquals(new HashSet<String>(), g().getNodes());
    }
    @Test()
    public void testToStringOfEmpty() {
        assertEquals("{}", g().toString());
    }


//    adding null node should throw exception
    @Test(expected = IllegalArgumentException.class)
    public void testAddNullNode() {
        Graph<String,String> g = g();
        g.addNode(null);
    }

    @Test
    public void testAddNode() {
        Graph<String,String> g = g();
        g.addNode("n1");
        assertTrue(g.hasNode("n1"));
    }
    @Test
    public void testToStringAfterAddingNode() {
        Graph<String,String> g = g();
        g.addNode("n1");
        assertEquals("{n1=[]}", g.toString());
    }
    @Test
    public void testAddDuplicateNode() {
        Graph<String,String> g = g();
        g.addNode("n1");
        assertFalse(g.addNode("n1"));
    }

    @Test
    public void testAddMultipleNodes() {
        Graph<String,String> g = g();
        g.addNode("n1");
        g.addNode("n2");
        assertTrue(g.hasNode("n1"));
        assertTrue(g.hasNode("n2"));
    }

//    adding edge to nonexistent node
    @Test(expected = IllegalArgumentException.class)
    public void testAddEdgeToNonexistent() {
        Graph<String,String> g = g();
        g.addNode("n3");
        g.addEdge("n1", "n2", "e1" );
    }

    @Test
    public void testAddEdge() {
        Graph<String,String> g = g();
        g.addNode("n1");
        g.addNode("n2");
        g.addEdge("n1", "n2", "e1" );
        assertTrue(g.hasEdge("n1", "n2", "e1"));
    }

    @Test
    public void testAddMultipleEdges() {
        Graph<String,String> g = g();
        g.addNode("n1");
        g.addNode("n2");
        g.addNode("n3");
        g.addEdge("n1", "n2", "e1" );
        g.addEdge("n1", "n3", "e2" );
        assertTrue(g.hasEdge("n1", "n2", "e1"));
        assertTrue(g.hasEdge("n1", "n3", "e2"));
    }

    @Test
    public void testAddDuplicateEdges() {
        Graph<String,String> g = g();
        g.addNode("n1");
        g.addNode("n2");
        g.addNode("n3");
        g.addEdge("n1", "n2", "e1" );
        assertFalse(g.addEdge("n1", "n2", "e1" ));
    }


    @Test(expected = IllegalArgumentException.class)
    public void testHasNodeThrowsException() {
        g().hasNode(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasEdgeThrowsException() {
        Graph<String,String> g = g();
        g.addNode("n1");
        g.addNode("n2");
        g.addEdge("n1", "n2", "e1");
        assertFalse(g.hasEdge("n3", "n2", "e1"));
    }

    @Test
    public void testGetNodesThree() {
        Graph<String,String> g = g();
        g.addNode("n1");
        g.addNode("n2");
        g.addNode("n3");
        g.addEdge("n1", "n2", "e1");
        HashSet<String> set = new HashSet<>();
        set.add("n1");
        set.add("n2");
        set.add("n3");
        assertEquals(set, g.getNodes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetChildrenException() {
        Graph<String,String> g = g();
        g.addNode("n1");
        g.getEdges("n2");
    }

    @Test()
    public void testGetChildrenNone() {
        Graph<String,String> g = g();
        g.addNode("n1");
        g.addNode("n2");
        g.addNode("n3");
        g.addEdge("n1", "n2", "e1");
        HashSet<Edge> set = new HashSet<>();
        assertEquals(set, g.getEdges("n2"));
    }

    @Test()
    public void testGetChildrenMultiple() {
        Graph<String,String> g = g();
        g.addNode("n1");
        g.addNode("n2");
        g.addNode("n3");
        g.addEdge("n1", "n2", "e1");
        g.addEdge("n1", "n3", "e2");
        HashSet<Edge> set = new HashSet<>();
        set.add(new Edge<String,String>("e1", "n2"));
        set.add(new Edge<String,String>("e2", "n3"));
        assertEquals(set, g.getEdges("n1"));
    }
}

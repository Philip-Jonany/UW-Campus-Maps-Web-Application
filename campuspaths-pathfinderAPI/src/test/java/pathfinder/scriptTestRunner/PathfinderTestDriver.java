/*
 * Copyright (C) 2021 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2021 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package pathfinder.scriptTestRunner;

import graph.Edge;
import graph.Graph;
import marvel.MarvelPaths;
import pathfinder.Dijkstra;
import pathfinder.datastructures.Path;
import java.util.Comparator;
import java.io.*;
import java.util.*;

/**
 * This class implements a test driver that uses a script file format
 * to test an implementation of Dijkstra's algorithm on a graph.
 */
public class PathfinderTestDriver {
    private final Map<String, Graph<String,Double>> graphs = new HashMap<String, Graph<String,Double>>();
    private final PrintWriter output;
    private final BufferedReader input;

    // Leave this constructor public
    public PathfinderTestDriver(Reader r, Writer w) {
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    // Leave this method public
    public void runTests() throws IOException {
        String inputLine;
        while((inputLine = input.readLine()) != null) {
            if((inputLine.trim().length() == 0) ||
                    (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if(st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<>();
                    while(st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            switch(command) {
                case "FindPath":
                    findPath(arguments);
                    break;
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "ListNodes":
                    listNodes(arguments);
                    break;
                case "ListChildren":
                    listChildren(arguments);
                    break;
                default:
                    output.println("Unrecognized command: " + command);
                    break;
            }
        } catch(Exception e) {
            String formattedCommand = command;
            formattedCommand += arguments.stream().reduce("", (a, b) -> a + " " + b);
            output.println("Exception while running command: " + formattedCommand);
            e.printStackTrace(output);
        }
    }

    private void findPath (List<String> arguments) {
        if(arguments.size() != 3) {
            throw new CommandException("Bad arguments to FindPath: " + arguments);
        }

        String graphName = arguments.get(0);
        String n1 = arguments.get(1);
        String n2 = arguments.get(2);
        findPath(graphName, n1, n2);
    }

    private void findPath (String graphName, String n1, String n2) {
        Graph<String, Double> g  = graphs.get(graphName);
        if (!g.hasNode(n1) && !g.hasNode(n2)) {
            output.println("unknown: " + n1);
            output.println("unknown: " + n2);
            return;
        }
        if (!g.hasNode(n1)) {
            output.println("unknown: " + n1);
            return;
        }
        if (!g.hasNode(n2)) {
            output.println("unknown: " + n2);
            return;
        }
        Path<String> path = Dijkstra.findPath(g, n1, n2);
        output.println("path from " + n1 +" to "+ n2 +":");
        if (path == null) {
            output.println("no path found");
            return;
        }
        Iterator<Path<String>.Segment> it = path.iterator();
        Path<String>.Segment curr;
        while (it.hasNext()) {
            curr = it.next();
            output.println(curr.getStart() +" to "+ curr.getEnd()+" with weight "+ String.format("%.3f", curr.getCost()));
        }
        output.println(String.format("total cost: %.3f", path.getCost()));
    }

    private void createGraph(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
        if (graphs.containsKey(graphName)) {
            return;
        }
        graphs.put(graphName, new Graph<String,Double>());
        output.println("created graph " + graphName);
    }

    private void addNode(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to AddNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {
        Graph<String,Double> g = graphs.get(graphName);
        boolean added = g.addNode(nodeName);
        if (added) {
            output.print("added node " + nodeName +" to " + graphName +"\n");
        }
    }

    private void addEdge(List<String> arguments) {
        if(arguments.size() != 4) {
            throw new CommandException("Bad arguments to AddEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);

        double edgeLabel;
        try {
            edgeLabel = Double.parseDouble(arguments.get(3));
        } catch(NumberFormatException e) {
            throw new CommandException("Last argument must be a double");
        }
        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
                         Double edgeLabel) {
        Graph<String,Double> g = graphs.get(graphName);
        Boolean added = g.addEdge(parentName,childName, edgeLabel);
        if (added) {
            output.println("added edge " + String.format("%.3f",edgeLabel) +" from " +parentName+" to "+childName+" in " + graphName);
        }
    }

    private void listNodes(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to ListNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {
        Graph<String,Double> g = graphs.get(graphName);
        output.print(graphName +" contains:");
        for (String n : g.getNodes()) {
            output.print(" " + n);
        }
        output.println();
    }

    private void listChildren(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {
        Graph<String,Double> g  = graphs.get(graphName);
        output.print("the children of " + parentName + " in "+graphName+" are:");
        Set<Edge<Double,String>> set = g.getEdges(parentName);
        ArrayList<Edge<Double,String>> list = new ArrayList<>();
        for (Edge<Double,String> e:set) {
            list.add(e);
        }
        Collections.sort(list, new Comparator<Edge<Double, String>>() {
            @Override
            public int compare(Edge<Double, String> o1, Edge<Double, String> o2) {
                if (!(o1.getLabel().equals(o2.getLabel()))) {
                    return o1.getLabel().compareTo(o2.getLabel());
                }
                if(!(o1.getDest().equals(o2.getDest()))) {
                    return o1.getDest().compareTo(o2.getDest());
                }
                return 0;
            }
        });
        for (Edge<Double,String> e : list) {
            output.print(" " +e.getDest() + "(" + String.format("%.3f", e.getLabel()) + ")");
        }
        output.println();
    }

    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }

        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }
}

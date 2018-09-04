package ru.uskov.apidiff.graph;

import ru.uskov.apidiff.classesmodel.ClassInstance;
import ru.uskov.apidiff.transform.TransformOperation;

import java.util.*;

public class Graph {

    private final Set<ClassInstance> initialApi;

    public Graph(Set<ClassInstance> initialApi) {
        this.initialApi= new HashSet<>(initialApi);
    }

    //TODO add progress monitor
    //TODO in case of Dijkstra’s algorithm you should go through full graph
    public GraphNode pathTo(Set<ClassInstance> newApi) {
        final Set<GraphNode> openNodes = new HashSet<>();
        final Queue<GraphNode> shellNodes = new PriorityQueue<>();

        shellNodes.add(new GraphNode(initialApi));
        final RouteHelper routeHelper = new RouteHelper();

        while (! shellNodes.isEmpty()) {
            final GraphNode currentNode = shellNodes.poll();

            System.out.println(currentNode.getWeight() + "  " + openNodes.size());
            if (currentNode.getApi().equals(newApi)) {
                return currentNode;
            }
            openNodes.add(currentNode);
            for (TransformOperation transformOperation : routeHelper.getTransformOperations(currentNode, newApi)) {
                GraphNode newNode = currentNode.withTransform(transformOperation);
                if (! openNodes.contains(newNode)) {
                    for(Iterator<GraphNode> it = shellNodes.iterator(); it.hasNext(); ) {
                        final GraphNode node = it.next();
                        if ((node.getWeight() > newNode.getWeight()) && node.equals(newNode)) {
                            // Existing node has unoptimal route
                            it.remove();
                            shellNodes.add(newNode);
                            break;
                        }
                    }
                    shellNodes.add(newNode);
                }
            }
        }
        //TODO Error: could not find path
        return null;
    }
}

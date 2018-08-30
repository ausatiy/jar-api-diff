package ru.uskov.apidiff.graph;

import ru.uskov.apidiff.classesmodel.ClassInstance;
import ru.uskov.apidiff.transform.TransformOperation;

import java.util.*;

public class Graph {

    private final Set<ClassInstance> initialApi;

    public Graph(Set<ClassInstance> initialApi) {
        this.initialApi= new HashSet<>(initialApi);
    }

    //TODO progress monitor
    //TODO in case of Deyxtra algorithm you should go through full graph
    public GraphNode pathTo(Set<ClassInstance> newApi) {
        final Set<GraphNode> openNodes = new TreeSet<>();
        final Queue<GraphNode> shellNodes = new PriorityQueue<>();

        shellNodes.add(new GraphNode(initialApi));

        final RouteHelper routeHelper = new RouteHelper();

        while (! shellNodes.isEmpty()) {
            GraphNode currentNode = shellNodes.poll();
            if (currentNode.getApi().equals(newApi)) {
                return currentNode;
            }
            openNodes.add(currentNode);
            for (TransformOperation transformOperation : routeHelper.getTransformOperations(currentNode.getApi(), newApi)) {
                GraphNode newNode = currentNode.withTransform(transformOperation);
                if (! openNodes.contains(newNode)) {
                    shellNodes.add(newNode);
                }
            }
        }
        //TODO Error: could not find path
        return null;
    }
}

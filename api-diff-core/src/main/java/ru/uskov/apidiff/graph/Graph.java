package ru.uskov.apidiff.graph;

import ru.uskov.apidiff.classesmodel.Api;
import ru.uskov.apidiff.classesmodel.ClassInstance;
import ru.uskov.apidiff.transform.TransformOperation;

import java.util.*;

/**
 * Graph of API changes. Each node of graph represents API obtained by some list of changes.
 * At the moment the Dijkstra’s algorithm is used to find the easiest way to find path in the graph
 * (to find list of changes in order to obtain required API)
 */
public class Graph {

    private final Api initialApi;
    private final RouteHelper.RenamePolicy renamePolicy;

    public Graph(Api initialApi, RouteHelper.RenamePolicy renamePolicy) {
        this.initialApi= initialApi;
        this.renamePolicy = renamePolicy;
    }

    /**
     * Finds the "lightest" list of changes to get newApi from initialApi
     * @param newApi the api to be obtained
     * @return graphNode with list of changes required to get required API
     */
    public GraphNode pathTo(Api newApi) {
        final Set<GraphNode> openNodes = new HashSet<>();
        final Queue<GraphNode> shellNodesQueue = new PriorityQueue<>();

        shellNodesQueue.add(new GraphNode(initialApi));
        final RouteHelper routeHelper = new RouteHelper(renamePolicy);

        while (! shellNodesQueue.isEmpty()) {
            final GraphNode currentNode = shellNodesQueue.poll();
            if (currentNode.getApi().equals(newApi)) {
                return currentNode;
            }
            openNodes.add(currentNode);
            for (TransformOperation transformOperation : routeHelper.getTransformOperations(currentNode, newApi)) {
                System.out.println(String.format("Checked nodes: %s, nodes to check: %s, current weight: %s.", openNodes.size(), shellNodesQueue.size(), currentNode.getWeight()));
                GraphNode newNode = currentNode.withTransform(transformOperation);
                if (! openNodes.contains(newNode)) {
                    for(Iterator<GraphNode> it = shellNodesQueue.iterator(); it.hasNext(); ) {
                        final GraphNode node = it.next();
                        if ((node.getWeight() > newNode.getWeight()) && node.equals(newNode)) {
                            // Existing node has unoptimal route
                            it.remove();
                            shellNodesQueue.add(newNode);
                            break;
                        }
                    }
                    shellNodesQueue.add(newNode);
                }
            }
        }
        throw new RuntimeException("Could not find find full list of class modifications.");
    }
}

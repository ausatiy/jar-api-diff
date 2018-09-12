package ru.uskov.apidiff.graph;

import org.jetbrains.annotations.NotNull;
import ru.uskov.apidiff.classesmodel.Api;
import ru.uskov.apidiff.classesmodel.ClassInstance;
import ru.uskov.apidiff.transform.TransformOperation;

import java.util.*;

/**
 * Representation of state in the graph of changes.
 * Note: nodes are considered to be equal if they are representing the same {@Link Api}
 * It means that two nodes could be reached by different paths but be equal at the same time. (i.e. weights for equal nodes could differ)
 */
public class GraphNode implements Comparable<GraphNode> {
    private final Api api;

    //These parameters actually may change
    private List<TransformOperation> transforms;
    private int weight;

    public GraphNode(Api api) {
        this(api, new ArrayList<>());
    }

    public GraphNode(Api api, List<TransformOperation> transforms) {
        this.api = api;
        this.transforms = new ArrayList<>(transforms);

        int sum = 0;
        for (TransformOperation transformOperation : transforms) {
            sum += transformOperation.getWeight();
            if (sum < 0) {
                throw new RuntimeException("Integer overflow. It seems that input data size is too big.");
            }
        }
        weight = sum;
    }

    public Api getApi() {
        return api;
    }

    public List<TransformOperation> getTransforms() {
        return Collections.unmodifiableList(transforms);
    }

    public GraphNode withTransform(TransformOperation transformOperation) {
        List<TransformOperation> transformOperations = new ArrayList<>(transforms);
        transformOperations.add(transformOperation);
        return new GraphNode(transformOperation.getNewApi(), transformOperations);
    }

    @Override
    public String toString() {
        return String.format("Weight [%s] Transforms [%s] Api [%s]", weight, transforms, api);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof GraphNode) {
            GraphNode o1 = (GraphNode) o;
            return api.equals(o1.api);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return api.hashCode();
    }

    @Override
    public int compareTo(GraphNode o) {
        return weight - o.weight;
    }

    public int getWeight() {
        return weight;
    }
}

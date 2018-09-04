package ru.uskov.apidiff.graph;

import org.jetbrains.annotations.NotNull;
import ru.uskov.apidiff.classesmodel.ClassInstance;
import ru.uskov.apidiff.transform.TransformOperation;

import java.util.*;

//TODO document that nodes are equal if they are representing the same node.
// the route does not influence on equals
public class GraphNode implements Comparable<GraphNode> {
    private final Set<ClassInstance> api;

    //These parameters actually may change
    private List<TransformOperation> transforms;
    private int weight;

    public GraphNode(Set<ClassInstance> api) {
        this(api, new ArrayList<>());
    }

    public GraphNode(Set<ClassInstance> api, List<TransformOperation> transforms) {
        this.api = new HashSet<>(api);
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

    public Set<ClassInstance> getApi() {
        return Collections.unmodifiableSet(api);
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

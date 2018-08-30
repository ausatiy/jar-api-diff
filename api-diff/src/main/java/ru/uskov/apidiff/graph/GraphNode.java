package ru.uskov.apidiff.graph;

import org.jetbrains.annotations.NotNull;
import ru.uskov.apidiff.classesmodel.ClassInstance;
import ru.uskov.apidiff.transform.TransformOperation;

import java.util.*;

public class GraphNode implements Comparable<GraphNode> {
    private final Set<ClassInstance> api;
    //TODO reorder transforms for better performance
    private final List<TransformOperation> transforms;
    private final int weigth;

    public GraphNode(Set<ClassInstance> initialApi) {
        this(initialApi, new ArrayList<>());
    }

    public GraphNode(Set<ClassInstance> api, List<TransformOperation> transforms) {
        this.api = new HashSet<>(api);
        this.transforms = transforms;
        int sum = 0;
        for (TransformOperation operation : transforms) {
            sum += operation.getWeight();
        }
        weigth = sum;
    }

    public Set<ClassInstance> getApi() {
        return Collections.unmodifiableSet(api);
    }

    public GraphNode withTransform(TransformOperation transformOperation) {
        List<TransformOperation> transformOperations = new ArrayList<>(transforms);
        transformOperations.add(transformOperation);
        return new GraphNode(transformOperation.apply(api), transformOperations);
    }

    @Override
    public int compareTo(@NotNull GraphNode o) {
        if (o == this) {
            return 0;
        }
        int diff = weigth - o.weigth;
        if (diff != 0) {
            return diff;
        }
        if (transforms.size() != o.transforms.size()) {
            return transforms.size() - o.transforms.size();
        }
        for (int i = 0; i < transforms.size(); i++) {
            diff = transforms.get(i).compareTo(o.transforms.get(i));
            if (diff != 0) {
                return diff;
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
    //TODO list of transform operations
    //TODO partial sort
    //TODO equals

    @Override
    public String toString() {
        return String.format("Weight [%s] Transforms [%s] Api [%s]", weigth, transforms, api);
    }
}

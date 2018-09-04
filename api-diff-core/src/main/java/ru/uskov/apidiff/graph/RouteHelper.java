package ru.uskov.apidiff.graph;

import ru.uskov.apidiff.classesmodel.ClassInstance;
import ru.uskov.apidiff.metric.Weights;
import ru.uskov.apidiff.transform.TransformOperation;

import java.util.HashSet;
import java.util.Set;

public class RouteHelper {



    //TODO add filters in order to improve performance
    private final Weights weights;
    private final TransformsProvider.TRANSFORMS[] transforms;

    public RouteHelper() {
        weights = new Weights();
        transforms = TransformsProvider.TRANSFORMS.values();
    }

    public Set<TransformOperation> getTransformOperations(GraphNode graphNode, Set<ClassInstance> newApi) {
        Set<TransformOperation> result = new HashSet<>();
        for (TransformsProvider.TRANSFORMS transform : transforms) {
            result.addAll(transform.getOperations(graphNode.getApi(), newApi, weights));
        }
        return result;
    }
}

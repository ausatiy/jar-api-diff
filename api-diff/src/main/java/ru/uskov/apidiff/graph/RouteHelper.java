package ru.uskov.apidiff.graph;

import ru.uskov.apidiff.classesmodel.ClassInstance;
import ru.uskov.apidiff.metric.Weights;
import ru.uskov.apidiff.transform.RenameClassOperation;
import ru.uskov.apidiff.transform.TransformOperation;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class RouteHelper {
    private final Weights weights = new Weights();

    public Set<TransformOperation> getTransformOperations(Set<ClassInstance> currentApi, Set<ClassInstance> newApi) {
        Set<String> currentNames = currentApi.stream().map(ClassInstance::getName).collect(Collectors.toSet());
        Set<String> newNames = newApi.stream().map(ClassInstance::getName).collect(Collectors.toSet());
        Set<TransformOperation> result = new TreeSet<>();

        //Possible optimisation: firstly add non existing classes
        for (String from : currentNames) {
            for (String to : newNames) {
                if (! from.equals(to)) {
                    result.add(new RenameClassOperation(from, to, weights.getRenameClassWeight()));
                }
            }
        }
        return result;
    }
}

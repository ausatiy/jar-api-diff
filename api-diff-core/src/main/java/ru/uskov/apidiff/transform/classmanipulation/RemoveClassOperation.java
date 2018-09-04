package ru.uskov.apidiff.transform.classmanipulation;

import ru.uskov.apidiff.classesmodel.ClassInstance;
import ru.uskov.apidiff.classesmodel.MethodInstance;
import ru.uskov.apidiff.transform.TransformOperation;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class RemoveClassOperation implements TransformOperation {
    private final String className;
    private final int weight;
    private final Set<ClassInstance> newApi;

    public RemoveClassOperation(Set<ClassInstance> api, String className, int weight) {
        this.className = className;
        this.weight = weight;
        this.newApi = api.stream().filter(x -> !(x.getName().equals(className))).collect(Collectors.toSet());
    }
    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public Set<ClassInstance> getNewApi() {
        return Collections.unmodifiableSet(newApi);
    }

    @Override
    public ClassInstance getSourceClass(ClassInstance classInstance) {
        return classInstance;
    }

    //TODO move to default method
    @Override
    public MethodInstance getSourceMethod(MethodInstance methodInstance) {
        return methodInstance;
    }

    @Override
    public String toString() {
        return String.format("Class \"%s\" was removed.", className);
    }
}

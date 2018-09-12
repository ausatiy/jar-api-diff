package ru.uskov.apidiff.transform.classmanipulation;

import ru.uskov.apidiff.classesmodel.Api;
import ru.uskov.apidiff.classesmodel.ClassInstance;
import ru.uskov.apidiff.classesmodel.MethodInstance;
import ru.uskov.apidiff.transform.ClassManipulation;
import ru.uskov.apidiff.transform.TransformOperation;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class RemoveClassOperation implements TransformOperation, ClassManipulation {
    private final String className;
    private final int weight;
    private final Api newApi;

    public RemoveClassOperation(Api api, String className, int weight) {
        this.className = className;
        this.weight = weight;
        this.newApi = api.removeClassByName(className);
    }
    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public Api getNewApi() {
        return newApi;
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

    @Override
    public boolean affectsClass(String name) {
        return className.equals(name);
    }
}

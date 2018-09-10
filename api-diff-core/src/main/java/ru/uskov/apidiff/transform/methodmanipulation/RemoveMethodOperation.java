package ru.uskov.apidiff.transform.methodmanipulation;

import ru.uskov.apidiff.classesmodel.ClassInstance;
import ru.uskov.apidiff.classesmodel.ImmutableClassInstance;
import ru.uskov.apidiff.classesmodel.MethodInstance;
import ru.uskov.apidiff.transform.TransformOperation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RemoveMethodOperation implements TransformOperation {


    private final ClassInstance sourceClass;
    private final ClassInstance destClass;
    private final MethodInstance sourceMethod;
    private final int weight;
    private final Set<ClassInstance> newApi;

    public RemoveMethodOperation(Set<ClassInstance> oldApi, ClassInstance oldClass, MethodInstance oldMethod, int weight) {
        this.weight = weight;
        this.sourceClass = oldClass;
        this.sourceMethod = oldMethod;
        Set<MethodInstance> newMethods = new HashSet<>(oldClass.getMethods());
        newMethods.remove(oldMethod);
        this.destClass = ImmutableClassInstance.copyOf(oldClass).withMethods(newMethods);
        this.newApi = new HashSet<>(oldApi);
        this.newApi.add(destClass);
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
        return classInstance.equals(destClass) ? sourceClass : classInstance;
    }

    @Override
    public MethodInstance getSourceMethod(MethodInstance methodInstance) {
        return methodInstance;
    }
}

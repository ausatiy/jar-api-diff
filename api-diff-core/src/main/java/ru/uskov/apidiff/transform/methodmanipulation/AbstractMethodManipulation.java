package ru.uskov.apidiff.transform.methodmanipulation;

import ru.uskov.apidiff.classesmodel.Api;
import ru.uskov.apidiff.classesmodel.ClassInstance;
import ru.uskov.apidiff.classesmodel.ImmutableClassInstance;
import ru.uskov.apidiff.classesmodel.MethodInstance;
import ru.uskov.apidiff.transform.TransformOperation;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractMethodManipulation implements TransformOperation {
    protected final ClassInstance sourceClass;
    protected final ClassInstance destClass;
    protected final MethodInstance sourceMethod;
    protected final MethodInstance destMethod;
    protected final int weight;
    protected final Api newApi;

    public AbstractMethodManipulation(Api oldApi, ClassInstance sourceClass, MethodInstance sourceMethod, MethodInstance destMethod, int weight) {
        this.sourceClass = sourceClass;
        this.weight = weight;
        this.destMethod = destMethod;
        this.sourceMethod = sourceMethod;

        Set<MethodInstance> methods = new HashSet<>(sourceClass.getMethods());
        methods.remove(sourceMethod);
        if (destMethod != null) {
            methods.add(destMethod);
        }

        this.destClass = ImmutableClassInstance.copyOf(sourceClass).withMethods(methods);
        this.newApi = oldApi.replace(sourceClass, destClass);
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
        return classInstance.equals(destClass) ? sourceClass : classInstance;
    }

    @Override
    public MethodInstance getSourceMethod(MethodInstance methodInstance) {
        return methodInstance.equals(destMethod) ? sourceMethod : methodInstance;
    }
}

package ru.uskov.apidiff.transform.attributemanipulation;

import ru.uskov.apidiff.classesmodel.ClassInstance;
import ru.uskov.apidiff.classesmodel.ImmutableClassInstance;
import ru.uskov.apidiff.classesmodel.ImmutableMethodInstance;
import ru.uskov.apidiff.classesmodel.MethodInstance;
import ru.uskov.apidiff.transform.TransformOperation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ChangeMethodAttributesOperation implements TransformOperation {
    private final ClassInstance sourceClass;
    private final ClassInstance destClass;
    private final MethodInstance sourceMethod;
    private final MethodInstance destMethod;
    private final int weight;
    private final Set<ClassInstance> newApi;

    public ChangeMethodAttributesOperation(Set<ClassInstance> oldApi, ClassInstance oldClass, MethodInstance oldMethod, MethodInstance newMethod, int weight) {
        this.weight = weight;
        this.sourceMethod = oldMethod;
        this.destMethod = ImmutableMethodInstance.copyOf(oldMethod)
                .withReturnType(newMethod.getReturnType())
                .withIsAbstract(newMethod.isAbstract())
                .withIsFinal(newMethod.isFinal())
                .withIsStatic(newMethod.isStatic())
                .withVisibility(newMethod.getVisibility())
                .withExceptions(newMethod.getExceptions());
        this.sourceClass = oldClass;
        Set<MethodInstance> newMethods = new HashSet<>(oldClass.getMethods());
        newMethods.remove(oldMethod);
        newMethods.add(newMethod);
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
        return methodInstance.equals(destMethod) ? sourceMethod : methodInstance;
    }
}

package ru.uskov.apidiff.transform.attributemanipulation;

import ru.uskov.apidiff.classesmodel.*;
import ru.uskov.apidiff.transform.TransformOperation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ChangeClassAttributesOperation implements TransformOperation {

    private final int weight;
    private final ClassInstance sourceClass;
    private final ClassInstance destinationClass;

    private final Api newApi;

    public ChangeClassAttributesOperation(Api oldApi, ClassInstance newClass, int weight) {
        this.weight = weight;
        this.sourceClass = oldApi.get(newClass.getName());
        newClass = ImmutableClassInstance.copyOf(newClass).withMethods(this.sourceClass.getMethods());
        this.destinationClass = newClass;
        this.newApi = oldApi.replace(this.sourceClass, newClass);
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
        return destinationClass.equals(classInstance) ? sourceClass : classInstance;
    }

    @Override
    public MethodInstance getSourceMethod(MethodInstance methodInstance) {
        return methodInstance;
    }

    @Override
    public String toString() {
        return String.format("Class signature changed from \"%s\" to \"%s\".", sourceClass.getSignature(), destinationClass.getSignature());
    }
}

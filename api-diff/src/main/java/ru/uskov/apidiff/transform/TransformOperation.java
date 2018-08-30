package ru.uskov.apidiff.transform;

import ru.uskov.apidiff.classesmodel.ClassInstance;

import java.util.Set;

public interface TransformOperation extends Comparable<TransformOperation> {

    int getWeight();

    Set<ClassInstance> apply(Set<ClassInstance> api);

    boolean isCommunicativeTo(TransformOperation operation);
}

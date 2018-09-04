package ru.uskov.apidiff.transform;

import ru.uskov.apidiff.classesmodel.ClassInstance;
import ru.uskov.apidiff.classesmodel.MethodInstance;

import java.util.Set;

public interface TransformOperation {

    int getWeight();

    Set<ClassInstance> getNewApi();

    ClassInstance getSourceClass(ClassInstance classInstance);

    MethodInstance getSourceMethod(MethodInstance methodInstance);

}

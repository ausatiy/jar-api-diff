package ru.uskov.apidiff.transform.methodmanipulation;

import ru.uskov.apidiff.classesmodel.Api;
import ru.uskov.apidiff.classesmodel.ClassInstance;
import ru.uskov.apidiff.classesmodel.ImmutableClassInstance;
import ru.uskov.apidiff.classesmodel.MethodInstance;
import ru.uskov.apidiff.transform.TransformOperation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AddMethodOperation extends AbstractMethodManipulation {

    public AddMethodOperation(Api oldApi, ClassInstance oldClass, MethodInstance newMethod, int weight) {
        super(oldApi, oldClass, null, newMethod, weight);
    }

    @Override
    public String toString() {
        return String.format("In class \"%s\" method \"%s\" was added.", sourceClass.getName(), destMethod.getSignature());
    }
}

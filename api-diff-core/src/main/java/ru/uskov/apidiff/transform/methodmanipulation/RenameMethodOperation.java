package ru.uskov.apidiff.transform.methodmanipulation;

import ru.uskov.apidiff.classesmodel.*;
import ru.uskov.apidiff.transform.TransformOperation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RenameMethodOperation extends AbstractMethodManipulation {

    public RenameMethodOperation(Api oldApi, ClassInstance oldClass, MethodInstance oldMethod, MethodInstance newMethod, int weight) {
        super(oldApi, oldClass, oldMethod, ImmutableMethodInstance.copyOf(oldMethod).withName(newMethod.getName()), weight);
    }

    @Override
    public String toString() {
        return String.format("In class \"%s\" method \"%s\" was renamed to \"%s\".", sourceClass.getName(), sourceMethod.getSignature(), destMethod.getName());
    }
}

package ru.uskov.apidiff.transform.attributemanipulation;

import ru.uskov.apidiff.classesmodel.*;
import ru.uskov.apidiff.transform.TransformOperation;
import ru.uskov.apidiff.transform.methodmanipulation.AbstractMethodManipulation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ChangeMethodAttributesOperation extends AbstractMethodManipulation {

    public ChangeMethodAttributesOperation(Api oldApi, ClassInstance oldClass, MethodInstance oldMethod, MethodInstance newMethod, int weight) {
        super(oldApi, oldClass, oldMethod, copyAttributes(oldMethod, newMethod), weight);
    }

    private static MethodInstance copyAttributes(MethodInstance oldMethod, MethodInstance newMethod) {
        return ImmutableMethodInstance.copyOf(oldMethod)
                .withExceptions(newMethod.getExceptions())
                .withReturnType(newMethod.getReturnType())
                .withIsAbstract(newMethod.isAbstract())
                .withIsFinal(newMethod.isFinal())
                .withIsStatic(newMethod.isStatic())
                .withVisibility(newMethod.getVisibility());
    }

    @Override
    public String toString() {
        return String.format("In class \"%s\" method \"%s\" changed to \"%s\".", sourceClass.getName(), sourceMethod.getSignature(), destMethod.getSignature());
    }
}

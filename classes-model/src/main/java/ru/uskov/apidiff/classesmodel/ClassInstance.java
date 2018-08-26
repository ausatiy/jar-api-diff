package ru.uskov.apidiff.classesmodel;

import org.immutables.value.Value;

import java.util.Set;

@Value.Immutable
@Value.Style(strictBuilder = true)
public interface ClassInstance {
    String getName();

    String getParent();

    Set<String> getInterfaces();

    Set<MethodInstance> getMethods();

    boolean isFinal();

    boolean isInterface();

    boolean isAbstract();

    Visibility getVisibility();

}


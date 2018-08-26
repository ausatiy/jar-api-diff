package ru.uskov.apidiff.classesmodel;

import org.immutables.value.Value;

import java.util.List;
import java.util.Set;

@Value.Immutable
@Value.Style(strictBuilder = true)
public interface MethodInstance {

    String getName();

    boolean isFinal();

    boolean isAbstract();

    Visibility getVisibility();

    Set<String> getExceptions();

    List<String> getParameters();
}


package ru.uskov.apidiff.classesmodel;

import org.immutables.value.Value;
import java.util.List;
import java.util.Set;

@Value.Immutable(prehash = true)
@Value.Style(strictBuilder = true)
public interface MethodInstance {

    String getName();

    String getReturnType();

    boolean isFinal();

    boolean isAbstract();

    boolean isStatic();

    Visibility getVisibility();

    Set<String> getExceptions();

    List<String> getParameters();

    @Value.Auxiliary
    List<InstructionInstance> getInstructions();
    
}


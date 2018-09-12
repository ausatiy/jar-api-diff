package ru.uskov.apidiff.classesmodel;

import org.immutables.value.Value;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents method instance including it's signature and partially instructions.
 * At the moment not instructions are parsed properly
 */
@Value.Immutable(prehash = true)
@Value.Style(strictBuilder = true)
public interface MethodInstance extends Comparable<MethodInstance> {

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

    /**
     * Returns human-readable method signature
     * @return method signature
     */
    default String getSignature() {
        return getVisibility().getDescription() + //public/private/...
                (getVisibility() == Visibility.PACKAGE_LOCAL ? "" : " ") +
                (isStatic() ? "static " : "") +
                (isFinal() ? "final " : (isAbstract() ? "abstract " : "")) + // final/abstract...
                getReturnType() + " " + getName() + "(" +
                getParameters().stream().collect(Collectors.joining(",")) + ")" +
                (getExceptions().isEmpty() ? "" : " throws " + getExceptions().stream().collect(Collectors.joining(",")));
    }

    default int compareTo(MethodInstance o) {
        int result = this.getName().compareTo(o.getName());
        if (result != 0) {
            return result;
        }
        result = this.getReturnType().compareTo(o.getReturnType());
        if (result != 0) {
            return result;
        }
        return this.getParameters().size() - o.getParameters().size();
    }
}


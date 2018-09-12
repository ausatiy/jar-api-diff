package ru.uskov.apidiff.classesmodel;

import org.immutables.value.Value;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents class including modifiers, name, interfaces and methods
 */
@Value.Immutable(prehash = true)
@Value.Style(strictBuilder = true)
public interface ClassInstance extends Comparable<ClassInstance>{
    String getName();

    String getParent();

    Set<String> getInterfaces();

    Set<MethodInstance> getMethods();

    boolean isFinal();

    boolean isInterface();

    boolean isAbstract();

    Visibility getVisibility();

    /**
     * Checks whether classes have similar attributes (modifiers, name, interfaces).
     * @param classInstance the instance to compare with
     * @return true if class attributes are equal, false otherwise
     */
    default boolean attributesEqual(ClassInstance instance) {
        return getName().equals(instance.getName()) &&
                getParent().equals(instance.getParent()) &&
                getInterfaces().equals(instance.getInterfaces()) &&
                isFinal() == instance.isFinal() &&
                isInterface() == instance.isInterface() &&
                isAbstract() == instance.isAbstract() &&
                getVisibility() == instance.getVisibility();
    }

    /**
     * Calculates java-style signature of class instance
     * @return text signature of class
     */
    default String getSignature() {
        return getVisibility().getDescription() + //public/private/...
                (getVisibility() == Visibility.PACKAGE_LOCAL ? "" : " ") +
                (isFinal() ? "final " : (isAbstract() ? (isInterface() ? "" : "abstract ") : "")) + // final/abstract...
                (isInterface() ? "interface " : "class ") +
                getName() +
                (getParent().equals("Object") ? "" : " extends " + getParent()) +
                (getInterfaces().isEmpty() ? "" : " implements " + getInterfaces().stream().collect(Collectors.joining(",")));
    }

    default int compareTo(ClassInstance o) {
        return getName().compareTo(o.getName());
    }
}


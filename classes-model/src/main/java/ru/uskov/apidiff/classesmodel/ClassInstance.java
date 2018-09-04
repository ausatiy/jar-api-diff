package ru.uskov.apidiff.classesmodel;

import org.immutables.value.Value;

import java.util.Set;
import java.util.stream.Collectors;

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

    default boolean attributesEqual(ClassInstance instance) {
        return getName().equals(instance.getName()) &&
                getParent().equals(instance.getParent()) &&
                getInterfaces().equals(instance.getInterfaces()) &&
                isFinal() == instance.isFinal() &&
                isInterface() == instance.isInterface() &&
                isAbstract() == instance.isAbstract() &&
                getVisibility() == instance.getVisibility();
    }

    //TODO test this method
    default String getSignature() {
        return getVisibility().getDescription() + //public/private/...
                (getVisibility() == Visibility.PACKAGE_LOCAL ? "" : " ") +
                (isFinal() ? "final " : (isAbstract() ? "abstract " : "")) + // final/abstract...
                (isInterface() ? "interface " : "class ") +
                getName() +
                (getParent().equals("Object") ? "" : " extends " + getParent()) +
                (getInterfaces().isEmpty() ? "" : " implements " + getInterfaces().stream().collect(Collectors.joining(",")));
    }

}


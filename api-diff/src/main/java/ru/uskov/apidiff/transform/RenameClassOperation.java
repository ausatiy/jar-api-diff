package ru.uskov.apidiff.transform;

import org.jetbrains.annotations.NotNull;
import ru.uskov.apidiff.classesmodel.ClassInstance;
import ru.uskov.apidiff.classesmodel.ImmutableClassInstance;
import ru.uskov.apidiff.classesmodel.ImmutableMethodInstance;
import ru.uskov.apidiff.classesmodel.MethodInstance;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RenameClassOperation implements TransformOperation {
    private final String from;
    private final String to;
    private final int weight;

    public RenameClassOperation(String from, String to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public Set<ClassInstance> apply(Set<ClassInstance> api) {
        Set<ClassInstance> result = new HashSet<>();
        for (ClassInstance instance : api) {
            Set<MethodInstance> newMethods = convertMethods(instance.getMethods(), from, to);
            if (! newMethods.equals(instance.getMethods())) {
                instance = ImmutableClassInstance.copyOf(instance).withMethods(newMethods);
            }

            if (from.equals(instance.getParent())) {
                instance = ImmutableClassInstance.copyOf(instance).withParent(to);
            }
            if (instance.getInterfaces().contains(from)) {
                Set<String> newInterfaces = new HashSet<>();
                newInterfaces.remove(from);
                newInterfaces.add(to);
                instance = ImmutableClassInstance.copyOf(instance).withInterfaces(newInterfaces);
            }
            if (from.equals(instance.getName())) {
                instance = ImmutableClassInstance.copyOf(instance).withName(to);
            }

            result.add(instance);

        }
        return result;
    }

    private boolean isTypeOf(String argumentType, String from) {
        return argumentType.replace("[]", "").equals(from);
    }

    //TODO nested classes are not supperted!
    private Set<MethodInstance> convertMethods(Set<MethodInstance> methods, String from, String to) {
        Set<MethodInstance> result = new HashSet<>();
        for (MethodInstance method : methods) {
            if (isTypeOf(method.getReturnType(), from)) {
                method = ImmutableMethodInstance.copyOf(method).withReturnType(method.getReturnType().replace(from, to));
            }
            if (method.getParameters().stream().anyMatch(x->isTypeOf(x, from))) {
                List<String> newArgs = method.getParameters().stream().map(x -> (isTypeOf(x, from) ? x.replace(from, to) : x)).collect(Collectors.toList());
                method = ImmutableMethodInstance.copyOf(method).withParameters(newArgs);
            }
            if (method.getExceptions().contains(from)) {
                method = ImmutableMethodInstance.copyOf(method).withExceptions(method.getExceptions().stream()
                        .map(x -> (x.equals(from) ? to : x)).collect(Collectors.toSet()));
            }
            result.add(method);
        }
        return result;
    }

    @Override
    public boolean isCommunicativeTo(TransformOperation operation) {
        if (! (operation instanceof RenameClassOperation)) {
            return false;
        }
        RenameClassOperation operation1 = (RenameClassOperation) operation;
        if (from.equals(operation1.from) || to.equals(operation1.to) ||
                to.equals(operation1.from) || from.equals(operation1.to)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int compareTo(@NotNull TransformOperation o) {
        if (! (o instanceof RenameClassOperation)) {
            throw new IllegalStateException("TODO");
        }
        RenameClassOperation o1 = (RenameClassOperation) o;
        if (from.compareTo(o1.from) != 0) {
            return from.compareTo(o1.from);
        }
        return to.compareTo(o1.to);
    }

    @Override
    public boolean equals(Object o) {
        if (! (o instanceof RenameClassOperation)) {
            return false;
        }
        RenameClassOperation o1 = (RenameClassOperation) o;
        return from.equals(o1.from) && to.equals(o1.to);
    }

    @Override
    public int hashCode() {
        return from.hashCode() + to.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Rename class [%s] to [%s]", from, to);
    }
}

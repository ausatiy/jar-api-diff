package ru.uskov.apidiff.transform.classmanipulation;

import ru.uskov.apidiff.classesmodel.*;
import ru.uskov.apidiff.transform.ClassManipulation;
import ru.uskov.apidiff.transform.TransformOperation;

import java.util.*;
import java.util.stream.Collectors;

public class RenameClassOperation implements TransformOperation, ClassManipulation {
    private final String from;
    private final String to;
    private final int weight;
    private final Api newApi;
    private final Map<ClassInstance, ClassInstance> modifiedClasses;
    private final Map<MethodInstance, MethodInstance> modifiedMethods;

    public RenameClassOperation(Api api, String from, String to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.modifiedClasses = new HashMap<>();
        this.modifiedMethods = new HashMap<>();

        Api currentApi = api;

        for (ClassInstance instance : api.getClasses()) {
            final ClassInstance sourceInstance = instance;
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

            if (! instance.equals(sourceInstance)) {
                modifiedClasses.put(instance, sourceInstance);
                currentApi = currentApi.replace(sourceInstance, instance);
            }
        }
        newApi = currentApi;
    }
    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public Api getNewApi() {
        return newApi;
    }

    @Override
    public ClassInstance getSourceClass(ClassInstance classInstance) {
        ClassInstance classInstance1 = modifiedClasses.get(classInstance);
        return classInstance1 == null ? classInstance : classInstance1;
    }

    @Override
    public MethodInstance getSourceMethod(MethodInstance methodInstance) {
        MethodInstance methodInstance1 = modifiedMethods.get(methodInstance);
        return methodInstance1 == null ? methodInstance : methodInstance1;
    }


    private boolean isTypeOf(String argumentType, String from) {
        return argumentType.replace("[]", "").equals(from);
    }

    //TODO nested classes are not supperted!
    private Set<MethodInstance> convertMethods(Set<MethodInstance> methods, String from, String to) {
        Set<MethodInstance> result = new HashSet<>();
        for (MethodInstance method : methods) {
            final MethodInstance sourceMethod = method;
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
            if (!sourceMethod.equals(method)) {
                modifiedMethods.put(method, sourceMethod);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return String.format("Class \"%s\" was renamed to \"%s\".", from, to);
    }

    @Override
    public boolean affectsClass(String name) {
        return from.equals(name) || to.equals(name);
    }
}

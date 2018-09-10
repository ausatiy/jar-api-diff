package ru.uskov.apidiff.transform.classmanipulation;

import ru.uskov.apidiff.classesmodel.ClassInstance;
import ru.uskov.apidiff.classesmodel.MethodInstance;
import ru.uskov.apidiff.transform.ClassManipulation;
import ru.uskov.apidiff.transform.TransformOperation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AddClassOperation implements TransformOperation, ClassManipulation {
    private final ClassInstance clazz;
    private final int weight;
    private final Set<ClassInstance> newApi;

    public AddClassOperation(Set<ClassInstance> api, ClassInstance clazz, int weight) {
        this.clazz = clazz;
        this.weight = weight;
        this.newApi = new HashSet<>(api);
        this.newApi.add(clazz);
    }
    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public Set<ClassInstance> getNewApi() {
        return Collections.unmodifiableSet(newApi);
    }

    @Override
    public ClassInstance getSourceClass(ClassInstance classInstance) {
        if (classInstance.equals(clazz)) {
            return null;
        }
        return classInstance;
    }

    @Override
    public MethodInstance getSourceMethod(MethodInstance methodInstance) {
        return methodInstance;
    }

    @Override
    public String toString() {
        return String.format("Class \"%s\" with %s methods was added.", clazz.getName(), clazz.getMethods().size());
    }

    @Override
    public boolean affectsClass(String name) {
        return clazz.getName().equals(name);
    }
}

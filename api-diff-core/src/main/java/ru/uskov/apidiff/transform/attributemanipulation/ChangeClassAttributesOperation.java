package ru.uskov.apidiff.transform.attributemanipulation;

import ru.uskov.apidiff.classesmodel.ClassInstance;
import ru.uskov.apidiff.classesmodel.MethodInstance;
import ru.uskov.apidiff.transform.TransformOperation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ChangeClassAttributesOperation implements TransformOperation {

    private final int weight;
    private final ClassInstance sourceClass;
    private final ClassInstance destinationClass;

    private final Set<ClassInstance> newApi;

    public ChangeClassAttributesOperation(Set<ClassInstance> oldApi, ClassInstance newClass, int weight) {
        this.weight = weight;
        this.destinationClass = newClass;
        Optional<ClassInstance> sourceClass = oldApi.stream().filter(x -> x.getName().equals(newClass.getName())).findAny();
        if (! sourceClass.isPresent()) {
            throw new IllegalStateException();//TODO
        }
        this.sourceClass = sourceClass.get();
        this.newApi = new HashSet<>(oldApi);
        this.newApi.remove(this.sourceClass);
        this.newApi.add(newClass);
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
        return destinationClass.equals(classInstance) ? sourceClass : classInstance;
    }

    @Override
    public MethodInstance getSourceMethod(MethodInstance methodInstance) {
        return methodInstance;
    }

    @Override
    public String toString() {
        return String.format("Class signature changed from %s to %s", sourceClass.getSignature(), destinationClass.getSignature());
    }
}

package ru.uskov.apidiff.classesmodel;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Representaion of API (set of classes with methods)
 */
public class Api {
    private final Map<String, ClassInstance> classes;
    private final int hash;

    public Api(Map<String, ClassInstance> classes) {
        this.classes = new HashMap<>(classes);
        int hash = 0;
        for (ClassInstance classInstance : getClasses()) {
            hash += classInstance.hashCode();
        }
        this.hash = hash;
    }

    public Api(Set<ClassInstance> classes) {
        this(classes.stream()
                .collect(Collectors.toMap(x->x.getName(), x->x)));
    }

    public Api replace(ClassInstance oldClass, ClassInstance newClass) {
        Map<String, ClassInstance> map = new HashMap<>(classes);
        if (oldClass != null) {
            map.remove(oldClass.getName());
        }
        if (newClass != null) {
            map.put(newClass.getName(), newClass);
        }
        return new Api(map);
    }

    public Set<String> getClassNames() {
        return Collections.unmodifiableSet(classes.keySet());
    }

    public Iterable<ClassInstance> getClasses() {
        return Collections.unmodifiableCollection(classes.values());
    }

    public Api removeClassByName(String className) {
        Map<String, ClassInstance> map = new HashMap<>(classes);
        map.remove(className);
        return new Api(map);
    }

    @Override
    public boolean equals(Object o) {
        if (o.hashCode() != hashCode()) {
            return false;
        }
        if (o instanceof Api) {
            return classes.equals(((Api)o).classes);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    public ClassInstance get(String s) {
        return classes.get(s);
    }
}

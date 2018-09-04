package ru.uskov.apidiff.parsing;

import ru.uskov.apidiff.classesmodel.*;
public class InstanceFilter {

    public boolean filter(ClassInstance clazz) {
        //TODO filter depending on visibility
        return true;
    }

    public boolean filter(MethodInstance method) {
        //TODO filter depending on visibility
        return true;
    }
}

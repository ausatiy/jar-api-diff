package ru.uskov.apidiff.classesmodel;

import ru.uskov.apidiff.classesmodel.objectnaming.ObjectNameMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Utils {

    public static Set<ClassInstance> readClassesToSet(String path, ObjectNameMapper objectNameMapper) throws IOException {
        final ClassParser classParser = new ClassParser(objectNameMapper);
        File file = new File(path);
        return classParser.readClasses(file);
    }
    public static Map<String, ClassInstance> readClasses(String path, ObjectNameMapper objectNameMapper) throws IOException {
        return readClassesToSet(path, objectNameMapper).stream().collect(Collectors.toMap(ClassInstance::getName, x -> x));
    }
}

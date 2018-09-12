package ru.uskov.apidiff.classesmodel;

import ru.uskov.apidiff.classesmodel.objectnaming.ObjectNameMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Utils {

    public static Api readClasses(String path, ObjectNameMapper objectNameMapper) throws IOException {
        final ClassParser classParser = new ClassParser(objectNameMapper);
        File file = new File(path);
        return classParser.readClasses(file);
    }

    public static MethodInstance getSomeMethod() {
        return ImmutableMethodInstance.builder()
                .name("methodName")
                .returnType("void")
                .isFinal(false)
                .isAbstract(false)
                .isStatic(false)
                .visibility(Visibility.PRIVATE)
                .build();
    }

    public static ClassInstance getSomeClass() {
        return  ImmutableClassInstance.builder()
                .name("someName")
                .parent("Parent")
                .isFinal(false)
                .isAbstract(false)
                .isInterface(false)
                .visibility(Visibility.PUBLIC)
                .build();
    }
}

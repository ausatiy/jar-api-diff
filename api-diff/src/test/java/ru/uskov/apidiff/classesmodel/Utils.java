package ru.uskov.apidiff.classesmodel;

import ru.uskov.apidiff.parsing.ClassParser;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Utils {

    public static Map<String, ClassInstance> readClasses(String path) throws IOException {
        final ClassParser classParser = new ClassParser();
        File file = new File("../test-resources/parseCases/build/libs/parseCases.jar");
        return classParser.readClasses(file);
    }
}

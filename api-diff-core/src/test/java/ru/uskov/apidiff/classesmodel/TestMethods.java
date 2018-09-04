package ru.uskov.apidiff.classesmodel;


import org.junit.BeforeClass;
import org.junit.Test;
import ru.uskov.apidiff.classesmodel.objectnaming.JavaStyleObjectNameMapper;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class TestMethods {
    private static Map<String, MethodInstance> methods;

    @BeforeClass
    public static void prepare() throws IOException {
        Map<String, ClassInstance> classes = Utils.readClasses("../test-resources/parseCases/build/libs/parseCases.jar", new JavaStyleObjectNameMapper());
        ClassInstance clazz = classes.get("some.packagename.ClassWithMethods");
        methods = clazz.getMethods().stream().collect(Collectors.toMap(MethodInstance::getName, Function.identity()));
    }

    @Test
    public void protectedMethod() throws IOException {
        final MethodInstance staticMethod = methods.get("protectedMethod");
        assertEquals(Collections.emptySet(), staticMethod.getExceptions());
        assertEquals(Collections.emptyList(), staticMethod.getParameters());
        assertEquals("void", staticMethod.getReturnType());
        assertEquals(Visibility.PROTECTED, staticMethod.getVisibility());
        assertEquals(false, staticMethod.isAbstract());
        assertEquals(false, staticMethod.isFinal());
        assertEquals(false, staticMethod.isStatic());
    }

    @Test
    public void privateMethod() throws IOException {
        final MethodInstance staticMethod = methods.get("privateMethod");
        assertEquals(Collections.emptySet(), staticMethod.getExceptions());
        assertEquals(Collections.emptyList(), staticMethod.getParameters());
        assertEquals("void", staticMethod.getReturnType());
        assertEquals(Visibility.PRIVATE, staticMethod.getVisibility());
        assertEquals(false, staticMethod.isAbstract());
        assertEquals(false, staticMethod.isFinal());
        assertEquals(false, staticMethod.isStatic());
    }

    @Test
    public void packageLocalMethod() throws IOException {
        final MethodInstance staticMethod = methods.get("packageLocalMethod");
        assertEquals(Collections.emptySet(), staticMethod.getExceptions());
        assertEquals(Collections.emptyList(), staticMethod.getParameters());
        assertEquals("void", staticMethod.getReturnType());
        assertEquals(Visibility.PACKAGE_LOCAL, staticMethod.getVisibility());
        assertEquals(false, staticMethod.isAbstract());
        assertEquals(false, staticMethod.isFinal());
        assertEquals(false, staticMethod.isStatic());
    }

    @Test
    public void staticMethod() throws IOException {
        final MethodInstance staticMethod = methods.get("staticMethod");
        assertEquals(Collections.emptySet(), staticMethod.getExceptions());
        assertEquals(Collections.emptyList(), staticMethod.getParameters());
        assertEquals("void", staticMethod.getReturnType());
        assertEquals(Visibility.PUBLIC, staticMethod.getVisibility());
        assertEquals(false, staticMethod.isAbstract());
        assertEquals(false, staticMethod.isFinal());
        assertEquals(true, staticMethod.isStatic());
    }

    @Test
    public void staticFinalMethod() throws IOException {
        final MethodInstance staticMethod = methods.get("staticFinalMethod");
        assertEquals(Collections.emptySet(), staticMethod.getExceptions());
        assertEquals(Collections.emptyList(), staticMethod.getParameters());
        assertEquals("void", staticMethod.getReturnType());
        assertEquals(Visibility.PUBLIC, staticMethod.getVisibility());
        assertEquals(false, staticMethod.isAbstract());
        assertEquals(true, staticMethod.isFinal());
        assertEquals(true, staticMethod.isStatic());
    }

    @Test
    public void abstractMethod() throws IOException {
        final MethodInstance staticMethod = methods.get("abstractMethod");
        assertEquals(Collections.emptySet(), staticMethod.getExceptions());
        assertEquals(Collections.emptyList(), staticMethod.getParameters());
        assertEquals("void", staticMethod.getReturnType());
        assertEquals(Visibility.PUBLIC, staticMethod.getVisibility());
        assertEquals(true, staticMethod.isAbstract());
        assertEquals(false, staticMethod.isFinal());
        assertEquals(false, staticMethod.isStatic());
    }

    @Test
    public void finalMethod() throws IOException {
        final MethodInstance staticMethod = methods.get("finalMethod");
        assertEquals(Collections.emptySet(), staticMethod.getExceptions());
        assertEquals(Collections.emptyList(), staticMethod.getParameters());
        assertEquals("void", staticMethod.getReturnType());
        assertEquals(Visibility.PUBLIC, staticMethod.getVisibility());
        assertEquals(false, staticMethod.isAbstract());
        assertEquals(true, staticMethod.isFinal());
        assertEquals(false, staticMethod.isStatic());
    }

    @Test
    public void methodWithArguments() throws IOException {
        final MethodInstance staticMethod = methods.get("methodWithArguments");
        assertEquals(new HashSet<>(Arrays.asList("java.io.IOException", "RuntimeException")), staticMethod.getExceptions());
        assertEquals(Arrays.asList("int", "Object", "String", "Integer", "java.util.List"), staticMethod.getParameters());
        assertEquals("String", staticMethod.getReturnType());
        assertEquals(Visibility.PUBLIC, staticMethod.getVisibility());
        assertEquals(false, staticMethod.isAbstract());
        assertEquals(false, staticMethod.isFinal());
        assertEquals(false, staticMethod.isStatic());
    }

    @Test
    public void methodWithArrayArguments() throws IOException {
        final MethodInstance staticMethod = methods.get("methodWithArrayArguments");
        assertEquals(new HashSet<>(Arrays.asList("java.io.IOException", "RuntimeException")), staticMethod.getExceptions());
        assertEquals(Arrays.asList("int[]", "Object[]", "String[]", "Integer[]", "java.util.List[]"), staticMethod.getParameters());
        assertEquals("String[]", staticMethod.getReturnType());
        assertEquals(Visibility.PUBLIC, staticMethod.getVisibility());
        assertEquals(false, staticMethod.isAbstract());
        assertEquals(false, staticMethod.isFinal());
        assertEquals(false, staticMethod.isStatic());
    }
}

package ru.uskov.apidiff.classesmodel;


import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;
import ru.uskov.apidiff.parsing.ClassParser;
import ru.uskov.apidiff.classesmodel.Visibility;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class TestClassParser {
    private static Map<String, ClassInstance> classes;

    @BeforeClass
    public static void prepare() throws IOException {
        Utils.readClasses("../test-resources/parseCases/build/libs/parseCases.jar");
    }

    @Test
    public void testPublicClass() throws IOException {
        ClassInstance clazz = classes.get("some/packagename/PublicClass");
        assertNotNull(clazz);

        assertFalse(clazz.isAbstract());
        assertFalse(clazz.isFinal());
        assertFalse(clazz.isInterface());

        assertEquals(Visibility.PUBLIC, clazz.getVisibility());
        assertEquals("java/lang/Object", clazz.getParent());
        assertEquals(Collections.singleton("java/io/Serializable"), clazz.getInterfaces());
    }

    @Test
    public void testPackageLocalClass() throws IOException {
        ClassInstance clazz = classes.get("some/packagename/PackageLocalClass");
        assertNotNull(clazz);

        assertFalse(clazz.isAbstract());
        assertTrue(clazz.isFinal());
        assertFalse(clazz.isInterface());

        assertEquals(Visibility.PACKAGE_LOCAL, clazz.getVisibility());
        assertEquals("java/lang/Object", clazz.getParent());
        assertEquals(Collections.emptySet(), clazz.getInterfaces());
    }

    @Test
    public void testAbstractClass() throws IOException {
        ClassInstance clazz = classes.get("some/packagename/AbstractClass");
        assertNotNull(clazz);

        assertTrue(clazz.isAbstract());
        assertFalse(clazz.isFinal());
        assertFalse(clazz.isInterface());

        assertEquals(Visibility.PACKAGE_LOCAL, clazz.getVisibility());
        assertEquals("java/lang/Object", clazz.getParent());
        assertEquals(Collections.emptySet(), clazz.getInterfaces());
    }

    @Test
    public void testPrivateClass() throws IOException {
        ClassInstance clazz = classes.get("some/packagename/PackageLocalClass$PrivateClass");
        assertNotNull(clazz);

        assertFalse(clazz.isAbstract());
        assertFalse(clazz.isFinal());
        assertFalse(clazz.isInterface());

        assertEquals(Visibility.PRIVATE, clazz.getVisibility());
        assertEquals("java/lang/Object", clazz.getParent());
        assertEquals(Collections.emptySet(), clazz.getInterfaces());
    }

    @Test
    public void testSomeInterface() throws IOException {
        ClassInstance clazz = classes.get("some/packagename/SomeInterface");
        assertNotNull(clazz);

        assertTrue(clazz.isAbstract());
        assertFalse(clazz.isFinal());
        assertTrue(clazz.isInterface());

        assertEquals(Visibility.PUBLIC, clazz.getVisibility());
        assertEquals("java/lang/Object", clazz.getParent());
        assertEquals(Collections.singleton("java/io/Closeable"), clazz.getInterfaces());
    }

    @Test
    public void testInnerClass1() throws IOException {
        ClassInstance clazz = classes.get("some/packagename/PackageLocalClass$InnerClass");
        assertNotNull(clazz);

        assertFalse(clazz.isAbstract());
        assertFalse(clazz.isFinal());
        assertFalse(clazz.isInterface());
        assertEquals(Visibility.PUBLIC, clazz.getVisibility());
    }

    @Test
    public void testInnerClass2() throws IOException {
        ClassInstance clazz = classes.get("some/packagename/PackageLocalClass$InnerClass$SecondLevelInnerClass");
        assertNotNull(clazz);

        assertTrue(clazz.isAbstract());
        assertFalse(clazz.isFinal());
        assertFalse(clazz.isInterface());
        assertEquals(Visibility.PRIVATE, clazz.getVisibility());
    }

    @Test
    public void testInnerClass3() throws IOException {
        ClassInstance clazz = classes.get("some/packagename/PackageLocalClass$InnerClass$SecondLevelInnerClass$ThirdLevelInnerClass");
        assertNotNull(clazz);

        assertFalse(clazz.isAbstract());
        assertTrue(clazz.isFinal());
        assertFalse(clazz.isInterface());
        assertEquals(Visibility.PRIVATE, clazz.getVisibility());
    }


}

package ru.uskov.apidiff.classesmodel;


import org.junit.BeforeClass;
import org.junit.Test;
import ru.uskov.apidiff.parsing.ClassParser;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestMethods {
    private static Map<String, ClassInstance> classes;

    @BeforeClass
    public static void prepare() throws IOException {
        classes = Utils.readClasses("../test-resources/parseCases/build/libs/parseCases.jar");
    }

    @Test
    public void testPublicClass() throws IOException {
        ClassInstance clazz = classes.get("some/packagename/ClassWithMethods");
        assertNotNull(clazz);

        assertFalse(clazz.isAbstract());
        assertFalse(clazz.isFinal());
        assertFalse(clazz.isInterface());

        assertEquals(Visibility.PUBLIC, clazz.getVisibility());
        assertEquals("java/lang/Object", clazz.getParent());
        assertEquals(Collections.singleton("java/io/Serializable"), clazz.getInterfaces());
    }
}

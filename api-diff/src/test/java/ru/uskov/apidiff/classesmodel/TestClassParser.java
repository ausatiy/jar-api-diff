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
        final ClassParser classParser = new ClassParser();
        File file = new File("../test-resources/parseCases/build/libs/parseCases.jar");
        classes = classParser.readClasses(file);
    }

    @Test
    public void testPublicClass() throws IOException {
        ClassInstance publicClass = classes.get("some.packagename.PublicClass");
        assertNotNull(publicClass);

        assertFalse(publicClass.isAbstract());
        assertFalse(publicClass.isFinal());
        assertFalse(publicClass.isInterface());

        assertEquals(Visibility.PUBLIC, publicClass.getVisibility());
        assertEquals("java.lang.Object", publicClass.getParent());
        assertEquals(Collections.singleton("java.io.Serializable"), publicClass.getInterfaces());
    }
}

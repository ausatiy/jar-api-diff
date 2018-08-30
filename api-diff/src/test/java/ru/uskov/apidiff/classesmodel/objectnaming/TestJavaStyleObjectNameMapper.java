package ru.uskov.apidiff.classesmodel.objectnaming;

import org.junit.Test;
import ru.uskov.apidiff.objectnaming.JavaStyleObjectNameMapper;
import ru.uskov.apidiff.objectnaming.ObjectNameMapper;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class TestJavaStyleObjectNameMapper {

    private final ObjectNameMapper mapper = new JavaStyleObjectNameMapper();

    @Test
    public void testClassRenamer() {
        assertEquals("SomeClass", mapper.convertClassName("SomeClass"));
        assertEquals("package.SomeClass", mapper.convertClassName("package/SomeClass"));
    }

    @Test
    public void testVoid() {
        assertEquals(Collections.singletonList("void"), mapper.convertArgumentTypes("V"));
    }

    @Test
    public void testPrimitiveTypes() {
        assertEquals(Collections.singletonList("byte"), mapper.convertArgumentTypes("B"));
        assertEquals(Collections.singletonList("byte[]"), mapper.convertArgumentTypes("[B"));
        assertEquals(Collections.singletonList("byte[][]"), mapper.convertArgumentTypes("[[B"));

        assertEquals(Collections.singletonList("char"), mapper.convertArgumentTypes("C"));
        assertEquals(Collections.singletonList("char[]"), mapper.convertArgumentTypes("[C"));

        assertEquals(Collections.singletonList("double"), mapper.convertArgumentTypes("D"));

        assertEquals(Collections.singletonList("float"), mapper.convertArgumentTypes("F"));

        assertEquals(Collections.singletonList("int"), mapper.convertArgumentTypes("I"));

        assertEquals(Collections.singletonList("long"), mapper.convertArgumentTypes("J"));

        assertEquals(Collections.singletonList("short"), mapper.convertArgumentTypes("S"));

        assertEquals(Collections.singletonList("boolean"), mapper.convertArgumentTypes("Z"));
    }


    @Test
    public void testObjectsTypeRenamer() {
        assertEquals(Collections.singletonList("void"), mapper.convertArgumentTypes("V"));
        assertEquals(Collections.singletonList("String"), mapper.convertArgumentTypes("Ljava/lang/String;"));
        assertEquals(Collections.singletonList("String[]"), mapper.convertArgumentTypes("[Ljava/lang/String;"));
        assertEquals(Collections.singletonList("String[][]"), mapper.convertArgumentTypes("[[Ljava/lang/String;"));
        assertEquals(Collections.singletonList("String[][][]"), mapper.convertArgumentTypes("[[[Ljava/lang/String;"));

        assertEquals(Collections.singletonList("some.ClassName"), mapper.convertArgumentTypes("Lsome/ClassName;"));
        assertEquals(Collections.singletonList("some.ClassName[]"), mapper.convertArgumentTypes("[Lsome/ClassName;"));
    }

    @Test
    public void testList() {
        assertEquals(Arrays.asList("int", "char", "byte", "Object", "String[]", "short"), mapper.convertArgumentTypes("ICBLjava/lang/Object;[Ljava/lang/String;S"));

    }
}

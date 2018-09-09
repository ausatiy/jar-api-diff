package ru.uskov.apidiff.classesmodel;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.uskov.apidiff.classesmodel.objectnaming.JavaStyleObjectNameMapper;

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class TestReadInstructions {

    private static Map<String, ClassInstance> classes;

    @BeforeClass
    public static void prepare() throws IOException {
        classes = Utils.readClasses("../test-resources/parseCases/build/libs/parseCases.jar", new JavaStyleObjectNameMapper());
    }

    /**
     * This test checks byte-code instructions are read properly.
     * The methods with the same source code should be equal at the instruction level.
     */
    @Test
    public void testSameInstructions() throws IOException {
        ClassInstance clazz = classes.get("some.packagename.ClassWithInstructions");
        assertNotNull(clazz);

        Map<String, MethodInstance> methods = clazz.getMethods().stream().collect(Collectors.toMap(x->x.getName(), Function.identity()));

        assertEquals(methods.get("doSmth_01").getInstructions(), methods.get("doSmth_02").getInstructions());
        assertNotEquals(methods.get("doSmth_01").getInstructions(), methods.get("doSmth_03").getInstructions());
        assertNotEquals(methods.get("doSmth_01").getInstructions(), methods.get("doSmth_04").getInstructions());

        assertEquals(methods.get("doSmth_05").getInstructions(), methods.get("doSmth_06").getInstructions());
        assertNotEquals(methods.get("doSmth_05").getInstructions(), methods.get("doSmth_07").getInstructions());

        assertEquals(methods.get("doSmth_08").getInstructions(), methods.get("doSmth_09").getInstructions());
        assertNotEquals(methods.get("doSmth_08").getInstructions(), methods.get("doSmth_10").getInstructions());
    }

}

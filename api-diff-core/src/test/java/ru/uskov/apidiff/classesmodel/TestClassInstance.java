package ru.uskov.apidiff.classesmodel;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestClassInstance {

    @Test
    public void testCompareAttributes() {
        ClassInstance instance = Utils.getSomeClass();
        assertTrue(instance.attributesEqual(instance));
        assertTrue(instance.attributesEqual(ImmutableClassInstance
                .copyOf(instance)
                .withMethods(Utils.getSomeMethod())));


        assertFalse(instance.attributesEqual(ImmutableClassInstance
                .copyOf(instance)
                .withInterfaces("interface")));

        assertFalse(instance.attributesEqual(ImmutableClassInstance
                .copyOf(instance)
                .withParent("Another")));

        assertFalse(instance.attributesEqual(ImmutableClassInstance
                .copyOf(instance)
                .withInterfaces("Another")));

        assertFalse(instance.attributesEqual(ImmutableClassInstance
                .copyOf(instance)
                .withName("Another")));

        assertFalse(instance.attributesEqual(ImmutableClassInstance
                .copyOf(instance)
                .withIsAbstract(true)));

        assertFalse(instance.attributesEqual(ImmutableClassInstance
                .copyOf(instance)
                .withIsFinal(true)));

        assertFalse(instance.attributesEqual(ImmutableClassInstance
                .copyOf(instance)
                .withVisibility(Visibility.PACKAGE_LOCAL)));
    }

    @Test
    public void testToString() {
        ClassInstance instance = Utils.getSomeClass();
        assertEquals("public class someName extends Parent", instance.getSignature());
        assertEquals("abstract class someName extends Parent", ImmutableClassInstance.copyOf(instance)
                .withIsAbstract(true)
                .withVisibility(Visibility.PACKAGE_LOCAL)
                .getSignature());
        assertEquals("final class someName extends Parent", ImmutableClassInstance.copyOf(instance)
                .withIsFinal(true)
                .withVisibility(Visibility.PACKAGE_LOCAL)
                .getSignature());
    }

}

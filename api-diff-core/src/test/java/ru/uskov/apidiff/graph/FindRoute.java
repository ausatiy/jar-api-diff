package ru.uskov.apidiff.graph;

import org.junit.Test;
import ru.uskov.apidiff.classesmodel.ClassInstance;
import ru.uskov.apidiff.classesmodel.Utils;
import ru.uskov.apidiff.classesmodel.objectnaming.JavaStyleObjectNameMapper;
import ru.uskov.apidiff.transform.classmanipulation.RenameClassOperation;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

public class FindRoute {


    @Test
    public void testRenameClass() throws IOException {
        Set<ClassInstance> oldClasses = Utils.readClassesToSet("../test-resources/baseClasses/build/libs/baseClasses.jar", new JavaStyleObjectNameMapper());
        Set<ClassInstance> newClasses = Utils.readClassesToSet("../test-resources/renameBaseClasses/build/libs/renameBaseClasses.jar", new JavaStyleObjectNameMapper());
        GraphNode finalGraphNode = new Graph(oldClasses).pathTo(newClasses);
        assertNotNull(finalGraphNode);
        assertEquals(newClasses, finalGraphNode.getApi());
        assertEquals(1, finalGraphNode.getTransforms().size());
    }

    @Test
    public void testAddRmClasses() throws IOException {
        Set<ClassInstance> oldClasses = Utils.readClassesToSet("../test-resources/baseClasses/build/libs/baseClasses.jar", new JavaStyleObjectNameMapper());
        Set<ClassInstance> newClasses = Utils.readClassesToSet("../test-resources/addRmRenameClasses/build/libs/addRmRenameClasses.jar", new JavaStyleObjectNameMapper());
        GraphNode finalGraphNode = new Graph(oldClasses).pathTo(newClasses);
        assertNotNull(finalGraphNode);
        assertEquals(newClasses, finalGraphNode.getApi());
        assertEquals(4, finalGraphNode.getTransforms().size());
    }

}
